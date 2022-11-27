package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar_utills.data.Token

class RegexTree(
    override val value: Token<Char>,
    override val children: MutableList<Tree<Char>> = mutableListOf()
) : Tree<Char>