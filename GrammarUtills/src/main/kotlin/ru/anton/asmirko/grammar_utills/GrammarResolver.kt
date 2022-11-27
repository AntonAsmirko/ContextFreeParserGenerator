package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.*

class GrammarResolver {

    fun resolveGrammar(
        grammar: String,
        nonTerminals: List<Char>,
        aLattice: Set<Char>,
        otherLattice: Set<Char>,
        epsilon: Char,
        bucks: Char,
        latticeSubstitute: Char
    ): Grammar<Char> {
        val rules = grammar.split("\n")
            .map { strRule ->
                val splited = strRule.split("->")
                val rawLSide = splited[0].trim()
                val rawRSide = splited[1].trim()
                if (nonTerminals.find { it == rawLSide[0] } == null) {
                    throw IllegalStateException("Only upper case letters supported for names of rules")
                }
                if (rawRSide.isEmpty()) {
                    throw IllegalStateException("Right side of rule cannot be empty")
                }
                Rule(NonTerminalToken(rawLSide[0]), rawRSide.toList().map {
                    if (it in nonTerminals) {
                        NonTerminalToken(it)
                    } else {
                        TerminalToken(it)
                    }
                })
            }
        return Grammar(
            rules = rules,
            nonTerminals = nonTerminals.map { NonTerminalToken(it) }.toSet(),
            aLattice = aLattice,
            otherLattice = otherLattice,
            epsilonToken = EpsilonToken(epsilon),
            bucksToken = BucksToken(bucks),
            startNonTerminal = rules.first().nonTerminal,
            latticeSubstitute = TerminalToken(latticeSubstitute)
        )
    }
}

//'ε'