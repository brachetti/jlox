package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ScannerTest extends CommonTest {

    /**
     * SUT
     */
    private Scanner scanner;
    private String input;
    private List<Token> result;

    @BeforeEach
    void setUp() {
        this.scanner = null;
        this.result = new ArrayList<>();
        this.input = "";
    }

    @Test
    void shouldRecognizeEmptyString() {
        givenInput("");
        whenScanning();
        thenResultShouldBe(EOF());
    }

    @Test
    void shouldIgnoreComment() {
        givenInput("// var name");
        whenScanning();
        thenResultShouldBe(EOF());
    }

    @Test
    void shouldIgnoreComment_2() {
        givenInput("// var name\nvar name");
        whenScanning();
        thenResultShouldBe(T_Var(), identifier("name"), EOF());
    }

    private Token EOF() {
        return new Token(TokenType.EOF, "", null, 0);
    }

    private void thenResultShouldBe(Token ...expectedTokens) {
        assertEquals(expectedTokens.length, this.result.size());
        for (int i = 0; i < expectedTokens.length; i++) {
            Token expected = expectedTokens[i];
            Token actual = this.result.get(i);
            assertEquals(expected, actual, "Missed expectectation at position " + i);
        }
    }

    private void whenScanning() {
        this.scanner = new Scanner(this.input);
        this.result = this.scanner.scanTokens();
    }

    private void givenInput(String string) {
        this.input = string + "\n";
    }

}
