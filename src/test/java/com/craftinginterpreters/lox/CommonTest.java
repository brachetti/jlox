package com.craftinginterpreters.lox;

import java.util.jar.Attributes.Name;

public abstract class CommonTest {

    Expr.Binary Calc(Expr.Literal left, Token operator, Expr.Literal right) {
        return new Expr.Binary(left, operator, right);
    }

    Token Plus() {
        return new Token(TokenType.PLUS, null, null, 0);
    }

    Expr E_Var(String name) {
        return new Expr.Variable(identifier(name));
    }

    Token identifier(String name) {
        return new Token(TokenType.IDENTIFIER, name, name, 0);
    }

    Stmt.Print Print(Expr expr) {
        return new Stmt.Print(expr);
    }

    Stmt.Var S_Var(String name, Expr value) {
        return new Stmt.Var(identifier(name), value);
    }

    Stmt.Expression S_Expr(Expr expr) {
        return new Stmt.Expression(expr);
    }

    Token T_Var() {
        return new Token(TokenType.VAR, "var", "var", 0);
    }

    Expr.Literal literal(Object value) {
        return new Expr.Literal(value);
    }

    Expr E_Assign(String name, Expr value) {
        return new Expr.Assign(identifier(name), value);
    }

    Expr E_Assign(String name, Object value) {
        return new Expr.Assign(identifier(name), literal(value));
    }
}
