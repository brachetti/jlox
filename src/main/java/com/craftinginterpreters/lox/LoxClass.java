package com.craftinginterpreters.lox;

import java.util.List;

public class LoxClass implements LoxCallable {

    private final String name;
    private LoxInstance instance;

    public LoxClass(String name) {
        this.name = name;
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

    
}
