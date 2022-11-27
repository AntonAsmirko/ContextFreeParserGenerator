package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar_utills.data.Token

interface Tree<T> {
    val children: MutableList<Tree<T>>
    val `value`: Token<T>
}