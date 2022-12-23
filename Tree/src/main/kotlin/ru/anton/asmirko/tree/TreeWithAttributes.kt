package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

interface TreeWithAttributes<T, R> : Tree<T> {
    val code: ((MutableList<Tree<T>>, Token<T>) -> R)

    fun yield(): R
}