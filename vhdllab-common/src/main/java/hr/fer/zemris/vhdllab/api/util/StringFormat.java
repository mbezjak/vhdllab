package hr.fer.zemris.vhdllab.api.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Helper class for checking if strings are of right format.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class StringFormat {

    /**
     * A name of a file that contains strings not suitable to represent VHDL
     * entity name.
     */
    private static final String NOT_VALID_FILE = "NotValidVHDLNames.txt";
    /**
     * Encoding of a {@link #NOT_VALID_FILE}.
     */
    private static final String NOT_VALID_FILE_ENCODING = "UTF-8";

    /**
     * A set of strings not suitable to represent VHDL entity name.
     */
    private static final Set<String> notValidVHDLNames;

    private static final String BASIC_NAME_PATTERN = "\\p{Alpha}\\p{Alnum}*(_\\p{Alnum}+)*";

    static {
        // initial capacities are only an estimate
        notValidVHDLNames = new HashSet<String>(50);
        InputStream is = StringFormat.class.getClassLoader()
                .getResourceAsStream(NOT_VALID_FILE);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(is,
                    NOT_VALID_FILE_ENCODING));
            String s;
            while ((s = reader.readLine()) != null) {
                s = s.trim();
                if (s.equals("") || s.startsWith("#")) {
                    continue;
                }
                notValidVHDLNames.add(s.toLowerCase());
            }
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ignored) {
                }
            } else if (is != null) {
                try {
                    is.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

    /**
     * Don't let anyone instantiate this class.
     */
    private StringFormat() {
    }

    /**
     * Ignores case and checks if <code>s</code> is a basic name. Correct
     * basic name is a string with the following format:
     * <ul>
     * <li>it must contain only alpha (only letters of English alphabet),
     * numeric (digits 0 to 9) or underscore (_) characters</li>
     * <li>it must not start with a non-alpha character</li>
     * <li>it must not end with an underscore character</li>
     * <li>it must not contain a tandem of underscore characters</li>
     * </ul>
     *
     * @param s
     *            a string that will be checked
     * @return <code>true</code> if <code>s</code> is a basic name;
     *         <code>false</code> otherwise
     */
    private static boolean isBasicName(String s) {
        return Pattern.matches(BASIC_NAME_PATTERN, s);
    }

    /**
     * Ignores case and checks if <code>s</code> is a correct entity name.
     * Correct entity name is a string with the following format:
     * <ul>
     * <li>it must contain only alpha (only letters of English alphabet),
     * numeric (digits 0 to 9) or underscore (_) characters</li>
     * <li>it must not start with a non-alpha character</li>
     * <li>it must not end with an underscore character</li>
     * <li>it must not contain a tandem of underscore characters</li>
     * <li>it must not be a reserved word (check at
     * hr.fer.zemris.vhdllab.utilities.NotValidVHDLNames.txt)</li>
     * </ul>
     *
     * @param s
     *            a string that will be checked
     * @return <code>true</code> if <code>s</code> is a correct name;
     *         <code>false</code> otherwise
     */
    public static boolean isCorrectEntityName(String s) {
        return isBasicName(s) && !notValidVHDLNames.contains(s.toLowerCase());
    }

    /**
     * Ignores case and checks if <code>s</code> is a correct port name. A
     * correct port name format is the same as
     * {@link #isCorrectEntityName(String)}.
     *
     * @param s
     *            a string that will be checked
     * @return <code>true</code> if <code>s</code> is a correct port name;
     *         <code>false</code> otherwise
     */
    public static boolean isCorrectPortName(String s) {
        return isCorrectEntityName(s);
    }

    /**
     * Ignores case and checks if <code>s</code> is a correct file name. A
     * correct file name format is the same as
     * {@link #isCorrectEntityName(String)}.
     *
     * @param s
     *            a string that will be checked
     * @return <code>true</code> if <code>s</code> is a correct file name;
     *         <code>false</code> otherwise
     */
    public static boolean isCorrectFileName(String s) {
        return isCorrectEntityName(s);
    }

    /**
     * Ignores case and checks if <code>s</code> is a correct project name.
     * Correct entity name is a string with the following format:
     * <ul>
     * <li>it must contain only alpha (only letters of English alphabet),
     * numeric (digits 0 to 9) or underscore (_) characters</li>
     * <li>it must not start with a non-alpha character</li>
     * <li>it must not end with an underscore character</li>
     * <li>it must not contain a tandem of underscore characters</li>
     * </ul>
     *
     * @param s
     *            a string that will be checked
     * @return <code>true</code> if <code>s</code> is a correct project
     *         name; <code>false</code> otherwise
     */
    public static boolean isCorrectProjectName(String s) {
        return isBasicName(s);
    }

}