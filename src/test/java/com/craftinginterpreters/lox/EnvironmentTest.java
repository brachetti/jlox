package com.craftinginterpreters.lox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class EnvironmentTest {

    /**
     * SUT
     */
    private Environment environment;
    private int answerCount;
    private Object answerValue;

    @BeforeEach
    void setUp() {
        this.environment = new Environment();
    }

    @Test
    void shouldBeEmpty() {
        givenAnEmptyEnvironment();
        whenCountingVars();
        thenCountIs(0);
    }

    @Test
    void shouldBeEmptyWithEmptyBlock() {
        givenAnEmptyEnvironment();
        givenANewBlockEnv();
        whenCountingVars();
        thenCountIs(0);
        whenCountingAllVars();
        thenCountIs(0);
    }

    @Test
    void shouldFindInitializedVar() {
        givenAnEmptyEnvironment();
        givenVariableDefinedAndAssigned("one", 1);
        whenGettingValueOf("one");
        thenValueIs(1);
    }

    private void thenValueIs(Object expected) {
        assertEquals(expected, this.answerValue);
    }

    private void whenGettingValueOf(String name) {
        this.answerValue = this.environment.get(identifier(name));
    }

    private Token identifier(String name) {
        return new Token(TokenType.IDENTIFIER, name, null, 0);
    }

    private void givenVariableDefinedAndAssigned(String name, Object value) {
        this.environment.define(name, value);
    }

    private void whenCountingAllVars() {
        this.answerCount = this.environment.countAll();
    }

    private void givenANewBlockEnv() {
        this.environment = new Environment(this.environment);
    }

    private void thenCountIs(int expected) {
        assertEquals(expected, this.answerCount);
    }

    private void whenCountingVars() {
        this.answerCount = this.environment.count();
    }

    private void givenAnEmptyEnvironment() {
        this.environment = new Environment();
    }
}
