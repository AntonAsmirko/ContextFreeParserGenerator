package ru.anton.asmirko

interface Parser {
    fun <T> parse(str: String): Tree<T>
}