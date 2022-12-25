package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

data class TreeImpl(
    override val value: Token,
    override val children: MutableList<Tree> = mutableListOf()
) : Tree