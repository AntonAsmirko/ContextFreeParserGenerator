package ru.anton.asmirko.antlrcalculator;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import ru.anton.asmirko.antlrcalculator.antlr.ExprLexer;
import ru.anton.asmirko.antlrcalculator.antlr.ExprParser;
import ru.anton.asmirko.graphviz.TreeDrawer;
import ru.anton.asmirko.tree.TreeWithAttributes;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            System.out.println("You must specify input file");
        }
        try {
            var inputFile = args[0];
            var inputStream = CharStreams.fromFileName(inputFile);
            var lexer = new ExprLexer(inputStream);
            var tokens = new CommonTokenStream(lexer);
            var parser = new ExprParser(tokens);
            var tree = parser.prog();
            var exprTreeVisitor = new ExprToTreeVisitor();
            var treeInternal = exprTreeVisitor.visit(tree);
            var treeDrawer = new TreeDrawer();
            treeDrawer.drawTree(treeInternal, "first-test", "graphs/antlr-calc");
            System.out.println(((TreeWithAttributes) treeInternal).yield());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
