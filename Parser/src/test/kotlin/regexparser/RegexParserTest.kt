package regexparser

import com.google.common.truth.Truth
import org.junit.Assert
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import org.junit.jupiter.params.provider.ValueSource
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.tree.Tree

class RegexParserTest {

    private val tokens = mutableListOf<Token<*>>()
    private val lexer = RegexLexer(grammar)

    @BeforeEach
    internal fun setUp() {
        tokens.clear()
    }

    @ParameterizedTest
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
            "abcd(e)fghi, abcd(e)fghi"
        ],
    )
    fun `test positive cases`(expr: String, expected: String) {
        val parser = RegexParser(grammar, lexer)
        val expectedTokens = expected.split("")
            .filter { it != "" }
            .map { TerminalToken(it) }
        val tree = parser.parse(expr.split("").filter { it != "" }.toList())
        getTerminalTokens(tree)
        Truth.assertThat(tokens).isEqualTo(expectedTokens)
    }

    @ParameterizedTest
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
            "|a",
            "a**"
        ]
    )
    fun `test negative cases`(expr: String) {
        val parser = RegexParser(grammar, lexer)
        try {
            parser.parse(expr.split("").filter { it != "" }.toList())
            Assert.fail()
        } catch (e: Exception) {
            println(e.message)
        }
    }

    private fun getTerminalTokens(tree: Tree<*>) {
        if (tree.value is TerminalToken) {
            if (tree.value != grammar.epsilonToken) {
                tokens.add(tree.value)
            }
        } else {
            tree.children.forEach { getTerminalTokens(it) }
        }
    }

    companion object {
        val rules = mutableListOf(
            Rule(
                nonTerminal = NonTerminalToken("S"),
                rightSide = listOf(NonTerminalToken("Or"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("S"),
                rightSide = listOf(TerminalToken("ε"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("Or"),
                rightSide = listOf(
                    NonTerminalToken("And"),
                    NonTerminalToken("Or'")
                )
            ),
            Rule(
                nonTerminal = NonTerminalToken("Or'"),
                rightSide = listOf(TerminalToken("|"), NonTerminalToken("Or"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("Or'"),
                rightSide = listOf(TerminalToken("ε"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("And"),
                rightSide = listOf(NonTerminalToken("St"), NonTerminalToken("And'"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("And'"),
                rightSide = listOf(NonTerminalToken("And"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("And'"),
                rightSide = listOf(TerminalToken("ε"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("St"),
                rightSide = listOf(NonTerminalToken("C"), NonTerminalToken("St'"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("St'"),
                rightSide = listOf(TerminalToken("*"), NonTerminalToken("St"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("St'"),
                rightSide = listOf(TerminalToken("ε"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("C"),
                rightSide = listOf(TerminalToken("("), NonTerminalToken("Or"), TerminalToken(")"))
            ),
            Rule(
                nonTerminal = NonTerminalToken("C"),
                rightSide = listOf(TerminalToken("char"))
            )
        )
        val grammar = Grammar(
            rules = rules,
            nonTerminals = setOf(
                NonTerminalToken("S"),
                NonTerminalToken("Or"),
                NonTerminalToken("Or'"),
                NonTerminalToken("And"),
                NonTerminalToken("And'"),
                NonTerminalToken("St"),
                NonTerminalToken("St'"),
                NonTerminalToken("C")
            ),
            otherLattice = setOf("|", "*", "(", ")"),
            epsilonToken = EpsilonToken("ε"),
            bucksToken = BucksToken("$"),
            startNonTerminal = NonTerminalToken("S"),
            latticeSubstitute = mapOf(
                TerminalToken("char") to ('a' .. 'z').map { it.toString() }.toSet()
            )
        )
    }
}