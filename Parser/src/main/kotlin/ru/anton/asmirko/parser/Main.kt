package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.graphviz.TreeDrawer
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser

fun main() {
    runArithmeticsParser()
}

fun runArithmeticsParser() {
    val grammar = """
       E -> TX
       X -> +TX
       X -> ε
       T -> FY
       Y -> *FY
       Y -> ε
       F -> a
       F -> (E)
    """.trimIndent()
    val grammarResolver = GrammarResolver()
    val resolvedGrammar = grammarResolver.resolveGrammar(
        grammar, nonTerminals = listOf('E', 'X', 'T', 'Y', 'F'),
        aLattice = ('0'..'9').toMutableSet(),
        otherLattice = setOf('*', '+', '(', ')'),
        epsilon = 'ε',
        bucks = '$',
        latticeSubstitute = 'a'
    )
    val lexer = RegexLexer(resolvedGrammar)
    val parser = RegexParser(resolvedGrammar, lexer)
    val toPlot = listOf(
        "1 + 3 * ( 2 +    1 )",
        "1",
        "(1)",
        "( (    ((1)) ) )",
        "1 * (2 + 3)",
        "1 + 2 + 3 + 4 + 5",
        "(1 + 2 + 4) * 4"
    )
    for (item in toPlot) {
        val result = parser.parse(item.toList())
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/arithmetics")
    }

    /**
    E -> O
    E -> ε
    O -> CX
    X -> |O
    X -> ε
    C -> SA
    A -> C
    A -> ε
    S -> TA

    E2 abc|d
    ---------------
    T
     */
    fun runRegexParser() {
        val grammar = Grammar<String>(
            rules = mutableListOf(
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
                    rightSide = listOf(TerminalToken("*"), NonTerminalToken("St'"))
                ),
                Rule(
                    nonTerminal = NonTerminalToken("St'"),
                    rightSide = listOf(NonTerminalToken("ε"))
                ),
                Rule(
                    nonTerminal = NonTerminalToken("C"),
                    rightSide = listOf(TerminalToken("("), NonTerminalToken("Or"), TerminalToken(")"))
                ),
                Rule(
                    nonTerminal = NonTerminalToken("C"),
                    rightSide = listOf(TerminalToken("char"))
                )
            ),
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
            aLati
        )
    }
}