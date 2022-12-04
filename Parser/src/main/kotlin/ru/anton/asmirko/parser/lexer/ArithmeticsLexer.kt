package ru.anton.asmirko.parser.lexer

import ru.anton.asmirko.lexer.AbstractLexer
import ru.anton.asmirko.grammar.Grammar

class ArithmeticsLexer(grammar: Grammar<Char>) : AbstractLexer<Char>(grammar, '$') {

    override fun isBlank(t: Char): Boolean {
        return t.isWhitespace()
    }
}