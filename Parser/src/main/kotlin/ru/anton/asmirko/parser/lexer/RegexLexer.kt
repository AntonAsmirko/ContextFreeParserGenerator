package ru.anton.asmirko.parser.lexer

import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.lexer.AbstractLexer

class RegexLexer(str: List<Char>, grammar: Grammar<Char>) : AbstractLexer<Char>(str, grammar) {

    override fun isBlank(t: Char): Boolean {
        return t.isWhitespace()
    }
}