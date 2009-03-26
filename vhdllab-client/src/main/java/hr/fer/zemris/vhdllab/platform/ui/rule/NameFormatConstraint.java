package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.validation.NameFormatConstraintValidator;

import org.springframework.rules.constraint.AbstractConstraint;

public class NameFormatConstraint extends AbstractConstraint {

    private static final long serialVersionUID = 1L;

    private static final NameFormatConstraintValidator VALIDATOR =
                                        new NameFormatConstraintValidator();

    @Override
    public boolean test(Object argument) {
        return VALIDATOR.isValid(argument);
    }

}
