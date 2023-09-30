package com.craftinginterpreters.lox;

public class RuntimeError extends RuntimeException {

    RuntimeError(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return "<RuntimeError reason='" + this.getLocalizedMessage() + "'>";
    }
}
