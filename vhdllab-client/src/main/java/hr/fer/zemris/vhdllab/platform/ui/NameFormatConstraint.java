package hr.fer.zemris.vhdllab.platform.ui;

import hr.fer.zemris.vhdllab.api.util.StringFormat;

import org.springframework.core.closure.Constraint;
import org.springframework.core.closure.support.AbstractConstraint;

public class NameFormatConstraint extends AbstractConstraint {

    private static final long serialVersionUID = 1L;

    private static final NameFormatConstraint INSTANCE = new NameFormatConstraint();

    private NameFormatConstraint() {
    }

    @Override
    public boolean test(Object argument) {
        if (argument instanceof String) {
            return StringFormat.isCorrectEntityName((String) argument);
        }
        return false;
    }

    public static Constraint instance() {
        return INSTANCE;
    }

}
