package com.craftinginterpreters.lox;

public class LoxClass {

    private final String name;

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
}
