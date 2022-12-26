package ru.anton.asmirko.lexer

import org.antlr.v4.runtime.CommonTokenStream
import ru.anton.asmirko.antlrmetagrammar.utills.MetaGrammarUtils
import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.grammar.Token
import ru.anton.asmirko.lexer.exception.LexerException

class TokenLexer(cts: CommonTokenStream, override val eof: String) : Lexer {

    private var curPos: Int = -1
    private lateinit var curT: String
    private lateinit var curToken: Token
    private lateinit var chunks: List<String>
    private val regexes = Regex(
        MetaGrammarUtils.getTokens(cts)
            .joinToString(separator = "|") { escaped(it) } + "|${escaped(eof)}"
    )

    private fun isBlank(t: String): Boolean {
        return t.matches(WHITESPACE) && t.isEmpty()
    }

    private fun nextT() {
        curPos++
        if (curPos >= chunks.size) {
            throw LexerException("Read attempt after EOF")
        }
        curT = chunks[curPos]
    }

    override fun nextToken() {
        nextT()
        while (isBlank(curT)) {
            nextT()
        }
        curToken = TerminalToken(curT)
    }

    override fun curToken(): Token {
        return curToken
    }

    override fun curPos(): Int {
        return curPos
    }

    override fun init(str: List<String>) {
        chunks = regexes.findAll(str[0] + eof).map { it.value }.toList()
        curPos = -1
        nextToken()
    }

    companion object {
        private val SPECIAL_CHARACTERS = setOf(
            ".", "^", "$", "*", "+", "-", "?", "(", ")",
            "[", "]", "{", "}", "\\", "|", "—", "/",
        )
        private val WHITESPACE = Regex("\\S+")

        private fun escaped(str: String) = if (str in SPECIAL_CHARACTERS) "\\$str" else str
    }
}