package ru.anton.asmirko.graphviz

import guru.nidi.graphviz.engine.Format
import guru.nidi.graphviz.engine.Graphviz
import guru.nidi.graphviz.model.Factory.mutGraph
import guru.nidi.graphviz.model.Factory.mutNode
import guru.nidi.graphviz.model.MutableNode
import ru.anton.asmirko.tree.Tree
import guru.nidi.graphviz.attribute.Label
import java.io.File

class TreeDrawer {

    private var nodeCounter = 0

    fun drawTree(tree: Tree, name: String, outDir: String = "graphs") {
        nodeCounter = 0
        val graph = mutGraph(outDir).add(reformatTree(tree))
        Graphviz.fromGraph(graph).width(500).height(500).render(Format.PNG).toFile(File("$outDir/$name"))
    }

    private fun reformatTree(tree: Tree): MutableNode {
        val node = mutNode("${tree.value}_$nodeCounter").add(Label.lines(tree.value.toString()))
        nodeCounter++
        for (child in tree.children) {
            node.addLink(reformatTree(child))
        }
        return node
    }
}