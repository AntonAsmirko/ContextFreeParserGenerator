package ru.anton.asmirko.antlrmetagrammar.utills;

import kotlin.text.Regex;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarParser;
import ru.anton.asmirko.grammar.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class MetaGrammarUtils {

    private static final Regex quoted = new Regex("'.*'");
    private static final Regex regex = new Regex("/.*/");
    private static final Regex action = new Regex("-\\{.*\\}-");

    private static String unquote(String withQuotes) {
        return withQuotes.substring(1, withQuotes.length() - 1);
    }

    private static Set<String> skip = Set.of(";");

    private static void traverseNode(ParseTree node,
                                     Set<String> nonTerminals,
                                     ArrayList<Token> result) {
        if (node instanceof TerminalNodeImpl) {
            var text = node.getText();
            if (quoted.matches(text)) {
                var unquoted = unquote(text);
                if (nonTerminals.contains(unquoted)) {
                    result.add(new NonTerminalToken(unquoted));
                } else {
                    result.add(new TerminalToken(unquoted));
                }
            } else {
                if (nonTerminals.contains(text)) {
                    result.add(new NonTerminalToken(text));
                } else {
                    if (!skip.contains(text)) {
                        result.add(new TerminalToken(text));
                    }
                }
            }
        } else {
            for (var i = 0; i < node.getChildCount(); i++) {
                traverseNode(node.getChild(i), nonTerminals, result);
            }
        }
    }

    public static List<String> getTokens(CommonTokenStream cts) {
        return cts.getTokens().stream()
                .map(org.antlr.v4.runtime.Token::getText)
                .filter(it -> quoted.matches(it) || regex.matches(it))
                .map(it -> it.substring(1, it.length() - 1))
                .collect(Collectors.toList());
    }

    public static Grammar treeToGrammar(MetaGrammarParser.RulesContext ctx, CommonTokenStream tokens) {
        var children = ctx.children;
        var nonTerminals = children.stream()
                .map(child -> child.getChild(0).getText())
                .collect(Collectors.toSet());
        var rules = new ArrayList<Rule>();
        for (var child : children) {
            var ruleName = child.getChild(0).getText();
            var alternatives = new ArrayList<ArrayList<Token>>();
            alternatives.add(new ArrayList<>());
            for (var i = 2; i < child.getChildCount(); i++) {
                var cur = child.getChild(i);
                if (cur instanceof TerminalNodeImpl) {
                    var text = cur.getText();
                    if ("|".equals(text)) {
                        alternatives.add(new ArrayList<>());
                    } else {
                        traverseNode(cur, nonTerminals, alternatives.get(alternatives.size() - 1));
                    }
                } else {
                    traverseNode(cur, nonTerminals, alternatives.get(alternatives.size() - 1));
                }
            }
            rules.addAll(alternatives.stream()
                    .map(it -> {
                        if (action.matches(it.get(it.size() - 1).getValue())) {
                            var actionStr = it.get(it.size() - 1).getValue();
                            it.remove(it.size() - 1);
                            return new Rule(new NonTerminalToken(ruleName), it, actionStr.substring(1, actionStr.length() - 1));
                        } else {
                            return new Rule(new NonTerminalToken(ruleName), it, null);
                        }
                    })
                    .collect(Collectors.toList()));
        }
        return new Grammar(
                rules,
                nonTerminals.stream()
                        .map(NonTerminalToken::new)
                        .collect(Collectors.toSet()),
                tokens.getTokens().stream()
                        .map(org.antlr.v4.runtime.Token::getText)
                        .filter(quoted::matches)
                        .map(MetaGrammarUtils::unquote)
                        .collect(Collectors.toSet()),
                new EpsilonToken("ε"),
                rules.get(0).getNonTerminal(),
                rules.stream().flatMap(
                        rule -> rule.getRightSide().stream()
                                .map(Token::getValue)
                                .filter(regex::matches)
                                .map(it -> it.substring(1, it.length() - 1))
                                .map(Regex::new)
                ).collect(Collectors.toSet())
        );
    }
}
