package hr.fer.zemris.vhdllab.service.extractors;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.StatusCodes;
import hr.fer.zemris.vhdllab.api.util.StringFormat;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.util.VhdlUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A extractor for a {@link FileTypes#VHDL_SOURCE} file type.
 *
 * @author marcupic
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class SourceExtractor implements CircuitInterfaceExtractor {

    // constants used in parsing
    private final static char[] IS = "IS".toCharArray();
    private final static char[] GENERIC = "GENERIC".toCharArray();
    private final static char[] PORT = "PORT".toCharArray();
    private final static char[] IN = "IN".toCharArray();
    private final static char[] OUT = "OUT".toCharArray();
    private final static char[] INOUT = "INOUT".toCharArray();
    private final static char[] BUFFER = "BUFFER".toCharArray();
    private final static char[] COLON = ":".toCharArray();
    private final static char[] WHITESPACE = " ".toCharArray();
    private final static char[] COMMA = ",".toCharArray();
    private final static char[] RIGHT_BRACKET = ")".toCharArray();
    private static final String ENTITY = "ENTITY";

    private final static char[][] COLON_WHITESPACE_COMMON_RIGHT_BRACKET = new char[][] {
            COLON, WHITESPACE, COMMA, RIGHT_BRACKET };
    private final static char[][] IN_OUT_INOUT_BUFFER = new char[][] { IN, OUT,
            INOUT, BUFFER };

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor#extractCircuitInterface(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws ServiceException {
        String source = file.getContent();
        source = VhdlUtil.decomment(source);
        source = VhdlUtil.removeWhiteSpaces(source);
        return extract(source);
    }

    private CircuitInterface extract(String original) throws ServiceException {
        List<Port> ports = new ArrayList<Port>();
        String source = original.toUpperCase();
        char[] chs = source.toCharArray();
        int pos = source.indexOf(ENTITY);
        if (pos == -1) {
            throwException();
        }
        pos += 7;
        int start = pos;
        while (chs[pos] != ' ') {
            pos++;
        }
        String entityName = new String(chs, start, pos - start);
        if (!StringFormat.isCorrectEntityName(entityName)) {
            throwException("Illegal entity name");
        }
        pos++;
        if (!startsWith(chs, pos, IS) || chs[pos + IS.length] != ' ') {
            throwException();
        }
        pos += 3;
        if (startsWith(chs, pos, GENERIC)
                && (chs[pos + GENERIC.length] == ' ' || chs[pos
                        + GENERIC.length] == '(')) {
            if (chs[pos + GENERIC.length] == ' ')
                pos++;
            pos += GENERIC.length;
            if (chs[pos] != '(') {
                throwException();
            }
            pos++;
            if (chs[pos] == ' ')
                pos++;
            int[] res = new int[2];
            while (true) {
                List<String> nazivi = new ArrayList<String>();
                while (true) {
                    findEnd(res, chs, pos,
                            COLON_WHITESPACE_COMMON_RIGHT_BRACKET);
                    if (res[0] == -1) {
                        throwException();
                    }
                    if (res[0] == 0) {
                        // dvotocka:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ' ')
                            pos++;
                        break;
                    }
                    if (res[0] == 1) {
                        // razmak:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ':') {
                            pos++;
                            if (chs[pos] == ' ')
                                pos++;
                            break;
                        }
                        if (chs[pos] == ',') {
                            pos++;
                            if (chs[pos] == ' ')
                                pos++;
                            continue;
                        }
                        throw new NullPointerException();
                        // return null;

                    }
                    if (res[0] == 2) {
                        // zarez:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ' ')
                            pos++;
                        continue;
                    }
                    if (res[0] == 3)
                        break;
                }
                if (res[0] == 3) {
                    if (nazivi.isEmpty())
                        break;
                    throw new NullPointerException();
                    // return null;

                }
                start = pos;
                pos = findTypeEnd(chs, pos);
                String type_name = new String(chs, start, pos - start);
                String initializer_value = null;
                if (chs[pos] == ' ')
                    pos++;
                if (chs[pos] == ':') {
                    pos++;
                    if (chs[pos] != '=') {
                        throw new NullPointerException();
                        // return null; // mora doci :=
                    }
                    pos++;
                    if (chs[pos] == ' ')
                        pos++;
                    start = pos;
                    pos = findInitializerEnd(chs, pos);
                    initializer_value = new String(chs, start, pos - start);

                }
                for (String naziv : nazivi) {
                    // naziv, type_name, initializer_value -> generic entry
                    // currently ignored
                }
                if (chs[pos] == ' ')
                    pos++;
                if (chs[pos] != ';') {
                    break;
                }
                pos++;
                if (chs[pos] == ' ')
                    pos++;
            }
            if (chs[pos] != ')') {
                throw new NullPointerException();
                // return null;
            }
            pos++;
            if (chs[pos] == ' ')
                pos++;
            if (chs[pos] != ';') {
                throw new NullPointerException();
                // return null;
            }
            pos++;
            if (chs[pos] == ' ')
                pos++;
        }

        if (startsWith(chs, pos, PORT)) {
            pos += PORT.length;
            if (chs[pos] == ' ')
                pos++;
            if (chs[pos] != '(') {
                throw new NullPointerException();
                // return null;
            }
            pos++;
            if (chs[pos] == ' ')
                pos++;
            int[] res = new int[2];
            while (true) {
                List<String> nazivi = new ArrayList<String>();
                while (true) {
                    findEnd(res, chs, pos,
                            COLON_WHITESPACE_COMMON_RIGHT_BRACKET);
                    if (res[0] == -1) {
                        throw new NullPointerException();
                        // return null;

                    }
                    if (res[0] == 0) {
                        // dvotocka:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ' ')
                            pos++;
                        break;
                    }
                    if (res[0] == 1) {
                        // razmak:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ':') {
                            pos++;
                            if (chs[pos] == ' ')
                                pos++;
                            break;
                        }
                        if (chs[pos] == ',') {
                            pos++;
                            if (chs[pos] == ' ')
                                pos++;
                            continue;
                        }
                        throw new NullPointerException();
                        // return null;

                    }
                    if (res[0] == 2) {
                        // zarez:
                        nazivi.add(new String(chs, pos, res[1] - pos));
                        pos = res[1] + 1;
                        if (chs[pos] == ' ')
                            pos++;
                        continue;
                    }
                    if (res[0] == 3)
                        break;
                }
                if (res[0] == 3) {
                    if (nazivi.isEmpty())
                        break;
                    throw new NullPointerException();
                    // return null;

                }
                String direction = null;
                int w = whichStarts(chs, pos, IN_OUT_INOUT_BUFFER);
                if (w != -1) {
                    pos += IN_OUT_INOUT_BUFFER[w].length;
                    if (chs[pos] == ' ')
                        pos++;
                    direction = new String(IN_OUT_INOUT_BUFFER[w]);
                } else {
                    direction = new String(IN);
                }
                start = pos;
                pos = findTypeEnd(chs, pos);
                String type_name = new String(chs, start, pos - start).trim();
                // String initializer_value = null;
                if (chs[pos] == ' ')
                    pos++;
                if (chs[pos] == ':') {
                    pos++;
                    if (chs[pos] != '=') {
                        throw new NullPointerException();
                        // return null; // mora doci :=
                    }
                    pos++;
                    if (chs[pos] == ' ')
                        pos++;
                    start = pos;
                    pos = findInitializerEnd(chs, pos);
                    // initializer_value = new String(chs,start,pos-start);

                }
                Range range = null;
                int x = type_name.indexOf('(');
                if (x > 0) {
                    String rangeText = type_name.substring(x);
                    type_name = type_name.substring(0, x).trim();
                    range = extractRange(rangeText);
                } else {
                    range = Range.SCALAR;
                }
                PortDirection dir = null;
                if(direction.equals("IN")) {
                    dir = PortDirection.IN;
                } else if (direction.equals("OUT")) {
                    dir = PortDirection.OUT;
                } else if (direction.equals("INOUT")) {
                    dir = PortDirection.INOUT;
                } else if (direction.equals("BUFFER")) {
                    dir = PortDirection.BUFFER;
                } else {
                    throwException();
                }
                TypeName typeName = null;
                if (type_name.equalsIgnoreCase("std_logic")) {
                    typeName = TypeName.STD_LOGIC;
                } else if (type_name.equalsIgnoreCase("std_logic_vector")) {
                    typeName = TypeName.STD_LOGIC_VECTOR;
                } else {
                    throwException();
                }
                for (String naziv : nazivi) {
                    Type type = new Type(typeName, range);
                    Port p = new Port(naziv, dir, type);
                    ports.add(p);
                }
                if (chs[pos] == ' ')
                    pos++;
                if (chs[pos] != ';') {
                    break;
                }
                pos++;
                if (chs[pos] == ' ')
                    pos++;
            }
            if (chs[pos] != ')') {
                throw new NullPointerException();
                // return null;

            }
            pos++;
            if (chs[pos] == ' ')
                pos++;
            if (chs[pos] != ';') {
                throw new NullPointerException();
                // return null;

            }
            pos++;
            if (chs[pos] == ' ')
                pos++;

        }

        return new CircuitInterface(entityName, ports);
    }

    /**
     * Use this method to extract range from string. This method support
     * following form of range:<br>
     * <code>(X TO Y)</code> or <code>(X DOWNTO Y)</code><br>
     * where <code>X</code> and <code>Y</code> can be numbers or parameter
     * names.
     *
     * @param rangeText
     *            string representaion of range
     * @return Range object representing given range, or <code>null</code> if
     *         error is encountered.
     */
    public static Range extractRange(String rangeText) {
        rangeText = rangeText.substring(1, rangeText.length() - 1).trim();
        int pos1 = rangeText.indexOf(' ');
        String from = rangeText.substring(0, pos1);
        rangeText = rangeText.substring(pos1 + 1);
        int pos2 = rangeText.indexOf(' ');
        String direction = rangeText.substring(0, pos2);
        String to = rangeText.substring(pos2 + 1);
        if (!direction.equals("TO") && !direction.equals("DOWNTO"))
            return null;
        VectorDirection dir;
        if(direction.equals("TO")) {
            dir = VectorDirection.TO;
        } else if (direction.equals("DOWNTO")) {
            dir = VectorDirection.DOWNTO;
        } else {
            return null;
        }
        return new Range(Integer.parseInt(from), dir, Integer.parseInt(to));
    }

    /**
     * This method finds position in given VHDL source represented as character
     * array, starting from given initial position, of character which
     * terminates signal initializer. This can be one of: ';', ')'. However,
     * character ')' is considered terminator only if it is not closure of some
     * '(' opened after the initial position.
     *
     * @param chs
     *            VHDL source represented as character array
     * @param pos
     *            initial position from where to start the search
     * @return position of character terminating signal initializer
     */
    private static int findInitializerEnd(char[] chs, int pos) {
        while (true) {
            if (chs[pos] == ';' || chs[pos] == ')')
                return pos;
            if (chs[pos] == '(') {
                int cnt = 1;
                for (pos = pos + 1; true; pos++) {
                    if (chs[pos] == '(')
                        cnt++;
                    if (chs[pos] == ')') {
                        cnt--;
                        if (cnt == 0) {
                            break;
                        }
                    }
                }
            }
            pos++;
        }
    }

    /**
     * This method finds position in given VHDL source represented as character
     * array, starting from given initial position, of character which
     * terminates signal type declaration. This can be one of: ':', ';', ')'.
     * However, character ')' is considered terminator only if it is not closure
     * of some '(' opened after the initial position.
     *
     * @param chs
     *            VHDL source represented as character array
     * @param pos
     *            initial position from where to start the search
     * @return position of character terminating signal type declaration
     */
    private static int findTypeEnd(char[] chs, int pos) {
        while (true) {
            if (chs[pos] == ':' || chs[pos] == ';' || chs[pos] == ')')
                return pos;
            if (chs[pos] == '(') {
                int cnt = 1;
                for (pos = pos + 1; true; pos++) {
                    if (chs[pos] == '(')
                        cnt++;
                    if (chs[pos] == ')') {
                        cnt--;
                        if (cnt == 0) {
                            break;
                        }
                    }
                }
            }
            pos++;
        }
    }

    /**
     * Use this method to find out which of character successions starts in
     * given array at given position. Candidates are given as last parameter. If
     * no candidate starts at given position, method will return -1.<br>
     * For example:<br>
     * <code>
     * char[] text = new char[] {'S','i','g','n','a','l',' ','x',' ',':',' ','I','N'};<br>
     * char[][] options = new char[][] {<br>
     *   new char[] {'O','U','T'},<br>
     *   new char[] {'I','N'},<br>
     *   new char[] {'B','U','F','F','E','R'}<br>
     * };<br>
     * int which = whichStarts(text,11,options);<br>
     * </code><br>
     * will result with <code>which = 1;</code>
     *
     * @param buf
     *            character array in which we want to perform search
     * @param pos
     *            position in buf from where the search will be performed
     * @param options
     *            zero or more character array which will be looked for.
     * @return index of the first array which is also found in buf at given
     *         position, or -1 if no such array exists.
     */
    private static int whichStarts(char[] buf, int pos, char[][] options) {
        for (int i = 0; i < options.length; i++) {
            if (startsWith(buf, pos, options[i]))
                return i;
        }
        return -1;
    }

    /**
     * Use this method to find where is the first occurrence of any of given
     * character successions (last parameter) in given array (first parameter)
     * starting from given initial position.
     *
     * @param res
     *            where to store result of find operation. This must be an array
     *            containing two elements. After the search is done, element
     *            with index 0 will be set to the index of option that was first
     *            found. Element with index 1 will be set to the position where
     *            this match starts.
     * @param buf
     *            where to find
     * @param pos
     *            from which position to start search
     * @param options
     *            an array of character successions which are valid candidates
     */
    private static void findEnd(int[] res, char[] buf, int pos, char[][] options) {
        while (pos < buf.length) {
            int w = whichStarts(buf, pos, options);
            if (w != -1) {
                res[0] = w;
                res[1] = pos;
                return;
            }
            pos++;
        }
        res[0] = -1;
    }

    /**
     * Returns <code>true</code> if character array on given position contains
     * given sub-array or <code>false</code> otherwise.
     *
     * @param buf
     *            original array on which check is performed
     * @param pos
     *            start position
     * @param option
     *            sub-array
     * @return <code>true</code> if sub-array is found in original array at
     *         given position, or <code>false</code> otherwise
     */
    private static boolean startsWith(char[] buf, int pos, char[] option) {
        if (pos + option.length > buf.length) {
            return false;
        }
        for (int i = 0; i < option.length; i++) {
            if (buf[pos + i] != option[i]) {
                return false;
            }
        }
        return true;
    }

    private void throwException() throws ServiceException {
        throwException("Entity block is invalid.");
    }

    private void throwException(String message) throws ServiceException {
        throw new ServiceException(StatusCodes.SERVICE_CANT_EXTRACT_CI, message);
    }

}
