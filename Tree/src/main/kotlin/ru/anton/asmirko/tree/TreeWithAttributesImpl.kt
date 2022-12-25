package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

data class TreeWithAttributesImpl<R>(
    override val value: Token,
    override val children: MutableList<Tree>,
    override val code: (MutableList<Tree>, Token) -> R
) : TreeWithAttributes<R> {

    override fun yield(): R = code(children, value)
}