package ru.anton.asmirko.lexer

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.grammar.NonTerminalToken
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token
import ru.anton.asmirko.lexer.exception.LexerException

abstract class AbstractLexer<T>(private val grammar: Grammar<T>, override val eof: T) : Lexer<T> {
    private var curPos = -1
    private var curT: T? = null
    private var curToken: Token<T>? = null
    private var str: List<T>? = null

    abstract fun isBlank(t: T): Boolean

    private fun nextT() {
        curPos++
        if (curPos >= str!!.size) {
            throw LexerException("Read attempt after EOF")
        }
        curT = str!![curPos]
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
        this.str = mutableListOf<T>().apply {
            addAll(str)
            add(eof)
        }
        curPos = -1
        nextToken()
    }

    override fun curPos(): Int {
        return curPos
    }

    private fun getToken(chunk: T): Token<T> {
        return if (grammar.latticeSubstitute.values.any { chunk in it }
            || chunk == eof || chunk in grammar.otherLattice) {
            TerminalToken(chunk)
        } else if (chunk == grammar.epsilonToken.value) {
            grammar.epsilonToken
        } else if (chunk == grammar.bucksToken.value) {
            grammar.bucksToken
        } else {
            NonTerminalToken(chunk)
        }
    }
}