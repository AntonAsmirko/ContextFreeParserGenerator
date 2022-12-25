import ru.anton.asmirko.tree.Tree

interface Parser {
    fun parse(str: List<String>): Tree
}