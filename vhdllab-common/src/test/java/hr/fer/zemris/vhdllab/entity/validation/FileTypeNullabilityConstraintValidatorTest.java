package hr.fer.zemris.vhdllab.entity.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileInfo;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class FileTypeNullabilityConstraintValidatorTest {

    private final Validator<?> validator = new FileTypeNullabilityConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValidFileTypeSetAndProjectTypeUser() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        File file = new File();
        file.setType(FileType.SOURCE);
        file.setProject(project);
        assertTrue("not valid when file type set and project type=user.",
                validator.isValid(file));
    }

    @Test
    public void isValidFileTypeSetAndProjectTypePredefined() {
        Project project = new Project();
        project.setType(ProjectType.PREDEFINED);
        File file = new File();
        file.setType(FileType.SOURCE);
        file.setProject(project);
        assertTrue("not valid when file type set and project type=predefined.",
                validator.isValid(file));
    }

    @Test
    public void isValidFileTypeSetAndProjectTypePreferences() {
        Project project = new Project();
        project.setType(ProjectType.PREFERENCES);
        File file = new File();
        file.setType(FileType.SOURCE);
        file.setProject(project);
        assertFalse("valid when file type set and project type=preferences.",
                validator.isValid(file));
    }

    @Test
    public void isValidFileTypeNotSetAndProjectTypePreferences() {
        Project project = new Project();
        project.setType(ProjectType.PREFERENCES);
        File file = new File();
        file.setType(null);
        file.setProject(project);
        assertTrue(
                "not valid when file type not set and project type=preferences.",
                validator.isValid(file));
    }

    @Test
    public void isValidClientLog() {
        ClientLog log = new ClientLog();
        log.setType(null);
        assertTrue("not valid when file type is null.", validator.isValid(log));
    }

    @Test
    public void isValidClientLogFileTypeSet() {
        ClientLog log = new ClientLog();
        log.setType(FileType.SOURCE);
        assertFalse("valid when file type not null.", validator.isValid(log));
    }

    /**
     * Always valid for FileInfo object.
     */
    @Test
    public void isValidFileInfo() {
        assertTrue(validator.isValid(new FileInfo()));
    }

}
