package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

interface Tree<T> {
    val children: MutableList<Tree<T>>
    val `value`: Token<T>
}