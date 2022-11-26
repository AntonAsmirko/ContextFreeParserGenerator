package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar


class FollowBuilder(private val grammar: Grammar) {

    private val firstBuilder = FirstBuilder(grammar)
    private val firstSets = firstBuilder.buildFirstSets()
    private val followSets = mutableMapOf<Char, MutableSet<Char>>()

    fun buildFollowSets(): Follow {
        for ((nonTerm, rule) in grammar.rules) {
            followOf(nonTerm)
        }
        return followSets
    }

    private fun followOf(ch: Char): MutableSet<Char> {
        if (followSets.containsKey(ch)) {
            return followSets[ch]!!
        }
        val follow = mutableSetOf<Char>()
        followSets[ch] = follow
        if (ch == grammar.rules[0].nonTerminal) {
            follow.add(BUCKS)
        }
        val productionsWithSymbol = getProductionsWithSymbol(ch)
        for ((nonTerm, rules) in productionsWithSymbol) {
            for (rule in rules) {
                val chIndex = rule.indexOf(ch)
                var followIndex = chIndex + 1
                while (true) {
                    if (followIndex == rule.length) {
                        if (nonTerm != ch) {
                            follow.addAll(followOf(nonTerm))
                        }
                        break
                    }
                    val followSymbol = rule[followIndex]
                    if (!followSymbol.isUpperCase() && !firstSets.containsKey(followSymbol)) {
                        follow.add(followSymbol)
                        break
                    }
                    val firstOfFollow = firstSets[followSymbol]!!.map { it }.toMutableSet()
                    if (!firstOfFollow.contains(FirstBuilder.EPSILON)) {
                        follow.addAll(firstOfFollow)
                        break
                    }
                    firstOfFollow.remove(FirstBuilder.EPSILON)
                    follow.addAll(firstOfFollow)
                    followIndex++
                }
            }
        }
        return follow
    }

    private fun getProductionsWithSymbol(ch: Char): MutableMap<Char, MutableList<String>> {
        val productionsWithSymbol = mutableMapOf<Char, MutableList<String>>()
        for ((nonTerm, rule) in grammar.rules) {
            if (rule.contains(ch)) {
                if (productionsWithSymbol[nonTerm] == null) {
                    productionsWithSymbol[nonTerm] = mutableListOf(rule)
                } else {
                    productionsWithSymbol[nonTerm]!!.add(rule)
                }
            }
        }
        return productionsWithSymbol
    }

    companion object {
        const val BUCKS = '$'
    }
}

typealias Follow = Map<Char, Set<Char>>