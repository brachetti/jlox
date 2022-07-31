package com.craftinginterpreters.lox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Scanner {

    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private static final Map<String, TokenType> keywords;

    static {
        keywords = new HashMap<>();
        keywords.put("and", TokenType.AND);
        keywords.put("or", TokenType.OR);
        keywords.put("class", TokenType.CLASS);
        keywords.put("else", TokenType.ELSE);
        keywords.put("if", TokenType.IF);
        keywords.put("false", TokenType.FALSE);
        keywords.put("true", TokenType.TRUE);
        keywords.put("for", TokenType.FOR);
        keywords.put("while", TokenType.WHILE);
        keywords.put("fun", TokenType.FUN);
        keywords.put("nil", TokenType.NIL);
        keywords.put("print", TokenType.PRINT);
        keywords.put("return", TokenType.RETURN);
        keywords.put("super", TokenType.SUPER);
        keywords.put("this", TokenType.THIS);
        keywords.put("var", TokenType.VAR);
    }

    private int start = 0;
    private int current = 0;
    private int line = 1;

    public Scanner(String source) {
        this.source = source;
    }

    public List<Token> scanTokens() {
        loop();
        tokens.add(new Token(TokenType.EOF, "", null, line));

        return this.tokens;
    }

    private void loop() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }
    }

    private void scanToken() {
        final char c = advance();
        switch (c) {
            case '(':
                addToken(TokenType.LEFT_PAREN);
                break;
            case ')':
                addToken(TokenType.RIGHT_PAREN);
                break;
            case '{':
                addToken(TokenType.LEFT_BRACE);
                break;
            case '}':
                addToken(TokenType.RIGHT_BRACE);
                break;
            case ',':
                addToken(TokenType.COMMA);
                break;
            case '.':
                addToken(TokenType.DOT);
                break;
            case '-':
                addToken(TokenType.MINUS);
                break;
            case '+':
                addToken(TokenType.PLUS);
                break;
            case ';':
                addToken(TokenType.SEMICOLON);
                break;
            case '*':
                addToken(TokenType.STAR);
                break;

            case '!':
                addToken(match('=') ? TokenType.BANG_EQUAL : TokenType.BANG);
                break;
            case '=':
                addToken(match('=') ? TokenType.EQUAL_EQUAL : TokenType.EQUAL);
                break;
            case '>':
                addToken(match('=') ? TokenType.GREATER_EQUAL : TokenType.GREATER);
                break;
            case '<':
                addToken(match('=') ? TokenType.LESS_EQUAL : TokenType.LESS);
                break;

            case '/':
                if (match('/')) {
                    // comments go until the end of the line
                    while (peek() != '\n' && !isAtEnd())
                        advance();
                } else if (match('*')) {
                    handleMultilineComments();
                } else {
                    addToken(TokenType.SLASH);
                }
                break;

            case ' ':
            case '\t':
            case '\r':
                // ignore whitespace
                break;

            case '\n':
                line++;
                break;

            case '"':
                handleString();
                break;

            default:
                if (isDigit(c)) {
                    handleNumber();
                } else if (isAlpha(c)) {
                    handleIdentifier();
                } else {
                    Lox.error(line, "Unexpected character: " + c);
                }
                break;
        }
    }

    private void handleMultilineComments() {
        boolean commentEnded = false;

        outer:
        while (!isAtEnd()) {
            char c = peek();
            
            switch (c) {
                // case '/':
                    // handle nested multi line comments
                    // if (match('*')) {
                    //     handleMultilineComments();
                    // }
                    // break;

                case '*':
                    if (match('/')) {
                        commentEnded = true;
                        break outer;
                    }

                case '\n':
                    line++;
                    break;

                default:
                    break;
            }

            advance();
        }

        if (!commentEnded) {
            Lox.error(line, "Unterminated multiline comment");
        }
    }

    private void handleIdentifier() {
        while (isAlphaNumeric(peek())) advance();

        String text = source.substring(start, current);
        TokenType type = keywords.get(text);
        if (type == null) {
            addToken(TokenType.IDENTIFIER, text);
        } else {
            addToken(type);
        }
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isAlpha(char c) {
        return 
            (c >= 'a' && c <= 'z') ||
            (c >= 'A' && c <= 'Z') ||
            c == '_';
    }

    private void handleNumber() {
        while (isDigit(peek()))
            advance();

        // look for a fraction part
        if (peek() == '.' && isDigit(peekNext())) {
            // consume the .
            advance();

            while (isDigit(peek()))
                advance();
        }

        addToken(TokenType.NUMBER, Double.parseDouble(source.substring(start, current)));
    }

    private char peekNext() {
        if (current + 1 > source.length())
            return '\0';
        return source.charAt(current + 1);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private void handleString() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n')
                line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string, mate!");
            return;
        }

        advance(); // the closing ".

        // trim the surrounding quotes
        String value = source.substring(start + 1, current - 1);
        addToken(TokenType.STRING, value);
    }

    private char peek() {
        if (isAtEnd())
            return '\0';
        return source.charAt(current);
    }

    private boolean match(char expected) {
        if (isAtEnd())
            return false;
        if (source.charAt(current) != expected)
            return false;

        current++;
        return true;
    }

    private char advance() {
        return source.charAt(current++);
    }

    private void addToken(TokenType type) {
        addToken(type, null);
    }

    private void addToken(TokenType type, Object literal) {
        tokens.add(new Token(type, null, literal, line));
    }

    private boolean isAtEnd() {
        return current >= source.length();
    }

}
