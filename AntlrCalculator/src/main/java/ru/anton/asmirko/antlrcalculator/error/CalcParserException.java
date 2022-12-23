package ru.anton.asmirko.antlrcalculator.error;

public class CalcParserException extends IllegalStateException {

    public CalcParserException(String message){
        super(message);
    }
}
