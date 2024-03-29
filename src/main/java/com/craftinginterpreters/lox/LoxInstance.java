package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.Map;

public class LoxInstance {

    private final LoxClass klass;
    private final Map<String, Object> fields = new HashMap<>();

    public LoxInstance(LoxClass loxClass) {
        this.klass = loxClass;
    }

    Object get(Token name) {
        final String identifier = name.lexeme;
        if (fields.containsKey(identifier)) {
            return fields.get(identifier);
        }

        LoxFunction method = klass.findMethod(identifier);
        if (method != null) return method.bind(this);

        method = klass.findClassMethod(identifier);
        if (method != null) return method.bind(klass);

        throw new RuntimeError("Undefined property '" + identifier + "'.");
    }

    @Override
    public String toString() {
        return "<LoxInstance [klass=" + klass.getName() + "]>";
    }

    public void set(Token name, Object value) {
        fields.put(name.lexeme, value);
    }
}
