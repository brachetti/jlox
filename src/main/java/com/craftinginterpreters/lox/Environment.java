package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Environment enclosing;

    /**
     * 
     */
    public Environment() {
        this.enclosing = null;
    }

    /**
     * @param enclosing
     */
    public Environment(Environment enclosing) {
        this.enclosing = enclosing;
    }

    void define(String name, Object value) {
        this.values.put(name, value);
    }

    int count() {
        return values.size();
    }

    int countAll() {
        if (enclosing != null) {
            return enclosing.countAll() + count();
        }

        return count();
    }

    Object get(Token name) {
        if (values.containsKey(name.lexeme)) {
            return values.get(name.lexeme);
        }
        
        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeException("Undefined variable '" + name.lexeme + "'.");
    }

    public void assign(Token name, Object value) {
        if (values.containsKey(name.lexeme)) {
            values.put(name.lexeme, value);
            return;
        } else if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeException("Undefined variable '" + name.lexeme + "'.");
    }
}
