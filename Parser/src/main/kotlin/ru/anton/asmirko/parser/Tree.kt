package ru.anton.asmirko

interface Tree<T> {
    val lChild: Tree<T>?
    val rChild: Tree<T>?
    val `value`: T
    var isTerminal: Boolean
}