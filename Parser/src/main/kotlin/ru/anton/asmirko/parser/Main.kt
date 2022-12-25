package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.graphviz.TreeDrawer
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser

fun main() {
    runRegexParser()
}

//fun runArithmeticsParser() {
//    val grammar = """
//       E -> TX
//       X -> +TX
//       X -> ε
//       T -> FY
//       Y -> *FY
//       Y -> ε
//       F -> a
//       F -> (E)
//    """.trimIndent()
//    val grammarResolver = GrammarResolver()
//    val resolvedGrammar = grammarResolver.resolveGrammar(
//        grammar, nonTerminals = listOf("E", "X", "T", "Y", "F"),
//        otherLattice = setOf('*', '+', '(', ')'),
//        epsilon = "ε",
//        bucks = '$',
//        latticeSubstitute = mapOf(
//            TerminalToken("a") to ('0' .. '9').toSet()
//        )
//    )
//    val lexer = ArithmeticsLexer(resolvedGrammar)
//    val parser = ArithmeticExprParser(resolvedGrammar, lexer)
//    val toPlot = listOf(
//        "1 + 3 * ( 2 +    1 )",
//        "1",
//        "(1)",
//        "( (    ((1)) ) )",
//        "1 * (2 + 3)",
//        "1 + 2 + 3 + 4 + 5",
//        "(1 + 2 + 4) * 4"
//    )
//    for (item in toPlot) {
//        val result = parser.parse(item.toList())
//        val treeDrawer = TreeDrawer()
//        treeDrawer.drawTree(result, item, "graphs/arithmetics")
//    }
//}

/**
 *  S -> Or
 *  S -> eps
 *
 *  Or -> AndOr'
 *  Or' -> |Or
 *  Or' -> eps
 *
 *  And -> StAnd'
 *  And' -> And
 *  And -> eps
 *
 *  St -> CSt'
 *  St' -> *St'
 *  St' -> eps
 *
 *  C -> (Or)
 *  C -> char
 */
fun runRegexParser() {
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
            rightSide = listOf(TerminalToken("*"), NonTerminalToken("St'"))
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
        otherLattice = setOf("|", "*"),
        epsilonToken = EpsilonToken("ε"),
        startNonTerminal = NonTerminalToken("S"),
        latticeSubstitute = setOf(Regex("[a-z]"))
    )
    val lexer = RegexLexer(grammar)
    val parser = RegexParser(grammar, lexer)
    val toPlot = listOf(
        "ab|cd",
        "(abc)*|d",
        "(a)*bc|de|f*",
        "a",
        "abc",
        "a*",
        "(x|y|z*abc)",
        "(a)"
    )
    for (item in toPlot) {
        val result = parser.parse(item.toList().map { it.toString() })
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/regex")
    }
}