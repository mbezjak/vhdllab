package hr.fer.zemris.vhdllab.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class NameFormatConstraintValidatorTest {

    private final Validator<?> validator = new NameFormatConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("regular object is valid.", validator.isValid(new Object()));
    }

    @Test
    public void isValidNameCorrect() {
        assertTrue("not valid when correct name format.", validator
                .isValid("correct_name_format"));
        assertTrue("one letter.", validator.isValid("a"));
        assertTrue("one letter testbench name.", validator.isValid("a_tb"));
        assertTrue("all letters.", validator.isValid("circuitAND"));
        assertTrue("with underscore.", validator.isValid("circuit_AND"));
        assertTrue("all lowercase.", validator.isValid("circuit_and"));
        assertTrue("all uppercase.", validator.isValid("CIRCUIT_AND"));
        assertTrue("with number.", validator.isValid("circuitAND4"));
        assertTrue("with underscore and number.", validator
                .isValid("circuit_AND_4"));
        assertTrue("double number.", validator.isValid("circuit_AND_41"));
        assertTrue("double number, underscore separated.", validator
                .isValid("circuit_AND_4_1"));
        assertTrue("number in the middle.", validator.isValid("circuit4AND"));
    }

    @Test
    public void isValidNameIncorrect() {
        assertFalse("valid with empty name.", validator.isValid(""));
        assertFalse("starts with illegal character.", validator
                .isValid("$circuitAND"));
        assertFalse("contains illegal character.", validator
                .isValid("circuit!AND"));
        assertFalse("starts with an underscore.", validator
                .isValid("_circuitAND"));
        assertFalse("starts with a number.", validator.isValid("1circuitAND"));
        assertFalse("double underscore.", validator.isValid("circuit__AND"));
        assertFalse("ends with an underscore.", validator
                .isValid("circuitAND_"));
        assertFalse("not character from English alphabet.", validator
                .isValid("circuit_š_AND"));
    }

    @Test
    public void isValidNameNotSuitable() {
        assertFalse("xor - reserved keyword.", validator.isValid("xor"));
        assertFalse("architecture - reserved keyword.", validator
                .isValid("architecture"));
        assertFalse("end - reserved keyword.", validator.isValid("end"));
        assertFalse("if - flow control keyword.", validator.isValid("if"));
        assertFalse("std_logic - reserved vhdl type.", validator
                .isValid("std_logic"));
    }

}
