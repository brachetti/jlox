package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Environment {
    private final Map<String, Object> values = new HashMap<>();
    private final Set<String> assigned = new HashSet<>();
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
        if (value != null) {
            this.assigned.add(name);
        }
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
        final String identifier = name.lexeme;
        if (values.containsKey(identifier)) {
            if (assigned.contains(identifier)) {
                return values.get(identifier);
            }
            throw new RuntimeException("Variable was not assigned before using. Identifier was '" + identifier + "'");
        }
        
        if (enclosing != null) {
            return enclosing.get(name);
        }

        throw new RuntimeException("Undefined variable '" + identifier + "'.");
    }

    public void assign(Token name, Object value) {
        final String identifier = name.lexeme;
        if (values.containsKey(identifier)) {
            values.put(identifier, value);
            assigned.add(identifier);
            return;
        } else if (enclosing != null) {
            enclosing.assign(name, value);
            return;
        }

        throw new RuntimeException("Undefined variable '" + identifier + "'.");
    }

    private Environment ancestor(int distance) {
        Environment environment = this;
        for (int i = 0; i < distance; i++) {
            environment = environment.enclosing;
        }
        return environment;
    }

    public Object getAt(Integer distance, String name) {
        return ancestor(distance).values.get(name);
    }

    public Object getAt(Integer distance, Token name) {
        return getAt(distance, name.lexeme);
    }

    public void assignAt(Integer distance, Token name, Object value) {
        ancestor(distance).values.put(name.lexeme, value);
    }
}
