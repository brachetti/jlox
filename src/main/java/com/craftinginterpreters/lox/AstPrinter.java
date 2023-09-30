package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Assign;
import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Call;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Logical;
import com.craftinginterpreters.lox.Expr.Unary;
import com.craftinginterpreters.lox.Expr.Variable;

public class AstPrinter implements Expr.Visitor<String> {

    String print(Expr expr) {
        return expr.accept(this);
    }

    private String parenthesize(String name, Expr... exprs) {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(name);
        for (Expr expr : exprs) {
            sb.append(" ");
            sb.append(expr.accept(this));
        }
        sb.append(")");

        return sb.toString();
    }

    @Override
    public String visitBinaryExpr(Binary expr) {
        return parenthesize(expr.operator.lexeme, expr.left, expr.right);
    }

    @Override
    public String visitCallExpr(Call expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitGroupingExpr(Grouping expr) {
        return parenthesize("group", expr.expression);
    }

    @Override
    public String visitLiteralExpr(Literal expr) {
        if (expr.value == null) return "nil";
        return expr.value.toString();
    }

    @Override
    public String visitUnaryExpr(Unary expr) {
        return parenthesize(expr.operator.lexeme, expr.right);
    }

    @Override
    public String visitVariableExpr(Variable expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitAssignExpr(Assign expr) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String visitLogicalExpr(Logical expr) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'visitLogicalExpr'");
    }
    
}
