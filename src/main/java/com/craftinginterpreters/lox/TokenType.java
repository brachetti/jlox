package com.craftinginterpreters.lox;

public enum TokenType {
    // Single character tokens
    LEFT_PAREN, RIGHT_PAREN,
    LEFT_BRACE, RIGHT_BRACE,
    COMMA, DOT,
    MINUS, PLUS, SLASH, STAR,
    SEMICOLON,

    // one or two character tokens
    BANG, BANG_EQUAL, EQUAL, EQUAL_EQUAL,
    GREATER, GREATER_EQUAL,
    LESS, LESS_EQUAL,

    // literals
    IDENTIFIER, STRING, NUMBER,

    // keywords
    AND, OR,
    CLASS, SUPER, THIS, FUN,
    TRUE, FALSE, NIL,
    IF, ELSE, FOR, WHILE,
    PRINT, RETURN, VAR,

    EOF
}
