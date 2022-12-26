package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

interface TreeWithAttributes {
    val code: ((MutableList<String>, String) -> String)

    fun yield(): String
}