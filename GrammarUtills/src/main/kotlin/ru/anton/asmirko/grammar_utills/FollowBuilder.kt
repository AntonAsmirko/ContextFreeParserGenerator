package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.grammar.NonTerminalToken
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token

class FollowBuilder(private val grammar: Grammar) {

    private val firstBuilder = FirstBuilder(grammar)
    private val firstSets = firstBuilder.buildFirstSets()
    private val followSets: MutableMap<Token, MutableSet<Token>> = mutableMapOf()

    fun buildFollowSets(): Follow {
        for ((nonTerm, rule) in grammar.rules) {
            followOf(nonTerm)
        }
        return followSets
    }

    private fun followOf(nonTermToken: NonTerminalToken): MutableSet<Token> {
        if (followSets.containsKey(nonTermToken)) {
            return followSets[nonTermToken]!!
        }
        val follow = mutableSetOf<Token>()
        followSets[nonTermToken] = follow
        if (nonTermToken == grammar.rules[0].nonTerminal) {
            follow.add(TerminalToken("$"))
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
        nonTermToken: NonTerminalToken
    ): MutableMap<NonTerminalToken, MutableList<List<Token>>> {
        val productionsWithSymbol = mutableMapOf<NonTerminalToken, MutableList<List<Token>>>()
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

typealias Follow = Map<Token, MutableSet<Token>>