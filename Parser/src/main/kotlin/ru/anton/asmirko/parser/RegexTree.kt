package ru.anton.asmirko

class RegexTree(
    override val lChild: Tree<Char>?,
    override val rChild: Tree<Char>?,
    override val value: Char,
    override var isTerminal: Boolean
) : Tree<Char> {
}