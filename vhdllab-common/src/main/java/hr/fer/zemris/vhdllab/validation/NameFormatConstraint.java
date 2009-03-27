package hr.fer.zemris.vhdllab.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@Documented
@ValidatorClass(NameFormatConstraintValidator.class)
@Target( { TYPE, FIELD, METHOD })
@Retention(RUNTIME)
public @interface NameFormatConstraint {
    String message() default "{validator.nameFormatConstraint}";
}
