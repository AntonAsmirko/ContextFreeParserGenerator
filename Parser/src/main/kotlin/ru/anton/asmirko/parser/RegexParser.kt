package ru.anton.asmirko

import com.sun.org.apache.xerces.internal.xni.grammars.Grammar

class RegexParser(private val grammar: Grammar): Parser<Char> {

    val firstSets = FirstBuilder(grammar)

    override fun parse(str: String): Tree<Char> {

    }
}