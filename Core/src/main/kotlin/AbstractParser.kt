import exception.EOFException
import exception.UnmatchedSymbolException
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.grammar_utills.FirstBuilder
import ru.anton.asmirko.grammar_utills.FollowBuilder
import ru.anton.asmirko.lexer.Lexer
import ru.anton.asmirko.tree.Tree
import ru.anton.asmirko.tree.TreeImpl

abstract class AbstractParser<T>(private val grammar: Grammar<T>, private val lexer: Lexer<T>) : Parser<T> {

    private val firstSets = FirstBuilder(grammar).buildFirstSets()
    private val followSets = FollowBuilder(grammar).buildFollowSets()
    private val mapWithRules: Map<NonTerminalToken<T>, () -> Tree<T>> = grammar.rules.map { it.nonTerminal }
        .distinct()
        .associateWith { makeRuleLambda(it) }

    override fun parse(str: List<T>): Tree<T> {
        lexer.init(str)
        val result = mapWithRules[grammar.startNonTerminal]!!()
        if (lexer.curToken().value != lexer.eof) {
            throw EOFException("EOF was not reached, last position: ${lexer.curPos()}")
        }
        return result
    }

    private fun makeRuleLambda(nonTerm: Token<T>): () -> Tree<T> {
        return {
            val rulesWithNonTerm = grammar.rules.filter { it.nonTerminal == nonTerm }
            val r = TreeImpl(value = nonTerm)
            for (rule in rulesWithNonTerm) {
                var wasEnter = false
                if (firstOne(rule, lexer.curToken())) {
                    for (index in rule.rightSide.indices) {
                        wasEnter = true
                        if (rule.rightSide[index] is TerminalToken) {
                            when {
                                rule.rightSide[index] == grammar.epsilonToken -> {
                                    r.children.add(TreeImpl(value = grammar.epsilonToken))
                                }
                                grammar.latticeSubstitute.map { Pair(it.key, it.value) }
                                    .firstOrNull { it.first == rule.rightSide[index] }
                                    ?.second
                                    ?.contains(lexer.curToken().value)
                                    ?.not()
                                    ?: false -> {
                                    throw UnmatchedSymbolException(
                                        msg = """Token at position ${lexer.curPos()} \"${lexer.curToken()}\" does not match symbol at grammar \"${rule.rightSide[index]}\
                                            which is lattice substitute an can have only this values [${grammar.latticeSubstitute[lexer.curToken()]!!.joinToString()}]
                                        """.trimMargin()
                                    )
                                }
                                grammar.latticeSubstitute.keys.none { it == rule.rightSide[index] }
                                        && lexer.curToken() != rule.rightSide[index] -> {
                                    throw UnmatchedSymbolException(
                                        msg = "Token at position ${lexer.curPos()} \"${lexer.curToken()}\" does not match symbol at grammar \"${rule.rightSide[index]}\""
                                    )
                                }
                                else -> {
                                    r.children.add(TreeImpl(value = lexer.curToken()))
                                    lexer.nextToken()
                                }
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
    }

    private fun firstOne(rule: Rule<T>, ch: Token<T>): Boolean {
        val firstCh = firstSets[rule.rightSide[0]]
            ?.map { it }
            ?.toMutableSet()
            ?: mutableSetOf(rule.rightSide[0])
        if (firstCh.contains(grammar.epsilonToken)) {
            firstCh.remove(grammar.epsilonToken)
            val followNonTerm = followSets[rule.nonTerminal]!!
            firstCh.addAll(followNonTerm)
        }
        val (tokenSub, options) = grammar.latticeSubstitute.filter { ch.value in it.value }
            .map { Pair(it.key, it.value) }
            .firstOrNull()
            ?: Pair(grammar.bucksToken, setOf())
        return if (ch.value in options) {
            firstCh.contains(tokenSub)
        } else {
            firstCh.contains(ch)
        }
    }
}