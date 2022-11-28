package lexer

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.grammar.NonTerminalToken
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token
import ru.anton.asmirko.lexer.Lexer

abstract class AbstractLexer<T>(private val grammar: Grammar<T>) : Lexer<T> {
    private var curPos = -1
    private var curT: T? = null
    private var curToken: Token<T>? = null
    private var str: List<T>? = null

    abstract fun isBlank(t: T): Boolean

    private fun nextT() {
        curPos++
        if (curPos < str!!.size) {
            curT = str!![curPos]
        }
    }

    final override fun nextToken() {
        nextT()
        while (isBlank(curT!!)) {
            nextT()
        }
        curToken = getToken(curT!!)
    }

    override fun curToken(): Token<T> {
        return curToken!!
    }

    override fun init(str: List<T>) {
        this.str = str
        curPos = -1
        nextT()
        curToken = getToken(curT!!)
    }

    override fun curPos(): Int {
        return curPos
    }

    private fun getToken(chunk: T): Token<T> {
        return when (chunk) {
            in grammar.aLattice -> TerminalToken(chunk)
            in grammar.otherLattice -> TerminalToken(chunk)
            grammar.epsilonToken -> grammar.epsilonToken
            grammar.bucksToken -> grammar.bucksToken
            else -> NonTerminalToken(chunk)
        }
    }
}