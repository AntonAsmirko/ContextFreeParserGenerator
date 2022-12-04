package ru.anton.asmirko.parser.lexer

import ru.anton.asmirko.grammar.Grammar
import ru.anton.asmirko.lexer.AbstractLexer

class RegexLexer(grammar: Grammar<String>) : AbstractLexer<String>(grammar, "EOF") {
    override fun isBlank(t: String): Boolean {
        return false
    }
}