package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.TerminalToken
import ru.anton.asmirko.grammar_utills.data.Token

class FirstBuilder<T>(private val grammar: Grammar<T>) {

    private val firstSets = mutableMapOf<Token<T>, MutableSet<Token<T>>>()

    fun buildFirstSets(): First<T> {
        for (rule in grammar.rules) {
            firstOf(rule.nonTerminal)
        }
        if (firstSets.containsKey(grammar.epsilonToken)) {
            firstSets.remove(grammar.epsilonToken)
        }
        return firstSets
    }

    private fun firstOf(token: Token<T>): MutableSet<Token<T>> {
        if (firstSets.containsKey(token)) {
            return firstSets[token]!!
        }
        return when (token) {
            is TerminalToken -> {
                val first: MutableSet<Token<T>> = mutableSetOf(token)
                firstSets[token] = first
                first
            }
            else -> {
                val first = grammar.rules.filter { it.nonTerminal == token }
                    .map {
                        val f0 = firstOf(it.rightSide[0]).map { it }.toMutableSet()
                        var i = 1
                        while (f0.contains(grammar.epsilonToken) && i < it.rightSide.size) {
                            f0.remove(grammar.epsilonToken)
                            f0.addAll(firstOf(it.rightSide[i]))
                            i++
                        }
                        f0
                    }
                    .flatten()
                    .toMutableSet()
                firstSets[token] = first
                first
            }
        }
    }
}

typealias First<T> = Map<Token<T>, MutableSet<Token<T>>>