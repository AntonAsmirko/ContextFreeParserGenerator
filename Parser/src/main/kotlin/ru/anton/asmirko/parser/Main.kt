package ru.anton.asmirko.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarLexer
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarParser
import ru.anton.asmirko.antlrmetagrammar.utills.MetaGrammarUtils
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.graphviz.TreeDrawer
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser

fun main(args: Array<String>) {
    runRegexParserFromGrammar(args[0])
}

fun initGrammarFromFile(grammarFile: String): Grammar {
    val inputStream = CharStreams.fromFileName(grammarFile)
    val lexer = MetaGrammarLexer(inputStream)
    val tokens = CommonTokenStream(lexer)
    val parser = MetaGrammarParser(tokens)
    val tree = parser.rules()
    return MetaGrammarUtils.treeToGrammar(tree, tokens)
}

fun runRegexParserFromGrammar(grammarFile: String) {
    val grammar = initGrammarFromFile(grammarFile)
    val lexer1 = RegexLexer(grammar)
    val parser1 = RegexParser(grammar, lexer1)
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
        val result = parser1.parse(item.toList().map { it.toString() })
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/regex")
    }
}

fun runArithmeticParserFromGrammar(grammarFile: String) {
    val grammar = initGrammarFromFile(grammarFile)
    val lexer1 = RegexLexer(grammar)
    val parser1 = RegexParser(grammar, lexer1)
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
        val result = parser1.parse(item.split("".toRegex()))
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/arithmetics")
    }
}

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