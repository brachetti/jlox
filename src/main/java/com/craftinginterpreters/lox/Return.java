package com.craftinginterpreters.lox;

public class Return extends RuntimeException {
    final Object value;

    Return(Object value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "<Return>";
    }
}
