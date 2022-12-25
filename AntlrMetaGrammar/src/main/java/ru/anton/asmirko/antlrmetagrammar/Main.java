package ru.anton.asmirko.antlrmetagrammar;

import kotlin.text.Regex;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import ru.anton.asmirko.grammar.*;
import ru.anton.asmirko.graphviz.TreeDrawer;
import ru.anton.asmirko.parser.lexer.RegexLexer;
import ru.anton.asmirko.parser.parser.RegexParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    private static final Regex quoted = new Regex("'.*'");
    private static final Regex regex = new Regex("/.*/");

    private static String unquote(String withQuotes) {
        return withQuotes.substring(1, withQuotes.length() - 1);
    }

    private static Set<String> skip = Set.of(";");

    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You must specify input file");
        }
        try {
            var inputFile = args[0];
            var inputStream = CharStreams.fromFileName(inputFile);
            var lexer = new MetaGrammarLexer(inputStream);
            var tokens = new CommonTokenStream(lexer);
            var parser = new MetaGrammarParser(tokens);
            var tree = parser.rules();
            var grammar = treeToGrammar(tree, tokens);
            var lexer1 = new RegexLexer(grammar);
            var parser1 = new RegexParser(grammar, lexer1);
            var toPlot = List.of(
                    "1 + 3 * ( 2 +    1 )",
                    "1",
                    "(1)",
                    "( (    ((1)) ) )",
                    "1 * (2 + 3)",
                    "1 + 2 + 3 + 4 + 5",
                    "(1 + 2 + 4) * 4"

            );
            for (var item : toPlot) {
                var result = parser1.parse(Arrays.asList(item.split("")));
                var treeDrawer = new TreeDrawer();
                treeDrawer.drawTree(result, item, "graphs/arithmetics");
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    private static Grammar treeToGrammar(MetaGrammarParser.RulesContext ctx, CommonTokenStream tokens) {
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
                    .map(it -> new Rule(new NonTerminalToken(ruleName), it))
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
                        .map(Main::unquote)
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
