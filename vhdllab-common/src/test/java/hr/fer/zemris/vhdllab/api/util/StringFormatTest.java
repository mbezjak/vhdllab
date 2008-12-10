package hr.fer.zemris.vhdllab.api.util;

import static hr.fer.zemris.vhdllab.api.util.StringFormat.isCorrectEntityName;
import static hr.fer.zemris.vhdllab.api.util.StringFormat.isCorrectFileName;
import static hr.fer.zemris.vhdllab.api.util.StringFormat.isCorrectPortName;
import static hr.fer.zemris.vhdllab.api.util.StringFormat.isCorrectProjectName;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;
import java.util.Set;

import org.junit.Test;

/**
 * A test case for {@link StringFormat} class.
 *
 * @author Miro Bezjak
 */
public class StringFormatTest {

    /**
     * Check initialization and that a file (NotValidVHDLNames.txt) is loaded.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void init() throws Exception {
        Field field = StringFormat.class.getDeclaredField("notValidVHDLNames");
        field.setAccessible(true);
        Set<String> names = (Set<String>) field.get(null);
        assertTrue("'illegal names' file not loaded.", names.contains("entity"));
        assertTrue("'illegal names' file not loaded.", names.contains("port"));
        assertTrue("'illegal names' file not loaded.", names.contains("if"));
    }

    /**
     * String is empty.
     */
    @Test(expected = NullPointerException.class)
    public void entityName() {
        isCorrectEntityName(null);
    }

    /**
     * Incorrect strings.
     */
    @Test
    public void entityName2() {
        assertFalse("empty string.", isCorrectEntityName(""));
        assertFalse("starts with illegal character.",
                isCorrectEntityName("$circuitAND"));
        assertFalse("ontains illegal character.",
                isCorrectEntityName("circuit!AND"));
        assertFalse("starts with an underscore.",
                isCorrectEntityName("_circuitAND"));
        assertFalse("starts with a number.", isCorrectEntityName("1circuitAND"));
        assertFalse("double underscore.", isCorrectEntityName("circuit__AND"));
        assertFalse("ends with an underscore.",
                isCorrectEntityName("circuitAND_"));
        assertFalse("not character from English alphabet.",
                isCorrectEntityName("circuit_š_AND"));
    }

    /**
     * Not suitable names.
     */
    @Test
    public void entityName3() {
        assertFalse("xor - reserved keyword.", isCorrectEntityName("xor"));
        assertFalse("architecture - reserved keyword.",
                isCorrectEntityName("architecture"));
        assertFalse("end - reserved keyword.", isCorrectEntityName("end"));
        assertFalse("if - flow control keyword.", isCorrectEntityName("if"));
        assertFalse("std_logic - reserved vhdl type.",
                isCorrectEntityName("std_logic"));

        // ... and many others
    }

    /**
     * Correct names.
     */
    @Test
    public void entityName4() {
        assertTrue("one letter.", isCorrectEntityName("a"));
        assertTrue("one letter testbench name.", isCorrectEntityName("a_tb"));
        assertTrue("all letters.", isCorrectEntityName("circuitAND"));
        assertTrue("with underscore.", isCorrectEntityName("circuit_AND"));
        assertTrue("all lowercase.", isCorrectEntityName("circuit_and"));
        assertTrue("all uppercase.", isCorrectEntityName("CIRCUIT_AND"));
        assertTrue("with number.", isCorrectEntityName("circuitAND4"));
        assertTrue("with underscore and number.",
                isCorrectEntityName("circuit_AND_4"));
        assertTrue("double number.", isCorrectProjectName("circuit_AND_41"));
        assertTrue("double number, underscore separated.",
                isCorrectEntityName("circuit_AND_4_1"));
        assertTrue("number in the middle.", isCorrectEntityName("circuit4AND"));
    }

    /**
     * String is empty.
     */
    @Test(expected = NullPointerException.class)
    public void portName() {
        isCorrectPortName(null);
    }

    /**
     * Incorrect strings.
     */
    @Test
    public void portName2() {
        assertFalse("empty string.", isCorrectPortName(""));
        assertFalse("starts with illegal character.",
                isCorrectPortName("$portIN"));
        assertFalse("ontains illegal character.", isCorrectPortName("port!IN"));
        assertFalse("starts with an underscore.", isCorrectPortName("_portIN"));
        assertFalse("starts with a number.", isCorrectPortName("1portIN"));
        assertFalse("double underscore.", isCorrectPortName("port__IN"));
        assertFalse("ends with an underscore.", isCorrectPortName("portIN_"));
        assertFalse("not character from English alphabet.",
                isCorrectPortName("port_š_IN"));
    }

    /**
     * Not suitable names.
     */
    @Test
    public void portName3() {
        assertFalse("xor - reserved keyword.", isCorrectPortName("xor"));
        assertFalse("architecture - reserved keyword.",
                isCorrectPortName("architecture"));
        assertFalse("end - reserved keyword.", isCorrectPortName("end"));
        assertFalse("if - flow control keyword.", isCorrectPortName("if"));
        assertFalse("std_logic - reserved vhdl type.",
                isCorrectPortName("std_logic"));

        // ... and many others
    }

