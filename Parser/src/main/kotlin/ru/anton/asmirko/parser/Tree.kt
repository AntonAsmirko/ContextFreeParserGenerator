package ru.anton.asmirko.parser

interface Tree<T> {
    val children: MutableList<Tree<T>>
    val `value`: T
    var isTerminal: Boolean
}