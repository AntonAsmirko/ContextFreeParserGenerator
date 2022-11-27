package ru.anton.asmirko.parser

import ru.anton.asmirko.grammar_utills.GrammarResolver
import ru.anton.asmirko.grammar_utills.data.Grammar
import ru.anton.asmirko.grammar_utills.data.TerminalToken

val stack = mutableListOf<Char>()

fun main() {
    val grammar = """
       E -> TX
       X -> +TX
       X -> ε
       T -> FY
       Y -> *FY
       Y -> ε
       F -> a
       F -> (E)
    """.trimIndent()
    val grammarResolver = GrammarResolver()
    val resolvedGrammar = grammarResolver.resolveGrammar(
        grammar, nonTerminals = listOf('E', 'X', 'T', 'Y', 'F'),
        aLattice = ('0'..'9').toMutableSet(),
        otherLattice = setOf('*', '+', '(', ')'),
        epsilon = 'ε',
        bucks = '$',
        latticeSubstitute = 'a'
    )
    val parser = RegexParser(resolvedGrammar)
    val expr = "1+3*(2+1)"
    val result = parser.parse(expr)
    dfs(result, resolvedGrammar)
    println(stack.joinToString(separator = ""))
}

fun dfs(tree: Tree<Char>, grammar: Grammar<Char>) {
    if (tree.value is TerminalToken) {
        if (tree.value != grammar.epsilonToken)
            stack.add(tree.value.value)
    } else {
        for (child in tree.children) {
            dfs(child, grammar)
        }
    }
}