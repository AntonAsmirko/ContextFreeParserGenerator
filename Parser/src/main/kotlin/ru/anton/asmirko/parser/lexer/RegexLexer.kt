package ru.anton.asmirko.parser.lexer

import lexer.AbstractLexer
import ru.anton.asmirko.grammar.Grammar

class RegexLexer(grammar: Grammar<Char>) : AbstractLexer<Char>(grammar) {

    override fun isBlank(t: Char): Boolean {
        return t.isWhitespace()
    }
}