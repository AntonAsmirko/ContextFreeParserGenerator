package ru.anton.asmirko.antlrmetagrammar;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import ru.anton.asmirko.antlrmetagrammar.utills.MetaGrammarUtils;
import ru.anton.asmirko.graphviz.TreeDrawer;
import ru.anton.asmirko.parser.lexer.RegexLexer;
import ru.anton.asmirko.parser.parser.RegexParser;
import ru.anton.asmirko.tree.TreeWithAttributes;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {

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
            var grammar = MetaGrammarUtils.treeToGrammar(tree, tokens);
            System.out.println();
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
                var evRes = ((TreeWithAttributes) result).yield();
                var treeDrawer = new TreeDrawer();
                treeDrawer.drawTree(result, item, "graphs/arithmetics");
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
