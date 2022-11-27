package ru.anton.asmirko.grammar_utills.lexer

import ru.anton.asmirko.grammar_utills.data.Token

interface Lexer<T> {
    fun nextToken()
    fun curToken(): Token<T>
    fun curPos(): Int
}