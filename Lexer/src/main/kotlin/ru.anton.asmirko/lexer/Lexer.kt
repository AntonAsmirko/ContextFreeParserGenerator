package ru.anton.asmirko.lexer

import ru.anton.asmirko.grammar.Token

interface Lexer<T> {
    val eof: T
    fun nextToken()
    fun curToken(): Token<T>
    fun curPos(): Int
    fun init(str: List<T>)
}