package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar

class FirstBuilder(private val grammar: Grammar) {

    private val firstSets = mutableMapOf<Char, MutableSet<Char>>()

    fun buildFirstSets(): First {
        for (rule in grammar.rules) {
            firstOf(rule.nonTerminal)
        }
        if (firstSets.containsKey(EPSILON)) {
            firstSets.remove(EPSILON)
        }
        return firstSets
    }

    private fun firstOf(ch: Char): MutableSet<Char> {
        if (firstSets[ch] != null) {
            return firstSets[ch]!!
        }
        if (!ch.isUpperCase()) {
            val first = mutableSetOf(ch)
            firstSets[ch] = first
            return first
        }
        val first = grammar.rules.filter { it.nonTerminal == ch }
            .map {
                val f0 = firstOf(it.rightSide[0]).map { it }.toMutableSet()
                var i = 1
                while (f0.contains(EPSILON) && i < it.rightSide.length) {
                    f0.remove(EPSILON)
                    f0.addAll(firstOf(it.rightSide[i]))
                    i++
                }
                f0
            }
            .flatten()
            .toMutableSet()
        firstSets[ch] = first
        return first
    }

    companion object {
        const val EPSILON = 'ε'
    }
}

typealias First = Map<Char, Set<Char>>