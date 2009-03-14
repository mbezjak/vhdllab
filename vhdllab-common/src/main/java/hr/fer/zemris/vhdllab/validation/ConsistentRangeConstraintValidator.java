package hr.fer.zemris.vhdllab.validation;

import hr.fer.zemris.vhdllab.service.ci.Port;

import org.hibernate.validator.Validator;

public class ConsistentRangeConstraintValidator implements
        Validator<ConsistentRangeConstraint> {

    @Override
    public void initialize(ConsistentRangeConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof Port) {
            Port port = (Port) value;
            Integer from = port.getFrom();
            Integer to = port.getTo();
            return (from == null && to == null) || (from != null && to != null);
        }
        return false;
    }

}
