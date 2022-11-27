package ru.anton.asmirko.parser

class RegexTree(
    override val value: Char,
    override var isTerminal: Boolean = false,
    override val children: MutableList<Tree<Char>> = mutableListOf()
) : Tree<Char> {
}