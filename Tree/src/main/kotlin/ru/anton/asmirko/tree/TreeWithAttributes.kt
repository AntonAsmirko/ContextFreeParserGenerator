package ru.anton.asmirko.tree

interface TreeWithAttributes {
    val code: ((MutableList<String>, String) -> String)

    fun yield(): String
}