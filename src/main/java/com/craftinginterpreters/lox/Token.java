package com.craftinginterpreters.lox;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    /**
     * @param type
     * @param lexeme
     * @param literal
     * @param line
     */
    public Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }
    @Override
    public String toString() {
        return type 
        + " " 
        + (lexeme != null ? lexeme : "") 
        + " " 
        + (literal != null ? literal : "");
    }
}
