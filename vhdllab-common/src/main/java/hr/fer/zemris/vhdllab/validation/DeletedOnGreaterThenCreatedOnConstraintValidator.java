package hr.fer.zemris.vhdllab.validation;

import hr.fer.zemris.vhdllab.entity.History;

import java.util.Date;

import org.hibernate.validator.Validator;

public class DeletedOnGreaterThenCreatedOnConstraintValidator implements
        Validator<DeletedOnGreaterThenCreatedOnConstraint> {

    @Override
    public void initialize(DeletedOnGreaterThenCreatedOnConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof History) {
            History history = (History) value;
            Date deletedOn = history.getDeletedOn();
            Date createdOn = history.getCreatedOn();
            return deletedOn != null ? deletedOn.compareTo(createdOn) > 0
                    : true;
        }
        return false;
    }

}
