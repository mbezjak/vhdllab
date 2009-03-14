package hr.fer.zemris.vhdllab.validation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

@Documented
@ValidatorClass(FileTypeNullabilityConstraintValidator.class)
@Target( { TYPE })
@Retention(RUNTIME)
public @interface FileTypeNullabilityConstraint {
    String message() default "{validator.fileTypeNullabilityConstraint}";
}
