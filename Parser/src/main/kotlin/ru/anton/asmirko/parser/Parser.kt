package ru.anton.asmirko.parser

interface Parser<T> {
    fun parse(str: String): Tree<T>
}