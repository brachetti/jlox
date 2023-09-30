package com.craftinginterpreters.lox;

import java.util.List;
import java.util.Map;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;

import com.craftinginterpreters.lox.Expr.Assign;
import com.craftinginterpreters.lox.Expr.Binary;
import com.craftinginterpreters.lox.Expr.Grouping;
import com.craftinginterpreters.lox.Expr.Literal;
import com.craftinginterpreters.lox.Expr.Logical;
import com.craftinginterpreters.lox.Expr.Unary;
import com.craftinginterpreters.lox.Expr.Variable;
import com.craftinginterpreters.lox.Stmt.Block;
import com.craftinginterpreters.lox.Stmt.Expression;
import com.craftinginterpreters.lox.Stmt.Function;
import com.craftinginterpreters.lox.Stmt.If;
import com.craftinginterpreters.lox.Stmt.Print;
import com.craftinginterpreters.lox.Stmt.Var;
import com.craftinginterpreters.lox.Stmt.While;

public class Interpreter implements Expr.Visitor<Object>, Stmt.Visitor<Void> {

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

    public InterpreterError(Token token, String message) {
      this(message, token);
    }
  }

  final Environment globals = new Environment();
  private Environment environment = globals;
  private final Map<Expr, Integer> locals = new HashMap<>();

  Interpreter() {
    globals.define("clock", new LoxCallable() {
      
      @Override
      public Integer arity() {
        return 0;
      }

      @Override
      public Object call(Interpreter interpreter, List<Object> arguments) {
        return (double) System.currentTimeMillis() / 1000.0;
      }

      @Override
      public String toString() {
        return "<native fn>";
      }
    });
  }

  public void interpret(List<Stmt> statements) {
    try {
      for (Stmt statment : statements) {
        execute(statment);
      }
    } catch (InterpreterError error) {

    }
  }

  private void execute(Stmt statement) {
    statement.accept(this);
  }

  void resolve(Expr expr, int depth) {
    locals.put(expr, depth);
  }

  private String stringify(Object object) {
    if (object == null)
      return "nil";

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
        return (double) left - (double) right;
      case PLUS:
        if (left instanceof Double && right instanceof Double) {
          return (double) left + (double) right;
        } else if (left instanceof String && right instanceof String) {
          return (String) left + (String) right;
        }

        error(expr.operator, "operands must be either both numbers or both Strings");
      case STAR:
        checkNumberOperand(expr.operator, left, right);
        return (double) left * (double) right;
      case SLASH:
        checkNumberOperand(expr.operator, left, right);
        guardAgainstDiv0(expr, right);
        return (double) left / (double) right;
      case GREATER:
        checkNumberOperand(expr.operator, left, right);
        return (double) left > (double) right;
      case GREATER_EQUAL:
        checkNumberOperand(expr.operator, left, right);
        return (double) left >= (double) right;
      case LESS:
        checkNumberOperand(expr.operator, left, right);
        return (double) left < (double) right;
      case LESS_EQUAL:
        checkNumberOperand(expr.operator, left, right);
        return (double) left <= (double) right;
      case BANG_EQUAL:
        return !isEqual(left, right);
      case EQUAL_EQUAL:
        return isEqual(left, right);
      default:
        break;
    }

    return null;
  }

  @Override
  public Object visitCallExpr(Expr.Call expr) {
    Object callee = evaluate(expr.callee);
    List<Object> arguments = new ArrayList<>();
    for (Expr argument : expr.arguments) {
      arguments.add(evaluate(argument));
    }

    if (!(callee instanceof LoxCallable)) {
      throw new InterpreterError(expr.paren, "Can only call functions and classes.");
    }

    LoxCallable function = (LoxCallable) callee;

    if (arguments.size() != function.arity()) {
      throw new InterpreterError(expr.paren,
          "Expected " + 
          function.arity() + " arguments, got " +
           arguments.size() + ".");
    }

    return function.call(this, arguments);
  }

  private void guardAgainstDiv0(Binary expr, Object right) {
    if (0 == (double) right) {
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
    if (left == null && right == null)
      return true;
    if (left == null)
      return false;

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
  public Object visitLogicalExpr(Logical expr) {
    Object left = evaluate(expr.left);

    if (expr.operator.type == TokenType.OR) {
      if (isTruthy(left)) return left;
    } else {
      if (!isTruthy(left)) return left;
    }
    
    return evaluate(expr.right);
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
      default:
        break;
    }

    return null;
  }

  private boolean isTruthy(Object object) {
    if (object == null)
      return false;
    if (object instanceof Boolean)
      return (boolean) object;

    return true;
  }

  private Object evaluate(Expr expression) {
    return expression.accept(this);
  }

  private InterpreterError error(Token token, String message) {
    Lox.error(token, message);
    return new InterpreterError(message, token);
  }

  @Override
  public Void visitExpressionStmt(Expression stmt) {
    evaluate(stmt.expression);
    return null;
  }

  @Override
  public Void visitFunctionStmt(Function stmt) {
    LoxFunction function = new LoxFunction(stmt, environment);
    environment.define(stmt.name.lexeme, function);

    return null;
  }

  @Override
  public Void visitPrintStmt(Print stmt) {
    Object value = evaluate(stmt.expression);
    System.out.println(stringify(value));
    return null;
  }

  @Override
  public Void visitReturnStmt(Stmt.Return stmt) {
    Object value = null;
    if (stmt.expression != null) {
      value = evaluate(stmt.expression);
    }

    throw new Return(value);
  }

  @Override
  public Void visitBreakStmt(Stmt.Break stmt) {
    throw new Break();
  }

  @Override
  public Void visitVarStmt(Var stmt) {
    Object value = null;
    if (stmt.initializer != null) {
      value = evaluate(stmt.initializer);
    }

    environment.define(stmt.name.lexeme, value);
    return null;
  }
  
  @Override
  public Void visitWhileStmt(While stmt) {
    while (isTruthy(evaluate(stmt.condition))) {
      try {
        execute(stmt.body);
      } catch (Break breakStatement) {
        return null;
      }
    }
    return null;
  }

  @Override
  public Object visitVariableExpr(Variable expr) {
    return lookupVariable(expr.name, expr);
  }

  private Object lookupVariable(Token name, Expr expr) {
    Integer distance = locals.get(expr);
    if (distance != null) {
      Object value = environment.getAt(distance, name);
      return value;
    }

    return globals.get(name);
  }

  @Override
  public Object visitAssignExpr(Assign expr) {
    Object value = evaluate(expr.value);

    Integer distance = locals.get(expr);
    if (distance != null) {
      environment.assignAt(distance, expr.name, value);
    } else {
      globals.assign(expr.name, value);
    }

    return value;
  }

  @Override
  public Void visitBlockStmt(Block stmt) {
    executeBlock(stmt.statements, new Environment(environment));
    return null;
  }

  void executeBlock(List<Stmt> statements, Environment environment) {
    Environment previous = this.environment;
    try {
      this.environment = environment;

      for (Stmt statement : statements) {
        execute(statement);
      }
    } catch (Break breakStatement) {
      throw new SyntaxError("break can not occur outside of while loop");
    } finally {
      this.environment = previous;
    }
  }

  @Override
  public Void visitIfStmt(If stmt) {
    if (isTruthy(evaluate(stmt.condition))) {
      execute(stmt.thenBranch);
    } else {
      if (stmt.elseBranch != null) {
        execute(stmt.elseBranch);
      }
    }
    return null;
  }
}
