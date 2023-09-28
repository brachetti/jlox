package com.craftinginterpreters.lox;

import java.util.List;

interface LoxCallable {
    Integer arity();
    Object call(Interpreter interpreter, List<Object> arguments);
}
