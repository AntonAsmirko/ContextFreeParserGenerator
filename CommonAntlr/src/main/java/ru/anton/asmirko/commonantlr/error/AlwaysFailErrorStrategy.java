package ru.anton.asmirko.commonantlr.error;

import org.antlr.v4.runtime.DefaultErrorStrategy;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.RecognitionException;

public class AlwaysFailErrorStrategy extends DefaultErrorStrategy {

    @Override
    public void reportError(Parser recognizer, RecognitionException e) {
        super.reportError(recognizer, e);
        throw new CalcParserException("Error");
    }

    @Override
    protected void reportMissingToken(Parser recognizer) {
        super.reportMissingToken(recognizer);
        throw new CalcParserException("Missing token!");
    }
}
