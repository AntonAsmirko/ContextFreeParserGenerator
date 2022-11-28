package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

data class TreeImpl<T>(
    override val value: Token<T>,
    override val children: MutableList<Tree<T>> = mutableListOf()
) : Tree<T>