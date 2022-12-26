package customgrammar

import com.google.common.truth.Truth
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.anton.asmirko.lexer.TokenLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.parser.utils.initGrammarFromString
import ru.anton.asmirko.testutills.getTerminalTokens
import ru.anton.asmirko.testutills.readFromUrl
import ru.anton.asmirko.testutills.resourceURLLoader
import ru.anton.asmirko.tree.TreeWithAttributes

class ArithmeticExprTest {

    @CsvSource(
        value = [
            "customgrammar/arithmeticexpr/input/test1.txt, customgrammar/arithmeticexpr/result/res1.txt",
            "customgrammar/arithmeticexpr/input/test2.txt, customgrammar/arithmeticexpr/result/res2.txt",
            "customgrammar/arithmeticexpr/input/test3.txt, customgrammar/arithmeticexpr/result/res3.txt",
            "customgrammar/arithmeticexpr/input/test4.txt, customgrammar/arithmeticexpr/result/res4.txt",
            "customgrammar/arithmeticexpr/input/test5.txt, customgrammar/arithmeticexpr/result/res5.txt",
            "customgrammar/arithmeticexpr/input/test6.txt, customgrammar/arithmeticexpr/result/res6.txt",
            "customgrammar/arithmeticexpr/input/test7.txt, customgrammar/arithmeticexpr/result/res7.txt",
            "customgrammar/arithmeticexpr/input/test8.txt, customgrammar/arithmeticexpr/result/res8.txt",
        ]
    )
    @ParameterizedTest
    fun `test positive cases tokens`(inputFile: String, expectedFile: String) {
        val grammarRawUrl = resourceURLLoader<ArithmeticExprTest>("customgrammar/arithmeticexpr/arithmeticexpr.txt")
        val grammarRaw = readFromUrl(grammarRawUrl)
        val (grammar, tokens) = initGrammarFromString(grammarRaw)
        val lexer = TokenLexer(tokens, "$")
        val parser = RegexParser(grammar, lexer)
        val inputUrl = resourceURLLoader<ArithmeticExprTest>(inputFile)
        val input = readFromUrl(inputUrl)
        val resultTree = parser.parse(listOf(input))
        val result = mutableListOf<String>()
        val expectedUrl = resourceURLLoader<ArithmeticExprTest>(expectedFile)
        val expected = readFromUrl(expectedUrl)
        getTerminalTokens(resultTree, result)
        Truth.assertThat(result.joinToString(separator = "")).isEqualTo(expected)
    }

    @ValueSource(
        strings = [
            "+ 1",
            "1+1)",
            "(1",
            "1 1",
            "+",
            "(1("
        ]
    )
    @ParameterizedTest
    fun `test negative cases`(input: String) {
        try {
            val grammarRawUrl = resourceURLLoader<ArithmeticExprTest>("customgrammar/arithmeticexpr/arithmeticexpr.txt")
            val grammarRaw = readFromUrl(grammarRawUrl)
            val (grammar, tokens) = initGrammarFromString(grammarRaw)
            val lexer = TokenLexer(tokens, "$")
            val parser = RegexParser(grammar, lexer)
            parser.parse(listOf(input))
            Assertions.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @CsvSource(
        value = [
            "customgrammar/arithmeticexpr/input/test1.txt, 10",
            "customgrammar/arithmeticexpr/input/test2.txt, 1",
            "customgrammar/arithmeticexpr/input/test3.txt, 1",
            "customgrammar/arithmeticexpr/input/test4.txt, 1",
            "customgrammar/arithmeticexpr/input/test5.txt, 1",
            "customgrammar/arithmeticexpr/input/test6.txt, 5",
            "customgrammar/arithmeticexpr/input/test7.txt, 15",
            "customgrammar/arithmeticexpr/input/test8.txt, 28",
        ]
    )
    @ParameterizedTest
    fun `evaluation test`(inputFile: String, expectedString: String) {
        val grammarRawUrl = resourceURLLoader<ArithmeticExprTest>("customgrammar/arithmeticexpr/arithmeticexpr.txt")
        val grammarRaw = readFromUrl(grammarRawUrl)
        val (grammar, tokens) = initGrammarFromString(grammarRaw)
        val lexer = TokenLexer(tokens, "$")
        val parser = RegexParser(grammar, lexer)
        val inputUrl = resourceURLLoader<ArithmeticExprTest>(inputFile)
        val input = readFromUrl(inputUrl)
        val resultTree = parser.parse(listOf(input))
        val result = (resultTree as TreeWithAttributes).yield().toDouble()
        Truth.assertThat(result).isEqualTo(expectedString.toDouble())
    }

}