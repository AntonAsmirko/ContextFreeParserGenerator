package ru.anton.asmirko.grammar

data class Grammar(
    val rules: List<Rule>,
    val nonTerminals: Set<NonTerminalToken>,
    val otherLattice: Set<String>,
    val epsilonToken: EpsilonToken = EpsilonToken("ε"),
    val startNonTerminal: NonTerminalToken,
    val latticeSubstitute: Set<Regex>
)

data class Rule(val nonTerminal: NonTerminalToken, val rightSide: List<Token>, val action: String? = null)

sealed class Token(open var value: String) {
    override fun equals(other: Any?): Boolean {
        if (other is Token) {
            return (value == other.value)
        }
        return false
    }

    override fun hashCode(): Int {
        return value.hashCode()
    }

    override fun toString(): String {
        return value
    }
}

class NonTerminalToken(override var value: String) : Token(value)

open class TerminalToken(override var value: String) : Token(value)

class EpsilonToken(override var value: String) : TerminalToken(value)


