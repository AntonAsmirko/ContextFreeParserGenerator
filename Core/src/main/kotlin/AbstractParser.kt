import ru.anton.asmirko.lexer.Lexer
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.grammar_utills.FirstBuilder
import ru.anton.asmirko.grammar_utills.FollowBuilder
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
        return mapWithRules[grammar.startNonTerminal]!!()
    }

    private fun makeRuleLambda(nonTerm: Token<T>): () -> Tree<T> {
        val lambda = {
            val rulesWithNonTerm = grammar.rules.filter { it.nonTerminal == nonTerm }
            val r = TreeImpl(value = nonTerm)
            for (rule in rulesWithNonTerm) {
                var wasEnter = false
                if (firstOne(rule, lexer.curToken())) {
                    for (index in rule.rightSide.indices) {
                        wasEnter = true
                        if (rule.rightSide[index] is TerminalToken) {
                            if (rule.rightSide[index] == grammar.epsilonToken) {
                                r.children.add(TreeImpl(value = grammar.epsilonToken))
                            } else {
                                r.children.add(TreeImpl(value = lexer.curToken()))
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
        return if (ch.value in grammar.aLattice) {
            firstCh.contains(grammar.latticeSubstitute)
        } else {
            return firstCh.contains(ch)
        }
    }
}