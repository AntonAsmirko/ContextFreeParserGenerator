package ru.anton.asmirko.antlrcalculator.parser;

import org.antlr.v4.runtime.TokenStream;
import ru.anton.asmirko.antlrcalculator.ExprParser;
import ru.anton.asmirko.commonantlr.error.AlwaysFailErrorStrategy;

public class ExprParserWithErrors extends ExprParser {

    public ExprParserWithErrors(TokenStream input) {
        super(input);
        super._errHandler = new AlwaysFailErrorStrategy();
    }
}
