package com.craftinginterpreters.lox;

import java.util.List;

public class LoxFunction implements LoxCallable {

    private final Stmt.Function declaration;

    LoxFunction(Stmt.Function declaration) {
        this.declaration = declaration;
    }

    @Override
    public Integer arity() {
        return this.declaration.params.size();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        Environment localEnvironment = new Environment(interpreter.globals);

        for (int i = 0; i < arity(); i++) {
            localEnvironment.define(
                declaration.params.get(i).lexeme, 
                arguments.get(i)
            );
        }

        interpreter.executeBlock(declaration.body, localEnvironment);
        return null;
    }

    @Override
    public String toString() {
        return "<fn " + declaration.name.lexeme + ">";
    }
}
