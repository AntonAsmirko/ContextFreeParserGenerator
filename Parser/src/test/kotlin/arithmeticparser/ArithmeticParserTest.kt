package arithmeticparser

import com.google.common.truth.Truth
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token
import ru.anton.asmirko.parser.GrammarResolver
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.tree.Tree

class ArithmeticParserTest {

    private val tokens = mutableListOf<Token<*>>()
    private val grammar = """
                E -> TX
                X -> +TX
                X -> ε
                T -> FY
                Y -> *FY
                Y -> ε
                F -> a
                F -> (E)
    """.trimIndent()
    private val grammarResolver = GrammarResolver()
    private val resolvedGrammar = grammarResolver.resolveGrammar(
        grammar, nonTerminals = listOf('E', 'X', 'T', 'Y', 'F'),
        aLattice = ('0'..'9').toMutableSet(),
        otherLattice = setOf('*', '+', '(', ')'),
        epsilon = 'ε',
        bucks = '$',
        latticeSubstitute = 'a'
    )
    private val lexer = RegexLexer(resolvedGrammar)

    @BeforeEach
    internal fun setUp() {
        tokens.clear()
    }

    @ParameterizedTest
    @CsvSource(
        value = [
            "1 + 3 * ( 2 +    1 ), 1+3*(2+1)",
            "1, 1",
            "(1), (1)",
            "    1    , 1",
            "( (    ((1)) ) ), ((((1))))",
            "1 * (2 + 3), 1*(2+3)",
            "1 + 2 + 3 + 4 + 5, 1+2+3+4+5",
            "(1 + 2 + 4) * 4, (1+2+4)*4"
        ]
    )
    fun test1(expr: String, expected: String) {
        val parser = RegexParser(resolvedGrammar, lexer)
        val expectedTokens = expected.map { TerminalToken(it) }
        val tree = parser.parse(expr.toList())
        getTerminalTokens(tree)
        Truth.assertThat(tokens).isEqualTo(expectedTokens)

    }

    private fun getTerminalTokens(tree: Tree<*>) {
        if (tree.value is TerminalToken) {
            if (tree.value.value != 'ε') {
                tokens.add(tree.value)
            }
        } else {
            tree.children.forEach { getTerminalTokens(it) }
        }
    }
}