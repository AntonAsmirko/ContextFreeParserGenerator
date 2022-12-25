package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

interface Tree {
    val children: MutableList<Tree>
    val `value`: Token
}