package ru.anton.asmirko.tree

import ru.anton.asmirko.grammar.Token

class TreeWithAttributesImpl(
    override val value: Token,
    override val children: MutableList<Tree> = mutableListOf(),
    override var code: (List<String>, String) -> String = { _, v -> v }
) : TreeWithAttributes, TreeImpl(value, children) {

    override fun yield(): String {
        val res = code(
            children
                .filterIsInstance<TreeWithAttributes>()
                .map { it.yield() },
            value.value
        )
        return res
    }
}