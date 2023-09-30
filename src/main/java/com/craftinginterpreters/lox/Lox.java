package com.craftinginterpreters.lox;

import static com.craftinginterpreters.lox.ExitCode.NORMAL;
import static com.craftinginterpreters.lox.ExitCode.SCANNER_ERROR;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Lox main class.
 */
public final class Lox {

  private static boolean hadError;
  private static Interpreter interpreter = new Interpreter();

  /**
   * Says hello to the world.
   *
   * @param args The arguments of the program.
   * @throws IOException
   */
  public static void main(final String[] args) throws IOException {
    if (args.length > 1) {
      System.out.println("Usage: jlox [script]");
      System.exit(NORMAL.getCode());
    }
    
    if (args.length == 1) {
      runFile(args[0]);
    } else {
      runPrompt();
    }
  }

  private static void runPrompt() throws IOException {
    InputStreamReader input = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(input);

    for (;;) {
      System.out.print("> ");
      String line = reader.readLine();
      if (line == null) {
        break;
      }
      run(line);

      // reset, to not kill the entire experience
      hadError = false;
    }
  }

  private static void runFile(final String path) throws IOException {
    byte[] bytes = Files.readAllBytes(Paths.get(path));
    run(new String(bytes, Charset.defaultCharset()));

    if (hadError) {
      System.exit(SCANNER_ERROR.getCode());
    }
  }

  private static void run(final String source) {
    final Scanner scanner = new Scanner(source);
    List<Token> tokens = scanner.scanTokens();

    Parser parser = new Parser(tokens);
    List<Stmt> statements = parser.parse();

    if (hadError)
      return;

    Resolver resolver = new Resolver(interpreter);
    resolver.resolve(statements);

    if (hadError) return;

    interpreter.interpret(statements);
  }

  static void error(final int line, final String message) {
    report(line, "", message);
  }

  private static void report(final int line, final String where, final String message) {
    System.err.println("[line " + line + "] Error " + where + ": " + message);
    hadError = true;
  }

  public static void error(Token token, String message) {
    if (token.type == TokenType.EOF) {
      report(token.line, " at end", message);
    } else {
      report(token.line, " at '" + token.lexeme + "'", message);
    }
  }
}
