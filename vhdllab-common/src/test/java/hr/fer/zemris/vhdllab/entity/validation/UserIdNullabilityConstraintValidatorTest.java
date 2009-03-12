package hr.fer.zemris.vhdllab.entity.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectInfo;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class UserIdNullabilityConstraintValidatorTest {

    private final Validator<?> validator = new UserIdNullabilityConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValidUserIdSetAndProjectTypeUser() {
        Project project = new Project("userId");
        project.setType(ProjectType.USER);
        assertTrue("not valid when user id set and project type=user.",
                validator.isValid(project));
    }

    @Test
    public void isValidUserIdSetAndProjectTypePredefined() {
        Project project = new Project("userId");
        project.setType(ProjectType.PREDEFINED);
        assertFalse("valid when user id set and project type=predefined.",
                validator.isValid(project));
    }

    @Test
    public void isValidUserIdSetAndProjectTypePreferences() {
        Project project = new Project("userId");
        project.setType(ProjectType.PREDEFINED);
        assertFalse("valid when user id set and project type=preferences.",
                validator.isValid(project));
    }

    @Test
    public void isValidUserIdNotSetAndProjectTypePredefined() {
        Project project = new Project();
        project.setUserId(null);
        project.setType(ProjectType.PREDEFINED);
        assertTrue(
                "not valid when user id not set and project type=predefined.",
                validator.isValid(project));
    }

    /**
     * Always valid for ProjectInfo object.
     */
    @Test
    public void isValidFileInfo() {
        assertTrue(validator.isValid(new ProjectInfo()));
    }
    
}
