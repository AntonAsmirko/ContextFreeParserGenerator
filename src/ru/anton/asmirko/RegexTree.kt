package ru.anton.asmirko

class RegexTree(
    override val lChild: Tree<String>?,
    override val rChild: Tree<String>?,
    override val value: String,
    override var isTerminal: Boolean
) : Tree<String> {
}