package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar.*

fun main() {

    val rules = mutableListOf(
        Rule(
            nonTerminal = NonTerminalToken("S"),
            rightSide = listOf(NonTerminalToken("Or"))
        ),
        Rule(
            nonTerminal = NonTerminalToken("S"),
            rightSide = listOf(TerminalToken("^"), NonTerminalToken("Or"))
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
            rightSide = listOf(TerminalToken("!"), NonTerminalToken("St"))
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
        otherLattice = setOf("|", "*", "(", ")", "?", "+", "^"),
        epsilonToken = EpsilonToken("ε"),
        startNonTerminal = NonTerminalToken("S"),
        latticeSubstitute = setOf(
            Regex("[a-z]"), Regex("[?*+]")
        )
    )

    val firstBuilder = FirstBuilder(grammar)
    val firstSets = firstBuilder.buildFirstSets()
    println(firstSets)
    val followBuilder = FollowBuilder(grammar)
    val follow = followBuilder.buildFollowSets()
    println(follow)
}
