package ru.anton.asmirko.lexer

import ru.anton.asmirko.grammar.Token

interface Lexer {
    val eof: String
    fun nextToken()
    fun curToken(): Token
    fun curPos(): Int
    fun init(str: List<String>)
}