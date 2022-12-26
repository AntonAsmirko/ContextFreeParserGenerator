package ru.anton.asmirko.testutills

import ru.anton.asmirko.grammar.TerminalToken
import ru.anton.asmirko.tree.Tree
import java.net.URL
import java.nio.file.Files
import java.nio.file.Path

inline fun <reified T> resourceURLLoader(file: String): URL {
    val classloader: ClassLoader = T::class.java.classLoader
    return classloader.getResource(file)!!
}

fun readFromUrl(url: URL): String = Files.readString(Path.of(url.path))

fun getTerminalTokens(tree: Tree, tokens: MutableList<String>) {
    if (tree.value is TerminalToken) {
        if (tree.value.value != "ε") {
            tokens.add(tree.value.value)
        }
    } else {
        tree.children.forEach { getTerminalTokens(it, tokens) }
    }
}