package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function declaration;
    private final Environment closure;

    LoxFunction(Stmt.Function declaration, Environment closure) {
        this.declaration = declaration;
        this.closure = closure;
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
            return result.value;
        }

        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
