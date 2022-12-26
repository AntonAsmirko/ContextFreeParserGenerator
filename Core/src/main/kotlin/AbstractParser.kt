import compile.getCompiler
import compile.runScript
import exception.EOFException
import exception.UnmatchedSymbolException
import ru.anton.asmirko.grammar.*
import ru.anton.asmirko.grammar_utills.FirstBuilder
import ru.anton.asmirko.grammar_utills.FollowBuilder
import ru.anton.asmirko.lexer.Lexer
import ru.anton.asmirko.tree.Tree
import ru.anton.asmirko.tree.TreeImpl
import ru.anton.asmirko.tree.TreeWithAttributesImpl

abstract class AbstractParser(private val grammar: Grammar, private val lexer: Lexer) : Parser {

    private val firstSets = FirstBuilder(grammar).buildFirstSets()
    private val followSets = FollowBuilder(grammar).buildFollowSets()

    override fun parse(str: List<String>): Tree {
        lexer.init(str)
        COMPILER.eval("val state = mutableMapOf<String, String>()")
        val result = makeRuleLambda(grammar.startNonTerminal)()
        if (lexer.curToken().value != lexer.eof) {
            throw EOFException("EOF was not reached, last position: ${lexer.curPos()}")
        }
        return result
    }

    private fun makeRuleLambda(nonTerm: Token): () -> Tree {
        return {
            val rulesWithNonTerm = grammar.rules.filter { it.nonTerminal == nonTerm }
            var r = TreeImpl(value = nonTerm.clone())
            for (rule in rulesWithNonTerm) {
                var wasEnter = false
                if (firstOne(rule, lexer.curToken().clone())) {
                    for (index in rule.rightSide.indices) {
                        wasEnter = true
                        rule.action?.let {
                            val compiled = COMPILER.runScript<(List<String>, String) -> String>(it)
                            r = r.toTreeWithAttributes()
                            (r as TreeWithAttributesImpl).code = compiled
                        }
                        if (rule.rightSide[index] is TerminalToken) {
                            when {
                                rule.rightSide[index] == grammar.epsilonToken -> {
                                    r.children.add(TreeImpl(value = grammar.epsilonToken.clone()))
                                }
                                grammar.latticeSubstitute.firstOrNull { TerminalToken(it.pattern) == rule.rightSide[index] }
                                    ?.matches(lexer.curToken().value)
                                    ?.not()
                                    ?: false -> {
                                    throw UnmatchedSymbolException(
                                        msg = "Token at position ${lexer.curPos()} \"${lexer.curToken()}\" does not match symbol at grammar \"${rule.rightSide[index]}\""
                                    )
                                }
                                grammar.latticeSubstitute.none { TerminalToken("/${it.pattern}/") == rule.rightSide[index] }
                                        && lexer.curToken() != rule.rightSide[index]  -> {
                                    throw UnmatchedSymbolException(
                                        msg = "Token at position ${lexer.curPos()} \"${lexer.curToken()}\" does not match symbol at grammar \"${rule.rightSide[index]}\""
                                    )
                                }
                                else -> {
                                    if (grammar.latticeSubstitute.any { it.matches(lexer.curToken().value) }) {
                                        r.children.add(TreeWithAttributesImpl(value = lexer.curToken().clone()))
                                    } else {
                                        r.children.add(TreeImpl(value = lexer.curToken().clone()))
                                    }
                                    lexer.nextToken()
                                }
                            }
                        } else {
                            r.children.add(makeRuleLambda(rule.rightSide[index].clone())())
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

    private fun firstOne(rule: Rule, ch: Token): Boolean {
        val firstCh = firstSets[rule.rightSide[0]]
            ?.map { it }
            ?.toMutableSet()
            ?: mutableSetOf(rule.rightSide[0])
        if (firstCh.contains(grammar.epsilonToken)) {
            firstCh.remove(grammar.epsilonToken)
            val followNonTerm = followSets[rule.nonTerminal]!!
            firstCh.addAll(followNonTerm)
        }
        val options = grammar.latticeSubstitute.firstOrNull { it.matches(ch.value) }
            ?: REGEX_ALWAYS_FALSE
        return if (options.matches(ch.value)) {
            firstCh.contains(TerminalToken("/${options.pattern}/"))
        } else {
            firstCh.contains(ch)
        }
    }

    companion object {
        val REGEX_ALWAYS_FALSE = Regex("$.^")
        val COMPILER = getCompiler()
    }

    fun NonTerminalToken.clone(): NonTerminalToken = NonTerminalToken(this.value)
    private fun TerminalToken.clone(): TerminalToken = TerminalToken(this.value)
    fun Token.clone(): TerminalToken = TerminalToken(this.value)
}