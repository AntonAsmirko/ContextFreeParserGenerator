package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.NonTerminalToken
import ru.anton.asmirko.grammar_utills.data.TerminalToken
import ru.anton.asmirko.grammar_utills.data.Token


class FollowBuilder<T>(private val grammar: Grammar<T>) {

    private val firstBuilder = FirstBuilder(grammar)
    private val firstSets = firstBuilder.buildFirstSets()
    private val followSets: MutableMap<Token<T>, MutableSet<Token<T>>> = mutableMapOf()

    fun buildFollowSets(): Follow<T> {
        for ((nonTerm, rule) in grammar.rules) {
            followOf(nonTerm)
        }
        return followSets
    }

    private fun followOf(nonTermToken: NonTerminalToken<T>): MutableSet<Token<T>> {
        if (followSets.containsKey(nonTermToken)) {
            return followSets[nonTermToken]!!
        }
        val follow = mutableSetOf<Token<T>>()
        followSets[nonTermToken] = follow
        if (nonTermToken == grammar.rules[0].nonTerminal) {
            follow.add(grammar.bucksToken)
        }
        val productionsWithSymbol = getProductionsWithSymbol(nonTermToken)
        for ((nonTerm, rules) in productionsWithSymbol) {
            for (rule in rules) {
                val nonTermTokenIndex = rule.indexOf(nonTermToken)
                var followIndex = nonTermTokenIndex + 1
                while (true) {
                    if (followIndex == rule.size) {
                        if (nonTerm != nonTermToken) {
                            follow.addAll(followOf(nonTerm))
                        }
                        break
                    }
                    val followSymbol = rule[followIndex]
                    if (followSymbol is TerminalToken && !firstSets.containsKey(followSymbol)) {
                        follow.add(followSymbol)
                        break
                    }
                    val firstOfFollow = firstSets[followSymbol]!!.map { it }.toMutableSet()
                    if (!firstOfFollow.contains(grammar.epsilonToken)) {
                        follow.addAll(firstOfFollow)
                        break
                    }
                    firstOfFollow.remove(grammar.epsilonToken)
                    follow.addAll(firstOfFollow)
                    followIndex++
                }
            }
        }
        return follow
    }

    private fun getProductionsWithSymbol(
        nonTermToken: NonTerminalToken<T>
    ): MutableMap<NonTerminalToken<T>, MutableList<List<Token<T>>>> {
        val productionsWithSymbol = mutableMapOf<NonTerminalToken<T>, MutableList<List<Token<T>>>>()
        for ((nonTerm, rule) in grammar.rules) {
            if (rule.contains(nonTermToken)) {
                if (!productionsWithSymbol.containsKey(nonTerm)) {
                    productionsWithSymbol[nonTerm] = mutableListOf(rule)
                } else {
                    productionsWithSymbol[nonTerm]!!.add(rule)
                }
            }
        }
        return productionsWithSymbol
    }
}

typealias Follow<T> = Map<Token<T>, MutableSet<Token<T>>>