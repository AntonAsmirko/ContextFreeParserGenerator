package ru.anton.asmirko.antlrcalculator;

import kotlin.text.Regex;
import org.antlr.v4.runtime.ParserRuleContext;
import ru.anton.asmirko.grammar.NonTerminalToken;
import ru.anton.asmirko.grammar.TerminalToken;
import ru.anton.asmirko.tree.Tree;
import ru.anton.asmirko.tree.TreeImpl;
import ru.anton.asmirko.tree.TreeWithAttributes;
import ru.anton.asmirko.tree.TreeWithAttributesImpl;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ExprToTreeVisitor extends ExprBaseVisitor<Tree<String>> {

    private final Map<String, Function<List<String>, String>> fnMap = Map.of(
            "+", l -> String.valueOf(Integer.parseInt(handleVariable(l.get(0))) + Integer.parseInt(handleVariable(l.get(1)))),
            "/", l -> String.valueOf(Integer.parseInt(handleVariable(l.get(0))) / Integer.parseInt(handleVariable(l.get(1)))),
            "*", l -> String.valueOf(Integer.parseInt(handleVariable(l.get(0))) * Integer.parseInt(handleVariable(l.get(1)))),
            "-", l -> String.valueOf(Integer.parseInt(handleVariable(l.get(0))) - Integer.parseInt(handleVariable(l.get(1))))
    );

    private final HashMap<String, String> variablesValues = new HashMap<>();
    private final Regex varRegex = new Regex("[a-z]+");

    private String handleVariable(String maybeVar) {
        if (varRegex.matches(maybeVar)) {
            return variablesValues.get(maybeVar);
        }
        return maybeVar;
    }

    @Override
    public Tree<String> visitProg(ExprParser.ProgContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    protected Tree<String> aggregateResult(Tree<String> aggregate, Tree<String> nextResult) {
        if (aggregate != null) {
            aggregate.getChildren().add(nextResult);
            return aggregate;
        } else {
            var agg = new TreeWithAttributesImpl<>(
                    new NonTerminalToken<>("S"),
                    new ArrayList<>(),
                    (children, value) -> children.stream()
                            .filter(child -> child instanceof TreeWithAttributes)
                            .map(child -> ((TreeWithAttributes<String, String>) child).yield())
                            .map(Object::toString)
                            .collect(Collectors.joining("\n"))
            );
            agg.getChildren().add(nextResult);
            return agg;
        }
    }

    @Override
    public Tree<String> visitAssign(ExprParser.AssignContext ctx) {
        var valName = ctx.getChild(0);
        var assign = ctx.getChild(1);
        var expr = ctx.getChild(2);
        var lineEnd = ctx.getChild(3);
        var children = new ArrayList<Tree<String>>();
        children.add(new TreeWithAttributesImpl<>(
                new TerminalToken<>(valName.getText()),
                new LinkedList<>(),
                (ch, value) -> value
        ));
        children.add(new TreeImpl<>(new TerminalToken<>(assign.getText()), new LinkedList<>()));
        children.add(visit(expr));
        children.add(new TreeImpl<>(new TerminalToken<>(lineEnd.getText()), new LinkedList<>()));
        return new TreeWithAttributesImpl<>(
                new NonTerminalToken<>("assign"),
                children,
                (ch, value) -> {
                    var attrs = ch.stream()
                            .filter(child -> child instanceof TreeWithAttributes)
                            .map(child -> ((TreeWithAttributes) child).yield())
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    if (varRegex.matches(attrs.get(1))) {
                        variablesValues.put(attrs.get(0), handleVariable(attrs.get(1)));
                        return String.format("%s = %s", attrs.get(0), handleVariable(attrs.get(1)));
                    } else {
                        variablesValues.put(attrs.get(0), attrs.get(1));
                        return String.format("%s = %s", attrs.get(0), attrs.get(1));
                    }
                }
        );
    }

    @Override
    public Tree<String> visitParens(ExprParser.ParensContext ctx) {
        var lParen = ctx.getChild(0);
        var expr = ctx.getChild(1);
        var rParen = ctx.getChild(2);
        var children = new ArrayList<Tree<String>>();
        children.add(new TreeImpl<>(new TerminalToken<>(lParen.getText()), new LinkedList<>()));
        children.add(visit(expr));
        children.add(new TreeImpl<>(new TerminalToken<>(rParen.getText()), new LinkedList<>()));
        return new TreeWithAttributesImpl<>(
                new NonTerminalToken<>("()"),
                children,
                (ch, value) -> {
                    var attrs = ch.stream()
                            .filter(child -> child instanceof TreeWithAttributes)
                            .map(child -> ((TreeWithAttributes<String, String>) child).yield())
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    return attrs.get(0);
                }
        );
    }

    @Override
    public Tree<String> visitMullDiv(ExprParser.MullDivContext ctx) {
        return visitBinary(ctx);
    }

    @Override
    public Tree<String> visitAddSub(ExprParser.AddSubContext ctx) {
        return visitBinary(ctx);
    }

    @Override
    public Tree<String> visitId(ExprParser.IdContext ctx) {
        return new TreeWithAttributesImpl<>(
                new TerminalToken<>(ctx.getText()),
                new LinkedList<>(),
                (ch, value) -> value.toString()
        );
    }

    @Override
    public Tree<String> visitInt(ExprParser.IntContext ctx) {
        return new TreeWithAttributesImpl<>(
                new TerminalToken<>(ctx.getText()),
                new LinkedList<>(),
                (ch, value) -> value.toString()
        );
    }

    private Tree<String> visitBinary(ParserRuleContext ctx) {
        var lExpr = ctx.getChild(0);
        var op = ctx.getChild(1);
        var rExpr = ctx.getChild(2);
        return new TreeWithAttributesImpl<>(
                new NonTerminalToken<>(op.getText()),
                List.of(
                        visit(lExpr),
                        visit(rExpr)
                ),
                (ch, value) -> {
                    var attrs = ch.stream()
                            .filter(child -> child instanceof TreeWithAttributes)
                            .map(child -> ((TreeWithAttributes<String, String>) child).yield())
                            .map(Object::toString)
                            .collect(Collectors.toList());
                    return fnMap.get(op.getText()).apply(attrs);
                }
        );
    }
}
