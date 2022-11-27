package ru.anton.asmirko.grammar_utills.lexer

import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.NonTerminalToken
import ru.anton.asmirko.grammar_utills.data.TerminalToken
import ru.anton.asmirko.grammar_utills.data.Token

abstract class AbstractLexer<T>(private val str: List<T>, private val grammar: Grammar<T>) : Lexer<T> {
    private var curPos = 0
    private var curT: T
    private var curToken: Token<T>

    init {
        curPos = 0
        curT = str.first()
        curToken = getToken(curT)
    }

    abstract fun isBlank(t: T): Boolean

    private fun nextT() {
        curPos++
        if (curPos < str.size) {
            curT = str[curPos]
        }
    }

    final override fun nextToken() {
        while (isBlank(curT)) {
            nextT()
        }
        nextT()
        curToken = getToken(curT)
    }

    override fun curToken(): Token<T> {
        return curToken!!
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