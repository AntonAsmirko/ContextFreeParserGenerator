package ru.anton.asmirko.parser.parser

import AbstractParser
import ru.anton.asmirko.lexer.Lexer
import ru.anton.asmirko.grammar.Grammar

class RegexParser(grammar: Grammar<Char>, lexer: Lexer<Char>) : AbstractParser<Char>(grammar, lexer)

class ArithmeticExprParser(grammar: Grammar<Char>, lexer: Lexer<Char>) : AbstractParser<Char>(grammar, lexer)