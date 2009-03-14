package hr.fer.zemris.vhdllab.entity.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.ClientLog;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class NameFormatConstraintValidatorTest {

    private final Validator<?> validator = new NameFormatConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValidProjectTypeUserNameCorrect() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        project.setName("correct_name_format");
        assertTrue("not valid when correct name format.", validator
                .isValid(project));

        project.setName("a");
        assertTrue("one letter.", validator.isValid(project));

        project.setName("a_tb");
        assertTrue("one letter testbench name.", validator.isValid(project));

        project.setName("circuitAND");
        assertTrue("all letters.", validator.isValid(project));

        project.setName("circuit_AND");
        assertTrue("with underscore.", validator.isValid(project));

        project.setName("circuit_and");
        assertTrue("all lowercase.", validator.isValid(project));

        project.setName("CIRCUIT_AND");
        assertTrue("all uppercase.", validator.isValid(project));

        project.setName("circuitAND4");
        assertTrue("with number.", validator.isValid(project));

        project.setName("circuit_AND_4");
        assertTrue("with underscore and number.", validator.isValid(project));

        project.setName("circuit_AND_41");
        assertTrue("double number.", validator.isValid(project));

        project.setName("circuit_AND_4_1");
        assertTrue("double number, underscore separated.", validator
                .isValid(project));

        project.setName("circuit4AND");
        assertTrue("number in the middle.", validator.isValid(project));
    }

    @Test
    public void isValidProjectTypeUserNameIncorrect() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        project.setName("");
        assertFalse("valid with empty name.", validator.isValid(project));

        project.setName("$circuitAND");
        assertFalse("starts with illegal character.", validator
                .isValid(project));

        project.setName("circuit!AND");
        assertFalse("contains illegal character.", validator.isValid(project));

        project.setName("_circuitAND");
        assertFalse("starts with an underscore.", validator.isValid(project));

        project.setName("1circuitAND");
        assertFalse("starts with a number.", validator.isValid(project));

        project.setName("circuit__AND");
        assertFalse("double underscore.", validator.isValid(project));

        project.setName("circuitAND_");
        assertFalse("ends with an underscore.", validator.isValid(project));

        project.setName("circuit_š_AND");
        assertFalse("not character from English alphabet.", validator
                .isValid(project));
    }

    @Test
    public void isValidProjectTypeUserNameNotSuitable() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        project.setName("xor");
        assertFalse("xor - reserved keyword.", validator.isValid(project));

        project.setName("architecture");
        assertFalse("architecture - reserved keyword.", validator
                .isValid(project));

        project.setName("end");
        assertFalse("end - reserved keyword.", validator.isValid(project));

        project.setName("if");
        assertFalse("if - flow control keyword.", validator.isValid(project));

        project.setName("std_logic");
        assertFalse("std_logic - reserved vhdl type.", validator
                .isValid(project));
    }

    @Test
    public void isValidProjectTypePreferences() {
        Project project = new Project();
        project.setType(ProjectType.PREFERENCES);
        project.setName("_incorrect_name_format");
        assertTrue(
                "project type preferences must always have correct name format.",
                validator.isValid(project));
    }

    @Test
    public void isValidProjectTypeUserFileNameCorrect() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        File file = new File();
        file.setName("correct_file_name");
        file.setProject(project);
        assertTrue("not valid with project type=user and name correct.",
                validator.isValid(file));
    }

    @Test
    public void isValidProjectTypeUserFileNameInCorrect() {
        Project project = new Project();
        project.setType(ProjectType.USER);
        File file = new File();
        file.setName("_incorrect_file_name");
        file.setProject(project);
        assertFalse("valid with project type=user and name incorrect.",
                validator.isValid(file));
    }

    @Test
    public void isValidProjectTypePreferencesForFile() {
        Project project = new Project();
        project.setType(ProjectType.PREFERENCES);
        File file = new File();
        file.setName("_incorrect_file_name");
        file.setProject(project);
        assertTrue(
                "file that belongs to project with type=preferences must always have correct name format.",
                validator.isValid(file));
    }

    @Test
    public void isValidClientLog() {
        assertTrue("client log must always have correct name format.",
                validator.isValid(new ClientLog("user identifier")));
    }

    @Test
    public void isValidNamedObject() {
        assertTrue("named object not valid with correct name format.",
                validator.isValid(new NamedObject("correct_name_format")));
    }

    @Test
    public void isValidNamedObjectNameIncorrect() {
        assertFalse("named object valid with incorrect name format.", validator
                .isValid(new NamedObject("_incorrect_name_format")));
    }

    public class NamedObject {
        private String name;

        public NamedObject(String name) {
            setName(name);
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

}
