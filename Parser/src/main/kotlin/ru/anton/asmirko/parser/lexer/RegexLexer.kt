package ru.anton.asmirko.parser.lexer

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.lexer.AbstractLexer

class RegexLexer(grammar: Grammar) : AbstractLexer(grammar, "EOF") {
    override fun isBlank(t: String): Boolean {
        return t.isBlank()
    }
}