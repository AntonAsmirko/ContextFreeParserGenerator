package ru.anton.asmirko.parser

import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarLexer
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarParser
import ru.anton.asmirko.antlrmetagrammar.utills.MetaGrammarUtils
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.graphviz.TreeDrawer
import ru.anton.asmirko.lexer.TokenLexer
import ru.anton.asmirko.parser.lexer.RegexLexer
import ru.anton.asmirko.parser.parser.RegexParser
import ru.anton.asmirko.tree.Tree
import ru.anton.asmirko.tree.TreeWithAttributes

fun main(args: Array<String>) {
    runArithmeticLangParser(args[0])
}

fun initGrammarFromFile(grammarFile: String): Pair<Grammar, CommonTokenStream> {
    val inputStream = CharStreams.fromFileName(grammarFile)
    val lexer = MetaGrammarLexer(inputStream)
    val tokens = CommonTokenStream(lexer)
    val parser = MetaGrammarParser(tokens)
    val tree = parser.rules()
    return Pair(MetaGrammarUtils.treeToGrammar(tree, tokens), tokens)
}

fun runRegexParserFromGrammar(grammarFile: String) {
    val (grammar, tokens) = initGrammarFromFile(grammarFile)
    val lexer1 = TokenLexer(tokens, "$")
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
        val result = parser1.parse(listOf(item))
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/regex")
    }
}

fun runArithmeticLangParser(grammarFile: String) {
    val (grammar, tokens) = initGrammarFromFile(grammarFile)
    val lexer1 = TokenLexer(tokens, "$")
    val parser1 = RegexParser(grammar, lexer1)
    val toPlot = listOf(
        """a = 10;
           b = 15;
           c = a + b + 1;"""
            .trimMargin(),
    )
    for (item in toPlot) {
        val result = parser1.parse(listOf(item))
        val res = (result as TreeWithAttributes).yield()
        val treeDrawer = TreeDrawer()
        treeDrawer.drawTree(result, item, "graphs/arithmetics")
    }
}

fun runArithmeticParserFromGrammar(grammarFile: String) {
    val (grammar, tokens) = initGrammarFromFile(grammarFile)
    val lexer1 = TokenLexer(tokens, "$")
    val parser1 = RegexParser(grammar, lexer1)
    val toPlot = listOf(
        "1 + 3 * ( 2 +    1 )",
        "1",
        "(1)",
        "( (    ((1)) ) )",
        "1 * (2 + 3)",
        "1 + 2 + 3 + 4 + 5",
        "(1 + 2 + 4) * 4",
        "124 + 3000 * 10",
        "1 / 1 - 1 * (-10 + 10) + 10"
    )
    for (item in toPlot) {
        val result = parser1.parse(listOf(item))
        val res = (result as TreeWithAttributes).yield()
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