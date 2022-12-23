package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

data class TreeWithAttributesImpl<T, R>(
    override val value: Token<T>,
    override val children: MutableList<Tree<T>>,
    override val code: (MutableList<Tree<T>>, Token<T>) -> R
) : TreeWithAttributes<T, R> {

    override fun yield(): R = code(children, value)
}