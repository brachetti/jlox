package com.craftinginterpreters.lox;

import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Unary;

public class Interpreter implements Expr.Visitor<Object> {

    public static class InterpreterError extends RuntimeException {
        final Token token;

        /**
         * @param message
         * @param token
         */
        public InterpreterError(String message, Token token) {
            super(message);
            this.token = token;
        }
    }

    public String interpret(Expr expr) {
        try {
            Object result = evaluate(expr);
            return stringify(result);
        } catch (InterpreterError error) {
            return null;
        }
    }

    private String stringify(Object object) {
        if (object == null) return "nil";

        if (object instanceof Double) {
            String text = object.toString();
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length() - 2);
            }
            return text;
        }

        return object.toString();
    }

    @Override
    public Object visitBinaryExpr(Binary expr) {
        Object left = evaluate(expr.left);
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, left, right);
                return (double)left - (double)right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double)left + (double)right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String)left + (String)right;
                }

                error(expr.operator, "operands must be either both numbers or both Strings");
            case STAR:
                checkNumberOperand(expr.operator, left, right);
                return (double)left * (double)right;
            case SLASH:
                checkNumberOperand(expr.operator, left, right);
                guardAgainstDiv0(expr, right);
                return (double)left / (double)right;
            case GREATER:
                checkNumberOperand(expr.operator, left, right);
                return (double)left > (double)right;
            case GREATER_EQUAL:
                checkNumberOperand(expr.operator, left, right);
                return (double)left >= (double)right;
            case LESS:
                checkNumberOperand(expr.operator, left, right);
                return (double)left < (double)right;
            case LESS_EQUAL:
                checkNumberOperand(expr.operator, left, right);
                return (double)left <= (double)right;
            case BANG_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            default: break;
        }
        
        return null;
    }

    private void guardAgainstDiv0(Binary expr, Object right) {
        if (0 == (double)right) {
            throw error(expr.operator, "Division by 0");
        }
    }

    private void checkNumberOperand(Token token, Object... operands) {
        for (Object operand : operands) {
            if (!(operand instanceof Double)) {
                throw error(token, "Operand must be a number");
            }
        }
    }

    private boolean isEqual(Object left, Object right) {
        if (left == null && right == null) return true;
        if (left == null) return false;

        return left.equals(right);
    }

    @Override
    public Object visitGroupingExpr(Grouping expr) {
        return evaluate(expr.expression);
    }

    @Override
    public Object visitLiteralExpr(Literal expr) {
        return expr.value;
    }

    @Override
    public Object visitUnaryExpr(Unary expr) {
        Object right = evaluate(expr.right);

        switch (expr.operator.type) {
            case MINUS:
                checkNumberOperand(expr.operator, right);                
                return -(double) right;
                
            case BANG:
                return !isTruthy(right);
            default: break;
        }

        return null;
    }

    private boolean isTruthy(Object object) {
        if (object == null) return false;
        if (object instanceof Boolean) return (boolean) object;
        
        return true;
    }

    private Object evaluate(Expr expression) {
        return expression.accept(this);
    }

    private InterpreterError error(Token token, String message) {
        Lox.error(token, message);
        return new InterpreterError(message, token);
    }
}
