package ru.anton.asmirko.grammar_utills.data

data class Grammar(val rules: List<Rule>)

data class Rule(val nonTerminal: Char, val rightSide: String)