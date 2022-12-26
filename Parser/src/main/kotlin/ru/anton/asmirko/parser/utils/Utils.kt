package ru.anton.asmirko.parser.utils

import org.antlr.v4.runtime.CharStream
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarLexer
import ru.anton.asmirko.antlrmetagrammar.MetaGrammarParser
import ru.anton.asmirko.antlrmetagrammar.utills.MetaGrammarUtils
import ru.anton.asmirko.grammar.Grammar

fun initGrammarFromFile(grammarFile: String): Pair<Grammar, CommonTokenStream> {
    val inputStream = CharStreams.fromFileName(grammarFile)
    return createGrammar(inputStream)
}

fun initGrammarFromString(grammarRaw: String): Pair<Grammar, CommonTokenStream> {
    val inputStream = CharStreams.fromString(grammarRaw)
    return createGrammar(inputStream)
}

private fun createGrammar(cpcs: CharStream): Pair<Grammar, CommonTokenStream> {
    val lexer = MetaGrammarLexer(cpcs)
    val tokens = CommonTokenStream(lexer)
    val parser = MetaGrammarParser(tokens)
    val tree = parser.rules()
    return Pair(MetaGrammarUtils.treeToGrammar(tree, tokens), tokens)
}