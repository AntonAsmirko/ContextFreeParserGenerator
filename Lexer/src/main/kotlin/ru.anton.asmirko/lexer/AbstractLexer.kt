package ru.anton.asmirko.lexer

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.grammar.NonTerminalToken
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token
import ru.anton.asmirko.lexer.exception.LexerException

abstract class AbstractLexer(private val grammar: Grammar, override val eof: String) : Lexer {
    private var curPos = -1
    private var curT: String? = null
    private var curToken: Token? = null
    private var str: List<String>? = null

    abstract fun isBlank(t: String): Boolean

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

    override fun curToken(): Token {
        return curToken!!
    }

    override fun init(str: List<String>) {
        this.str = mutableListOf<String>().apply {
            addAll(str)
            add(eof)
        }
        curPos = -1
        nextToken()
    }

    override fun curPos(): Int {
        return curPos
    }

    private fun getToken(chunk: String): Token {
        return if (grammar.latticeSubstitute.any { it.matches(chunk) }
            || chunk == eof || chunk in grammar.otherLattice) {
            TerminalToken(chunk)
        } else if (chunk == grammar.epsilonToken.value) {
            grammar.epsilonToken
        } else if (chunk == "$") {
            TerminalToken("$")
        } else {
            NonTerminalToken(chunk)
        }
    }
}