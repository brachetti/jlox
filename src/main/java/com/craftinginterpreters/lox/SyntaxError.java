package com.craftinginterpreters.lox;

public class SyntaxError extends RuntimeException {

    public SyntaxError(String reason) {
        super(reason);
    }

    @Override
    public String toString() {
        return "<SyntaxError reason='" + this.getLocalizedMessage() + "'>";
    }
}
