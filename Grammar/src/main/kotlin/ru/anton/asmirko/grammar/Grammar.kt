package ru.anton.asmirko.grammar

data class Grammar<T>(
    val rules: List<Rule<T>>,
    val nonTerminals: Set<NonTerminalToken<T>>,
    val aLattice: Set<T>,
    val otherLattice: Set<T>,
    val epsilonToken: EpsilonToken<T>,
    val bucksToken: BucksToken<T>,
    val startNonTerminal: NonTerminalToken<T>,
    val latticeSubstitute: TerminalToken<T>
)

data class Rule<T>(val nonTerminal: NonTerminalToken<T>, val rightSide: List<Token<T>>)

sealed class Token<T>(open val value: T) {
    override fun equals(other: Any?): Boolean {
        if (other is Token<*>) {
            return value?.equals(other.value) ?: false
        }
        return false
    }

    override fun hashCode(): Int {
        return value?.hashCode() ?: 0
    }

    override fun toString(): String {
        return value.toString()
    }
}

class NonTerminalToken<T>(override val value: T) : Token<T>(value)

open class TerminalToken<T>(override val value: T) : Token<T>(value)

class EpsilonToken<T>(override val value: T) : TerminalToken<T>(value)

class BucksToken<T>(override val value: T) : TerminalToken<T>(value)

