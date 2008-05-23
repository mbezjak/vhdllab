package hr.fer.zemris.vhdllab.api;

/**
 * Defines all file types that can be used (are recognized by server).
 *
 * @author Miro Bezjak
 * @since 6/2/2008
 * @version 1.0
 */
public final class FileTypes {

    /**
     * Don't let anyone instantiate this class.
     */
    private FileTypes() {
    }

    /**
     * A vhdl source code file type.
     */
    public static final String VHDL_SOURCE = "vhdl.source";
    /**
     * A vhdl predefined file type (every user can access predefined files).
     */
    public static final String VHDL_PREDEFINED = "vhdl.predefined";

    /**
     * A vhdl testbench file type.
     */
    public static final String VHDL_TESTBENCH = "vhdl.testbench";

    /**
     * A vhdl schema file type.
     */
    public static final String VHDL_SCHEMA = "vhdl.schema";

    /**
     * A vhdl automaton file type.
     */
    public static final String VHDL_AUTOMATON = "vhdl.automaton";

    /**
     * A user preferences file type.
     */
    public static final String PREFERENCES_USER = "preferences.user";

}
