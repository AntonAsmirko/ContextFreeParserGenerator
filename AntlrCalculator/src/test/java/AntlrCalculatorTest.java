import com.google.common.truth.Truth;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import ru.anton.asmirko.antlrcalculator.ExprLexer;
import ru.anton.asmirko.antlrcalculator.ExprToTreeVisitor;
import ru.anton.asmirko.antlrcalculator.error.CalcParserException;
import ru.anton.asmirko.antlrcalculator.parser.ExprParserWithErrors;
import ru.anton.asmirko.tree.TreeWithAttributes;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class AntlrCalculatorTest {

    @CsvSource(value = {
            "positive/input/test1.txt, positive/result/test1.txt",
            "positive/input/test2.txt, positive/result/test2.txt",
            "positive/input/test3.txt, positive/result/test3.txt",
            "positive/input/test4.txt, positive/result/test4.txt",
            "positive/input/test5.txt, positive/result/test5.txt",
            "positive/input/test6.txt, positive/result/test6.txt"
    })
    @ParameterizedTest
    public void testPositiveCases(String inputFile, String outputFile) throws IOException {
        var result = testInternal(inputFile);
        var expected = Files.readString(Path.of(resourceURLLoader(outputFile).getPath()));
        Truth.assertThat(result).isEqualTo(expected);
    }

    @ValueSource(strings = {
            "negative/input/test1.txt",
            "negative/input/test2.txt",
    })
    @ParameterizedTest
    public void testNegativeCases(String inputFile) throws IOException {
        try {
            testInternal(inputFile);
            Assertions.fail();
        } catch (CalcParserException e) {
            System.out.println(e.getMessage());
        }
    }

    private URL resourceURLLoader(String file) {
        var classloader = AntlrCalculatorTest.class.getClassLoader();
        return classloader.getResource(file);
    }

    private String testInternal(String inputFile) throws IOException {
        var uri = resourceURLLoader(inputFile);
        var inputStream = CharStreams.fromStream(uri.openStream());
        var lexer = new ExprLexer(inputStream);
        var tokens = new CommonTokenStream(lexer);
        var parser = new ExprParserWithErrors(tokens);
        var tree = parser.prog();
        var exprTreeVisitor = new ExprToTreeVisitor();
        var treeInternal = exprTreeVisitor.visit(tree);
        return ((TreeWithAttributes<String, String>) treeInternal).yield();
    }
}
