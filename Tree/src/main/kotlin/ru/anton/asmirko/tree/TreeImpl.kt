package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

open class TreeImpl(
    override val value: Token,
    override val children: MutableList<Tree> = mutableListOf()
) : Tree {
    fun toTreeWithAttributes(): TreeWithAttributesImpl = TreeWithAttributesImpl(
        value = this.value,
        children = this.children
    )
}