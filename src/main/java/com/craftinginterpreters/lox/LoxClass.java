package com.craftinginterpreters.lox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoxClass extends LoxInstance implements LoxCallable {

    final static LoxClass BaseObject = new LoxClass("Object", new HashMap<>(), new HashMap<>());

    private final String name;
    private LoxInstance instance;
    private final Map<String, LoxFunction> methods;
    private final Map<String, LoxFunction> classMethods;

    public LoxClass(String name, Map<String, LoxFunction> methods, Map<String, LoxFunction> classMethods) {
        super(BaseObject);
        this.name = name;
        this.methods = methods;
        this.classMethods = classMethods;
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
        LoxFunction initializer = this.findMethod("init");
        if (initializer == null) {
            return 0;
        }
        
        return initializer.arity();
    }

    @Override
    public Object call(Interpreter interpreter, List<Object> arguments) {
        instance = new LoxInstance(this);
        LoxFunction initializer = this.findMethod("init");
        if (initializer != null) {
            initializer.bind(instance).call(interpreter, arguments);
        }
        return instance;
    }

    Object get(Token name) {
        final String identifier = name.lexeme;

        LoxFunction method = this.findClassMethod(identifier);
        if (method != null) return method
            .bind((LoxClass) this, "self")
            .bind(this, "this");

        throw new RuntimeError("Undefined property '" + identifier + "'.");
    }

    public LoxFunction findClassMethod(String identifier) {
        if (classMethods.containsKey(identifier)) {
            return classMethods.get(identifier);
        }
        return null;
    }

    public LoxFunction findMethod(String identifier) {
        if (methods.containsKey(identifier)) {
            return methods.get(identifier);
        }
        
        return null;
    }
}
