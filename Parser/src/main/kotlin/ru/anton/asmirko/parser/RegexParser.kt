package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar_utills.FirstBuilder
import ru.anton.asmirko.grammar_utills.FollowBuilder
import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.Rule

class RegexParser(private val grammar: Grammar) : Parser<Char> {

    private val firstSets = FirstBuilder(grammar).buildFirstSets()
    private val followSets = FollowBuilder(grammar).buildFollowSets()
    private val mapWithRules: MutableMap<Char, () -> Tree<Char>>
    private val groupedRules: List<GroupedRule>
    private lateinit var str: String
    private var pos = 0
    private val terminals = ('0'..'9').toList()
    private var token = '%'

    init {
        localizeFirst()
        localizeFollow()
        groupedRules = makeGroupedRules()
        mapWithRules = createMapWithRules()
    }

    override fun parse(str: String): Tree<Char> {
        pos = 0
        this.str = str
        token = str[pos]
        return mapWithRules['E']!!()
    }

    private fun makeRuleLambda(nonTerm: Char): () -> Tree<Char> {
        val lambda = {
            val rulesWithNonTerm = groupedRules.filter { it.rule.nonTerminal == nonTerm }
            val r = RegexTree(value = nonTerm, isTerminal = false)
            if (pos >= str.length) {
                r.children.add(RegexTree(value = FirstBuilder.EPSILON, isTerminal = true))
                r
            } else {
                for (rule in rulesWithNonTerm) {
                    var wasEnter = false
                    if (firstOne(rule.rule, token)) {
                        for (index in rule.rule.rightSide.indices) {
                            wasEnter = true
                            if (!rule.rule.rightSide[index].isUpperCase()) {
                                if (rule.rule.rightSide[index] == FirstBuilder.EPSILON) {
                                    r.children.add(RegexTree(value = FirstBuilder.EPSILON, isTerminal = true))
                                } else {
                                    r.children.add(RegexTree(value = token, isTerminal = true))
                                    nextToken()
                                }
                            } else {
                                r.children.add(mapWithRules[rule.rule.rightSide[index]]!!())
                                //token = str[pos]
                            }
                        }
                    }
                    if (wasEnter) {
                        break
                    }
                }
                r
            }
        }
        return lambda
    }

    private fun createMapWithRules(): MutableMap<Char, () -> Tree<Char>> {
        val distinctNonTerms = grammar.rules.map { it.nonTerminal }.distinct()
        val result = mutableMapOf<Char, () -> Tree<Char>>()
        for (nonTerm in distinctNonTerms) {
            val lambdaRule = makeRuleLambda(nonTerm)
            result[nonTerm] = lambdaRule
        }
        return result
    }

    private fun nextToken() {
        pos++
        if (pos < str.length) {
            token = str[pos]
        }
    }

    private fun makeGroupedRules(): List<GroupedRule> {
        val distinctNonTerminals = grammar.rules.map { it.nonTerminal }.distinct()
        val result = mutableListOf<GroupedRule>()
        for (nonTerm in distinctNonTerminals) {
            val rulesWithNonTerm = grammar.rules.filter { it.nonTerminal == nonTerm }
            val groupedRules = rulesWithNonTerm.mapIndexed { index, rule ->
                GroupedRule(
                    distinctName = "${rule.nonTerminal}_$index",
                    rule = rule
                )
            }
            result.addAll(groupedRules)
        }
        return result
    }

    private fun firstOne(rule: Rule, ch: Char): Boolean {
        val firstCh = firstSets[rule.rightSide[0]]
            ?.map { it }
            ?.toMutableSet()
            ?: mutableSetOf(rule.rightSide[0])
        if (firstCh.contains(FirstBuilder.EPSILON)) {
            firstCh.remove(FirstBuilder.EPSILON)
            val followNonTerm = followSets[rule.nonTerminal]!!
            firstCh.addAll(followNonTerm)
        }
        return firstCh.contains(ch)
    }

    private fun localizeFirst() {
        for ((f, s) in firstSets) {
            if (s.contains('a')) {
                s.remove('a')
                s.addAll(terminals)
            }
        }
    }

    private fun localizeFollow() {
        for ((f, s) in followSets) {
            if (s.contains('a')) {
                s.remove('a')
                s.addAll(terminals)
            }
        }
    }

    data class GroupedRule(val distinctName: String, val rule: Rule)
}