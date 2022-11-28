import ru.anton.asmirko.tree.Tree

interface Parser<T> {
    fun parse(str: List<T>): Tree<T>
}