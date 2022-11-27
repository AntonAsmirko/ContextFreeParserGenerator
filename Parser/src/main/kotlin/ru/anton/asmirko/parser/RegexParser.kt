package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar_utills.FirstBuilder
import ru.anton.asmirko.grammar_utills.FollowBuilder
import ru.anton.asmirko.grammar_utills.data.*
import ru.anton.asmirko.parser.lexer.RegexLexer

class RegexParser(private val grammar: Grammar<Char>) : Parser<Char> {

    private val firstSets = FirstBuilder(grammar).buildFirstSets()
    private val followSets = FollowBuilder(grammar).buildFollowSets()
    private lateinit var lexer: RegexLexer
    private val mapWithRules: Map<NonTerminalToken<Char>, () -> Tree<Char>> = grammar.rules.map { it.nonTerminal }
        .distinct()
        .associateWith { makeRuleLambda(it) }

    override fun parse(str: String): Tree<Char> {
        lexer = RegexLexer(str.toList(), grammar)
        return mapWithRules[grammar.startNonTerminal]!!()
    }

    private fun makeRuleLambda(nonTerm: Token<Char>): () -> Tree<Char> {
        val lambda = {
            val rulesWithNonTerm = grammar.rules.filter { it.nonTerminal == nonTerm }
            val r = RegexTree(value = nonTerm)
            for (rule in rulesWithNonTerm) {
                var wasEnter = false
                if (firstOne(rule, lexer.curToken())) {
                    for (index in rule.rightSide.indices) {
                        wasEnter = true
                        if (rule.rightSide[index] is TerminalToken) {
                            if (rule.rightSide[index] == grammar.epsilonToken) {
                                r.children.add(RegexTree(value = grammar.epsilonToken))
                            } else {
                                r.children.add(RegexTree(value = lexer.curToken()))
                                lexer.nextToken()
                            }
                        } else {
                            r.children.add(mapWithRules[rule.rightSide[index]]!!())
                        }
                    }
                }
                if (wasEnter) {
                    break
                }
            }
            r
        }
        return lambda
    }

    private fun firstOne(rule: Rule<Char>, ch: Token<Char>): Boolean {
        val firstCh = firstSets[rule.rightSide[0]]
            ?.map { it }
            ?.toMutableSet()
            ?: mutableSetOf(rule.rightSide[0])
        if (firstCh.contains(grammar.epsilonToken)) {
            firstCh.remove(grammar.epsilonToken)
            val followNonTerm = followSets[rule.nonTerminal]!!
            firstCh.addAll(followNonTerm)
        }
        return if (ch.value in grammar.aLattice) {
            firstCh.contains(grammar.latticeSubstitute)
        } else {
            return firstCh.contains(ch)
        }
    }
}