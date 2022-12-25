package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

interface TreeWithAttributes<R> : Tree {
    val code: ((MutableList<Tree>, Token) -> R)

    fun yield(): R
}