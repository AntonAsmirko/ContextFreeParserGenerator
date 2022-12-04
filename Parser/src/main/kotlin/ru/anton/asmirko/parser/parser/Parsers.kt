package ru.anton.asmirko.parser.parser

import AbstractParser
import ru.anton.asmirko.lexer.Lexer
import ru.anton.asmirko.grammar.Grammar

/**
 *  +---+----------------------------------------------------------+
 *  |   |             ERE Precedence (from high to low)            |
 *  +---+----------------------------------------------------------+
 *  | 1 | Collation-related bracket symbols | [==] [::] [..]       |
 *  | 2 | Escaped characters                | \<special character> |
 *  | 3 | Bracket expression                | []                   |
 *  | 4 | Grouping                          | ()                   |
 *  | 5 | Single-character-ERE duplication  | * + ? {m,n}          |
 *  | 6 | Concatenation                     |                      |
 *  | 7 | Anchoring                         | ^ $                  |
 *  | 8 | Alternation                       | |                    |
 *  +---+-----------------------------------+----------------------+
 */
class RegexParser(grammar: Grammar<String>, lexer: Lexer<String>) : AbstractParser<String>(grammar, lexer)

class ArithmeticExprParser(grammar: Grammar<Char>, lexer: Lexer<Char>) : AbstractParser<Char>(grammar, lexer)