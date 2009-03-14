package hr.fer.zemris.vhdllab.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.service.ci.Port;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class ConsistentRangeConstraintValidatorTest {

    private final Validator<?> validator = new ConsistentRangeConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValid() {
        Port port = new Port();
        port.setFrom(null);
        port.setTo(null);
        assertTrue(validator.isValid(port));
    }

    @Test
    public void isValid2() {
        Port port = new Port();
        port.setFrom(2);
        port.setTo(2);
        assertTrue(validator.isValid(port));
    }

    @Test
    public void isValid3() {
        Port port = new Port();
        port.setFrom(null);
        port.setTo(2);
        assertFalse(validator.isValid(port));
    }

    @Test
    public void isValid4() {
        Port port = new Port();
        port.setFrom(2);
        port.setTo(null);
        assertFalse(validator.isValid(port));
    }

}
