package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InterpreterTest extends CommonTest {

    /**
     * SUT
     */
    private Interpreter interpreter;
    private List<Stmt> inInput;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    
    @BeforeEach
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @BeforeEach
    void setUp() {
        this.interpreter = new Interpreter();
    }

    @Test
    void shouldPrintNumber() {
        givenInput(inputs(
            Print(literal(1))
        ));
        whenInterpreting();
        thenContentWasPrinted("1");
    }

    @Test
    void shouldPrintString() {
        givenInput(inputs(
            Print(literal("abc"))
        ));
        whenInterpreting();
        thenContentWasPrinted("abc");
    }

    @Test
    void shouldPrintNothing() {
        givenInput(inputs(
            
        ));
        whenInterpreting();
        thenNothingWasPrinted();
    }

    @Test
    void shouldPrintVar() {
        givenInput(inputs(
            S_Var("name", literal("value")),
            Print(E_Var("name"))
        ));
        whenInterpreting();
        thenContentWasPrinted("value");
    }

    @Test
    void shouldPrintVar_Calc() {
        givenInput(inputs(
            S_Var("name", Calc(literal(1.0), Plus(), literal(3.0))),
            Print(E_Var("name"))
        ));
        whenInterpreting();
        thenContentWasPrinted("4");
    }

    @Test
    void shouldPrintRedefinedVar() {
        givenInput(inputs(
            S_Var("name", Calc(literal(1.0), Plus(), literal(3.0))),
            S_Var("name", Calc(literal(2.0), Plus(), literal(3.0))),
            Print(E_Var("name"))
        ));
        whenInterpreting();
        thenContentWasPrinted("5");
    }

    private void thenContentWasPrinted(String expected) {
        assertEquals(expected + "\n", outContent.toString());
    }

    private void thenNothingWasPrinted() {
        assertEquals("", outContent.toString());
    }

    private void whenInterpreting() {
        this.interpreter.interpret(this.inInput);
    }

    private List<Stmt> inputs(Stmt ...inputs) {
        return Arrays.asList(inputs);
    }

    private void givenInput(List<Stmt> input) {
        this.inInput = input;
    }
}
