package com.craftinginterpreters.lox;

public class UnknownVariableError extends RuntimeException {

    @Override
    public String toString() {
        return "<Unknown Variable Error>";
    }
    
}
