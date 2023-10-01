package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;

public class LoxClass implements LoxCallable {

    private final String name;
    private LoxInstance instance;
    private final Map<String, LoxFunction> methods;

    public LoxClass(String name, Map<String, LoxFunction> methods) {
        this.name = name;
        this.methods = methods;
    }

    @Override
    public String toString() {
        return "<LoxClass name=" + name + "]>";
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    @Override
    public Integer arity() {
        return 0;
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        instance = new LoxInstance(this);
        return instance;
    }

    public LoxFunction findMethod(String identifier) {
        if (methods.containsKey(identifier)) {
            return methods.get(identifier);
        }
        
        return null;
    }
}
