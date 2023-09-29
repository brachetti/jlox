package com.craftinginterpreters.tools;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import com.craftinginterpreters.lox.ExitCode;

public class GenerateAst {
    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            System.err.println(" Usage generate_ast <output directory>");
            System.exit(ExitCode.NORMAL.getCode());
        }

        String outputDir = args[0];
        defineAst(outputDir, "Expr", Arrays.asList(
        "Assign     : Token name, Expr value",    
            "Binary      : Expr left, Token operator, Expr right",
            "Call        : Expr callee, Token paren, List<Expr> arguments",       
            "Grouping    : Expr expression",
            "Literal     : Object value",
            "Logical     : Expr left, Token operator, Expr right",
            "Variable    : Token name",
            "Unary       : Token operator, Expr right"
        ));

        defineAst(outputDir, "Stmt", Arrays.asList(
            "Block     : List<Stmt> statements",    
            "Expression     : Expr expression",
            "Function       : Token name, List<Token> params, List<Stmt> body",
            "If             : Expr condition, Stmt thenBranch, Stmt elseBranch",
            "Print          : Expr expression",
            "Return         : Token keyword, Expr expression",
            "Break          : Token keyword",
            "While          : Expr condition, Stmt body",
            "Var            : Token name, Expr initializer"
        ));
    }

    private static void defineAst(String outputDir, String baseName, List<String> types) throws FileNotFoundException, UnsupportedEncodingException {
        String path = outputDir + "/" + baseName + ".java";
        PrintWriter writer = new PrintWriter(path, "UTF-8");

        writer.println("package com.craftinginterpreters.lox;");
        writer.println();
        writer.println("import java.util.List;");
        writer.println();
        writer.println("/**");
        writer.println(" * Ast class for " + baseName + ".");
        writer.println(" * ");
        writer.println(" * This class is auto-generated. Do not edit it by hand!");
        writer.println("*/");
        writer.println("abstract class " + baseName + " implements Input {");

        defineVisitor(writer, baseName, types);

        writer.println();
        writer.println("  abstract <R> R accept(Visitor<R> visitor);");
        
        defineTypes(writer, baseName, types);

        writer.println("}");
        writer.close();
    }

    private static void defineTypes(PrintWriter writer, String baseName, List<String> types) {
        for (String type : types) {
            writer.println();
            String className = type.split(":")[0].trim();
            String fields = type.split(":")[1].trim();
            defineType(writer, className, baseName, fields);
        }
    }

    private static void defineVisitor(PrintWriter writer, String baseName, List<String> types) {
        writer.println("  interface Visitor<R> {");

        for (String type : types) {
            String typeClass = type.split(":")[0].trim();
            writer.println("    R visit" + typeClass + baseName + "(" + typeClass + " " + baseName.toLowerCase() + ");");
        }

        writer.println("}");
    }

    private static void defineType(PrintWriter writer, String className, String baseName, String fieldList) {
        String[] fields = fieldList.split(", ");
        writer.println("  static class " + className + " extends " + baseName + " {");

        // fields
        for (String field : fields) {
            writer.println("    final " + field + ";");
        }
        writer.println();
        // ------

        // constructor
        writer.println("    " + className + "(" + fieldList + ") {");
        for (String field : fields) {
            String name = field.split(" ")[1];
            writer.println("      this." + name + " = " + name + ";");
        }
        writer.println("    }");
        // ------

        // Personal Visitor pattern
        writer.println();
        writer.println("    @Override");
        writer.println("    <R> R accept(Visitor<R> visitor) {");
        writer.println("      return visitor.visit" + className + baseName + "(this);");
        writer.println("    }");
        // ------

        // close type class
        writer.println("  }");
    }
}
