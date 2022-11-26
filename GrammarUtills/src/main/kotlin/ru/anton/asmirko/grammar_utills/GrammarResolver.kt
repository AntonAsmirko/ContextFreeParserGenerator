package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.Rule

class GrammarResolver {

    fun resolveGrammar(grammar: String): Grammar {
        return Grammar(
            grammar.split("\n")
                .map { strRule ->
                    val splited = strRule.split("->")
                    val rawLSide = splited[0].trim()
                    val rawRSide = splited[1].trim()
                    if (rawLSide.length != 1 || !rawLSide[0].isLetter() || !rawLSide[0].isUpperCase()) {
                        throw IllegalStateException("Only upper case letters supported for names of rules")
                    }
                    if (rawRSide.isEmpty()) {
                        throw IllegalStateException("Right side of rule cannot be empty")
                    }
                    Rule(rawLSide[0], rawRSide)
                }
        )
    }
}