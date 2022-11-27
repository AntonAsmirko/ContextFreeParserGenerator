package ru.anton.asmirko.grammar_utills

import ru.anton.asmirko.grammar_utills.data.Grammar

//class LL1GrammarVerifier<T>(private val grammar: Grammar<T>) {
//    private val firstSets = FirstBuilder(grammar).buildFirstSets()
//    private val followSets = FollowBuilder(grammar).buildFollowSets()
//
//    fun isLL1(): Boolean {
//        return isRulesHold()
//    }
//
//    private fun isRulesHold(): Boolean {
//        val grouped = mutableMapOf<Char, MutableList<String>>()
//        for ((nonTerm, rule) in grammar.rules) {
//            if (grouped[nonTerm] != null) {
//                grouped[nonTerm]!!.add(rule)
//            } else {
//                grouped[nonTerm] = mutableListOf(rule)
//            }
//        }
//        for ((nonTerm, rules) in grouped) {
//            for (i in rules.indices) {
//                var j = i + 1
//                while (j < rules.size) {
//                    val alpha = rules[i]
//                    val beta = rules[j]
//                    val firstAlpha = firstSets[alpha[0]] ?: setOf(alpha[0])
//                    val firstBeta = firstSets[beta[0]] ?: setOf(beta[0])
//                    val intersection = HashSet(firstAlpha)
//                    intersection.retainAll(firstBeta)
//                    if (intersection.isNotEmpty()) {
//                        return false
//                    }
//                    if (firstAlpha.contains(FirstBuilder.EPSILON)) {
//                        val followNonTerm = followSets[nonTerm]!!
//                        val intersectionFollow = HashSet(firstBeta)
//                        intersectionFollow.retainAll(followNonTerm)
//                        if (intersectionFollow.isNotEmpty()) {
//                            return false
//                        }
//                    }
//                    j++
//                }
//            }
//        }
//        return true
//    }
//}