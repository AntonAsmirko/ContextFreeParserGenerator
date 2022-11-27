package ru.anton.asmirko.grammar_utills

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
        grammar = grammar,
        nonTerminals = listOf('E', 'X', 'T', 'Y', 'F'),
        aLattice = ('0'..'9').toSet(),
        otherLattice = setOf('*', '+', ')', '('),
        epsilon = 'ε',
        bucks = '$',
        latticeSubstitute = 'a'
    )
    println(resolvedGrammar)
    val firstBuilder = FirstBuilder(resolvedGrammar)
    val firstSets = firstBuilder.buildFirstSets()
    println(firstSets)
    val followBuilder = FollowBuilder(resolvedGrammar)
    val follow = followBuilder.buildFollowSets()
    println(follow)
}
