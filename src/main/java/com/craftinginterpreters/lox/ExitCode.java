package com.craftinginterpreters.lox;

/**
 * Exit Codes instead of Magic Numbers.
 */
public enum ExitCode {

    /** Normal exit. */
    NORMAL("Normal exit", 64),
    /** Error in Scanner. */
    SCANNER_ERROR("Error in Scanner", 65);

    private final String name;
    private final int code;

    ExitCode(String name, int code) {
        this.name = name;
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }
}