    /**
     * Correct names.
     */
    @Test
    public void portName4() {
        assertTrue("one letter.", isCorrectPortName("a"));
        assertTrue("all letters.", isCorrectPortName("portIN"));
        assertTrue("with underscore.", isCorrectPortName("port_IN"));
        assertTrue("all lowercase.", isCorrectPortName("port_in"));
        assertTrue("all uppercase.", isCorrectPortName("PORT_IN"));
        assertTrue("with number.", isCorrectPortName("portIN4"));
        assertTrue("with underscore and number.",
                isCorrectPortName("port_IN_4"));
        assertTrue("double number.", isCorrectProjectName("port_IN_41"));
        assertTrue("double number, underscore separated.",
                isCorrectEntityName("port_IN_4_1"));
        assertTrue("number in the middle.", isCorrectPortName("port4IN"));
    }

    /**
     * String is empty.
     */
    @Test(expected = NullPointerException.class)
    public void fileName() {
        isCorrectFileName(null);
    }

    /**
     * Incorrect strings.
     */
    @Test
    public void fileName2() {
        assertFalse("empty string.", isCorrectFileName(""));
        assertFalse("starts with illegal character.",
                isCorrectFileName("$fileXOR"));
        assertFalse("ontains illegal character.", isCorrectFileName("file!XOR"));
        assertFalse("starts with an underscore.", isCorrectFileName("_fileXOR"));
        assertFalse("starts with a number.", isCorrectFileName("1fileXOR"));
        assertFalse("double underscore.", isCorrectFileName("file__XOR"));
        assertFalse("ends with an underscore.", isCorrectFileName("fileXOR_"));
        assertFalse("not character from English alphabet.",
                isCorrectFileName("file_š_XOR"));
    }

    /**
     * Not suitable names.
     */
    @Test
    public void fileName3() {
        assertFalse("xor - reserved keyword.", isCorrectFileName("xor"));
        assertFalse("architecture - reserved keyword.",
                isCorrectFileName("architecture"));
        assertFalse("end - reserved keyword.", isCorrectFileName("end"));
        assertFalse("if - flow control keyword.", isCorrectFileName("if"));
        assertFalse("std_logic - reserved vhdl type.",
                isCorrectFileName("std_logic"));

        // ... and many others
    }

    /**
     * Correct names.
     */
    @Test
    public void fileName4() {
        assertTrue("one letter.", isCorrectFileName("a"));
        assertTrue("all letters.", isCorrectFileName("fileXOR"));
        assertTrue("with underscore.", isCorrectFileName("file_XOR"));
        assertTrue("all lowercase.", isCorrectFileName("file_xor"));
        assertTrue("all uppercase.", isCorrectFileName("FILE_XOR"));
        assertTrue("with number.", isCorrectFileName("fileXOR4"));
        assertTrue("with underscore and number.",
                isCorrectFileName("file_XOR_4"));
        assertTrue("double number.", isCorrectProjectName("file_XOR_41"));
        assertTrue("double number, underscore separated.",
                isCorrectEntityName("file_XOR_4_1"));
        assertTrue("number in the middle.", isCorrectFileName("file4XOR"));
    }

    /**
     * String is empty.
     */
    @Test(expected = NullPointerException.class)
    public void projectName() {
        isCorrectProjectName(null);
    }

    /**
     * Incorrect strings.
     */
    @Test
    public void projectName2() {
        assertFalse("empty string.", isCorrectProjectName(""));
        assertFalse("starts with illegal character.",
                isCorrectProjectName("$projectMUX"));
        assertFalse("ontains illegal character.",
                isCorrectProjectName("project!MUX"));
        assertFalse("starts with an underscore.",
                isCorrectProjectName("_projectMUX"));
        assertFalse("starts with a number.",
                isCorrectProjectName("1projectMUX"));
        assertFalse("double underscore.", isCorrectProjectName("project__MUX"));
        assertFalse("ends with an underscore.",
                isCorrectProjectName("projectMUX_"));
        assertFalse("not character from English alphabet.",
                isCorrectProjectName("project_š_MUX"));
    }

    /**
     * Correct names.
     */
    @Test
    public void projectName3() {
        assertTrue("one letter.", isCorrectProjectName("a"));
        assertTrue("all letters.", isCorrectProjectName("projectMUX"));
        assertTrue("with underscore.", isCorrectProjectName("project_MUX"));
        assertTrue("all lowercase.", isCorrectProjectName("project_mux"));
        assertTrue("all uppercase.", isCorrectProjectName("PROJECT_MUX"));
        assertTrue("with number.", isCorrectProjectName("projectMUX4"));
        assertTrue("with underscore and number.",
                isCorrectProjectName("project_MUX_4"));
        assertTrue("double number.", isCorrectProjectName("project_MUX_41"));
        assertTrue("double number, underscore separated.",
                isCorrectProjectName("project_MUX_4_1"));
        assertTrue("number in the middle.", isCorrectProjectName("project4MUX"));
    }

}
