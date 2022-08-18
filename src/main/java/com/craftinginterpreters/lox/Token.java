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
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((lexeme == null) ? 0 : lexeme.hashCode());
        result = prime * result + ((literal == null) ? 0 : literal.hashCode());
        result = prime * result + ((type == null) ? 0 : type.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Token other = (Token) obj;
        if (lexeme == null) {
            if (other.lexeme != null)
                return false;
        } else if (!lexeme.equals(other.lexeme))
            return false;
        if (literal == null) {
            if (other.literal != null)
                return false;
        } else if (!literal.equals(other.literal))
            return false;
        if (type != other.type)
            return false;
        return true;
    }

    
}
