package ru.anton.asmirko.grammar_utills

fun main() {
    val grammar = """
       S -> F
       S -> (S+F)
       F -> a
    """.trimIndent()
    val grammarResolver = GrammarResolver()
    val resolvedGrammar = grammarResolver.resolveGrammar(grammar)
    println(resolvedGrammar)
    val firstBuilder = FirstBuilder(resolvedGrammar)
    val firstSets = firstBuilder.buildFirstSets()
    println(firstSets)
    val followBuilder = FollowBuilder(resolvedGrammar)
    val follow = followBuilder.buildFollowSets()
    println(follow)
    val verifier = LL1GrammarVerifier(resolvedGrammar)
    println(verifier.isLL1())
}
