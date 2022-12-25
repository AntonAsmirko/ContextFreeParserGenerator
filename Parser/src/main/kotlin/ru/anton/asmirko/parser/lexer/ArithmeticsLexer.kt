package ru.anton.asmirko.parser.lexer

import ru.anton.asmirko.lexer.AbstractLexer
import ru.anton.asmirko.grammar.Grammar

class ArithmeticsLexer(grammar: Grammar) : AbstractLexer(grammar, "$") {

    override fun isBlank(t: String): Boolean {
        return false
    }
}