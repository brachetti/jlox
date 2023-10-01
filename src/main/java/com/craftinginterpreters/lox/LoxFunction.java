package com.craftinginterpreters.lox;

import java.util.List;

import com.craftinginterpreters.lox.Stmt.Function;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function declaration;
    private final Environment closure;

    private final boolean isInitializer;

    LoxFunction(Stmt.Function declaration, Environment closure) {
        this(declaration, closure, false);
    }

    public LoxFunction(Function declaration, Environment closure, boolean isInitializer) {
        this.declaration = declaration;
        this.closure = closure;
        this.isInitializer = isInitializer;
    }

    @Override
    public Integer arity() {
        return this.declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment local = new Environment(closure);
        for (int i = 0; i < arity(); i++) {
            local.define(
                declaration.params.get(i).lexeme, 
                arguments.get(i)
            );
        }

        try {
            interpreter.executeBlock(declaration.body, local);
        } catch (Return result) {
            if (isInitializer) {
                return closure.getAt(0, "this");
            }
            return result.value;
        }

        if (isInitializer) return closure.getAt(0, "this");

        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }

    public LoxFunction bind(LoxInstance loxInstance) {
        Environment classClosure = new Environment(closure);
        classClosure.define("this", loxInstance);
        return new LoxFunction(
            declaration, 
            classClosure, 
            isInitializer
        );
    }
}
