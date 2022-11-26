package ru.anton.asmirko

enum class Token(strRep: Set<String>) {
    CLINI(setOf("*")),
    OR("|"),
    L_BRACKET("("),
    R_BRACKET(")"),
    CH()
}