package hr.fer.zemris.vhdllab.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@Documented
@ValidatorClass(ConsistentRangeConstraintValidator.class)
@Target( { TYPE })
@Retention(RUNTIME)
public @interface ConsistentRangeConstraint {
    String message() default "{validator.consistentRangeConstraint}";
}
