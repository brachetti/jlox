package com.craftinginterpreters.lox;

public class LoxInstance {

    private final LoxClass klass;

    public LoxInstance(LoxClass loxClass) {
        this.klass = loxClass;
    }

    @Override
    public String toString() {
        return "<LoxInstance [klass=" + klass.getName() + "]>";
    }

}
