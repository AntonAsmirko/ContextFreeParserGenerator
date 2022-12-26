package customgrammar

import com.google.common.truth.Truth
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.anton.asmirko.lexer.TokenLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.parser.utils.initGrammarFromString
import ru.anton.asmirko.testutills.getTerminalTokens
import ru.anton.asmirko.testutills.readFromUrl
import ru.anton.asmirko.testutills.resourceURLLoader

class RegexTest {

    @CsvSource(
        value = [
            "abacaba, abacaba",
            "(a), (a)",
            "a, a",
            "a(b), a(b)",
            "(ab), (ab)",
            "a|(ab), a|(ab)",
            "a*, a*",
            "a, a",
            "a*|ab|(cd|ef), a*|ab|(cd|ef)",
            "a*|b*, a*|b*",
            "a*b*c*d, a*b*c*d",
            "a|b|c|d, a|b|c|d",
            "abcd*ef, abcd*ef",
            "abcd(e)fghi, abcd(e)fghi",
            "a**, a**"
        ]
    )
    @ParameterizedTest
    fun `test positive cases tokens`(input: String, expected: String) {
        val grammarRawUrl = resourceURLLoader<ArithmeticExprTest>("customgrammar/regex/regex.txt")
        val grammarRaw = readFromUrl(grammarRawUrl)
        val (grammar, tokens) = initGrammarFromString(grammarRaw)
        val lexer = TokenLexer(tokens, "$")
        val parser = RegexParser(grammar, lexer)
        val resultTree = parser.parse(listOf(input))
        val result = mutableListOf<String>()
        getTerminalTokens(resultTree, result)
        Truth.assertThat(result.joinToString(separator = "")).isEqualTo(expected)
    }

    @ValueSource(
        strings = [
            "*",
            "*a",
            "|",
            "a(",
            "a)",
            "(a",
            ")a",
            "(*)(*)",
            "(|)",
            "|a"
        ]
    )
    @ParameterizedTest
    fun `test negative cases`(input: String) {
        try {
            val grammarRawUrl = resourceURLLoader<ArithmeticLangTest>("customgrammar/regex/regex.txt")
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
}