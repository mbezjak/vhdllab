package hr.fer.zemris.vhdllab.validation;

import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.CREATED_ON;
import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.DELETED_ON;
import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.DELETED_ON_BEFORE_CREATED_ON;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.History;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class DeletedOnGreaterThenCreatedOnConstraintValidatorTest {

    private final Validator<?> validator = new DeletedOnGreaterThenCreatedOnConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValidNullDeletedOn() {
        assertTrue("not valid when deleted on is null.", validator
                .isValid(new History()));
    }

    @Test
    public void isValid() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(DELETED_ON);
        assertTrue("not valid when deleted on is greater then created on.",
                validator.isValid(history));
    }

    @Test
    public void isValidCreatedOnEqualsDeletedOn() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(CREATED_ON);
        assertFalse("valid when created on equals deleted on.", validator
                .isValid(history));
    }

    @Test
    public void isValidDeletedOnBeforeCreatedOnCreatedOn() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(DELETED_ON_BEFORE_CREATED_ON);
        assertFalse("valid when deleted on before created on.", validator
                .isValid(history));
    }

}
