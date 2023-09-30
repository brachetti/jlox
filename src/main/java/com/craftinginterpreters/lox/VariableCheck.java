package com.craftinginterpreters.lox;

public enum VariableCheck {
    DECLARED, DEFINED, ACCESSED;

     final static VariableCheck declare() {
        return VariableCheck.DECLARED;
    }

     final static VariableCheck define() {
        return VariableCheck.DEFINED;
    }

     final static VariableCheck access() {
        return VariableCheck.ACCESSED;
    }

    boolean wasDefined() {
        return this.equals(DEFINED) || wasAccessed();
    }

    boolean wasAccessed() {
        return this.equals(ACCESSED);
    }
}
