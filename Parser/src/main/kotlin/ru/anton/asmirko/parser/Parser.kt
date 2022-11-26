package ru.anton.asmirko

interface Parser<T> {
    fun parse(str: String): Tree<T>
}