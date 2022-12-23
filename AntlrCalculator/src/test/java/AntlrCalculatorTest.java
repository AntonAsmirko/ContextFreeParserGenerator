import com.google.common.truth.Truth;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.anton.asmirko.antlrcalculator.ExprToTreeVisitor;
import ru.anton.asmirko.antlrcalculator.antlr.ExprLexer;
import ru.anton.asmirko.antlrcalculator.antlr.ExprParser;
import ru.anton.asmirko.tree.TreeWithAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AntlrCalculatorTest {

    @CsvSource(value = {
            "input/test1.txt, result/test1.txt",
            "input/test2.txt, result/test2.txt",
            "input/test3.txt, result/test3.txt",
            "input/test4.txt, result/test4.txt",
            "input/test5.txt, result/test5.txt",
            "input/test6.txt, result/test6.txt"
    })
    @ParameterizedTest
    public void testPositiveCases(String inputFile, String outputFile) throws IOException {
        var classloader = AntlrCalculatorTest.class.getClassLoader();
        var uri = classloader.getResource(inputFile);
        var inputStream = CharStreams.fromStream(uri.openStream());
        var lexer = new ExprLexer(inputStream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new ExprParser(tokens);
        var tree = parser.prog();
        var exprTreeVisitor = new ExprToTreeVisitor();
        var treeInternal = exprTreeVisitor.visit(tree);
        var result = ((TreeWithAttributes) treeInternal).yield();
        var expected = Files.readString(Path.of(classloader.getResource(outputFile).getPath()));
        Truth.assertThat(result).isEqualTo(expected);
    }
}
