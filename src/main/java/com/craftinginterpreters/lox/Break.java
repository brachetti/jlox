package com.craftinginterpreters.lox;

public class Break extends RuntimeException {
    
    public Break() {}

    @Override
    public String toString() {
        return "<Break>";
    }
}
