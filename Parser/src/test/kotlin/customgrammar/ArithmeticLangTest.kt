package customgrammar

import com.google.common.truth.Truth
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.anton.asmirko.lexer.TokenLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.parser.utils.initGrammarFromString
import ru.anton.asmirko.testutills.readFromUrl
import ru.anton.asmirko.testutills.resourceURLLoader
import ru.anton.asmirko.tree.TreeWithAttributes

class ArithmeticLangTest {

    @CsvSource(
        value = [
            "customgrammar/arithmeticlang/input/test1.txt, customgrammar/arithmeticlang/result/test1.txt",
            "customgrammar/arithmeticlang/input/test2.txt, customgrammar/arithmeticlang/result/test2.txt",
            "customgrammar/arithmeticlang/input/test3.txt, customgrammar/arithmeticlang/result/test3.txt",
            "customgrammar/arithmeticlang/input/test4.txt, customgrammar/arithmeticlang/result/test4.txt",
            "customgrammar/arithmeticlang/input/test5.txt, customgrammar/arithmeticlang/result/test5.txt",
            "customgrammar/arithmeticlang/input/test6.txt, customgrammar/arithmeticlang/result/test6.txt",
        ]
    )
    @ParameterizedTest
    fun `evaluation test`(inputFile: String, expectedFile: String) {
        val grammarRawUrl = resourceURLLoader<ArithmeticLangTest>("customgrammar/arithmeticlang/arithmeticlang.txt")
        val grammarRaw = readFromUrl(grammarRawUrl)
        val (grammar, tokens) = initGrammarFromString(grammarRaw)
        val lexer = TokenLexer(tokens, "$")
        val parser = RegexParser(grammar, lexer)
        val inputUrl = resourceURLLoader<ArithmeticLangTest>(inputFile)
        val input = readFromUrl(inputUrl)
        val resultTree = parser.parse(listOf(input))
        val result = (resultTree as TreeWithAttributes).yield()
        val expectedUrl = resourceURLLoader<ArithmeticLangTest>(expectedFile)
        val expected = readFromUrl(expectedUrl)
        Truth.assertThat(result).isEqualTo(expected)
    }

    @ValueSource(
        strings = [
            "customgrammar/arithmeticlang/negative/input/test1.txt",
            "customgrammar/arithmeticlang/negative/input/test2.txt"
        ]
    )
    @ParameterizedTest
    fun `test negative cases`(inputFile: String) {
        try {
            val grammarRawUrl = resourceURLLoader<ArithmeticLangTest>("customgrammar/arithmeticlang/arithmeticlang.txt")
            val grammarRaw = readFromUrl(grammarRawUrl)
            val inputUrl = resourceURLLoader<ArithmeticLangTest>(inputFile)
            val input = readFromUrl(inputUrl)
            val (grammar, tokens) = initGrammarFromString(grammarRaw)
            val lexer = TokenLexer(tokens, "$")
            val parser = RegexParser(grammar, lexer)
            parser.parse(listOf(input))
            Assertions.fail()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}