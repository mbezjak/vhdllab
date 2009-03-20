package hr.fer.zemris.vhdllab.service.filetype.source;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.VectorDirection;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.service.ci.PortDirection;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.impl.AbstractMetadataExtractionService;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SourceMetadataExtractionService extends
        AbstractMetadataExtractionService {

    /*
     * Notice: this class was written only to satisfy a test class so code is a
     * disaster. In future, this class should be implemented using lexical and
     * syntactic analysis.
     */

    // constants used in parsing
    private static final String ENTITY = "ENTITY";
    private static final String GENERIC = "GENERIC";
    private static final String END = "END";
    private final static String IS = "IS";
    private final static String PORT = "PORT";
    private final static String WHITESPACE = " ";
    private final static String DOT = ".";
    private final static String COLON = ":";
    private final static String SEMICOLON = ";";
    private final static String COMMA = ",";
    private final static String LEFT_BRACKET = "(";
    private final static String RIGHT_BRACKET = ")";
    private static final String WORK = "WORK";
    private static final String COMPONENT = "COMPONENT";

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        return extractCircuitInterface(file.getData());
    }

    public CircuitInterface extractCircuitInterface(String source) {
        String cleanedSource = source;
        cleanedSource = VhdlUtil.decomment(cleanedSource);
        cleanedSource = VhdlUtil.removeWhiteSpaces(cleanedSource);
        return extractCi(cleanedSource);
    }

    private int next(String source, String keyword, int offset) {
        if (!source.startsWith(keyword, offset)) {
            throwException();
        }
        return offset + keyword.length();
    }

    private int maybeNext(String source, String keyword, int offset) {
        int nextPosition = offset;
        if (source.startsWith(keyword, offset)) {
            nextPosition += keyword.length();
        }
        return nextPosition;
    }

    private int parsePorts(List<Port> ports, String source, String original,
            int pos) {
        int start = pos;
        boolean exit = false;
        while (true) {
            int end = source.indexOf(SEMICOLON, start);
            if (end == -1 || end == start) {
                throwException();
            }
            int end2 = maybeNext(source, WHITESPACE, end + SEMICOLON.length());
            if (source.startsWith(END, end2)
                    || source.startsWith(GENERIC, end2)) {
                if (source.startsWith(RIGHT_BRACKET + WHITESPACE, end - 2)) {
                    end -= 2;
                } else if (source.startsWith(RIGHT_BRACKET, end - 1)) {
                    end -= 1;
                }
                exit = true;
            }
            parsePort(ports, source.substring(start, end), original.substring(
                    start, end));
            if (exit) {
                return end;
            }
            start = end + 1;
        }
    }

    private void parsePort(List<Port> ports, String source, String original) {
        int pos = source.indexOf(COLON);
        if (pos == -1) {
            throwException();
        }
        String portNames = original.substring(0, pos).trim();
        pos += COLON.length();
        int start = maybeNext(source, WHITESPACE, pos);
        int end = source.indexOf(WHITESPACE, start);
        if (end == -1) {
            throwException();
        }
        String direction = source.substring(start, end).trim();
        PortDirection portDirection = null;
        try {
            portDirection = PortDirection.valueOf(direction);
        } catch (IllegalArgumentException e) {
            throwException();
        }
        end += WHITESPACE.length();
        Port type = new Port();
        parseType(source.substring(end), type);
        for (String n : portNames.split(COMMA)) {
            n = n.trim();
            Port port = new Port(type);
            port.setDirection(portDirection);
            port.setName(n);
            ports.add(port);
        }
    }

    private void parseType(String source, Port p) {
        String s = source.trim();
        if (s.equals("STD_LOGIC")) {
            return;
        }
        if (!s.startsWith("STD_LOGIC_VECTOR")) {
            throwException();
        }
        int pos = "STD_LOGIC_VECTOR".length();
        pos = maybeNext(s, WHITESPACE, pos);
        int start = next(s, LEFT_BRACKET, pos);
        start = maybeNext(s, WHITESPACE, start);
        int end = s.indexOf(WHITESPACE, start);
        if (end == -1) {
            throwException();
        }
        String fromString = s.substring(start, end);
        end += WHITESPACE.length();
        int from = 0;
        try {
            from = Integer.parseInt(fromString);
        } catch (NumberFormatException e) {
            throwException();
        }
        start = end;
        end = s.indexOf(WHITESPACE, start);
        if (end == -1) {
            throwException();
        }
        String direction = s.substring(start, end);
        end += WHITESPACE.length();
        VectorDirection vectorDirection = null;
        try {
            vectorDirection = VectorDirection.valueOf(direction);
        } catch (IllegalArgumentException e) {
            throwException();
        }
        start = end;
        end = s.indexOf(RIGHT_BRACKET, start);
        String toString = s.substring(start, end).trim();
        end += RIGHT_BRACKET.length();
        int to = 0;
        try {
            to = Integer.parseInt(toString);
        } catch (NumberFormatException e) {
            throwException();
        }
        end = maybeNext(s, WHITESPACE, end);
        if (end != s.length()) {
            throwException();
        }
        p.setFrom(from);
        p.setTo(to);
    }

    private CircuitInterface extractCi(String original) {
        List<Port> ports = new ArrayList<Port>();
        String source = original.toUpperCase(Locale.ENGLISH);
        int pos = source.indexOf(ENTITY);
        if (pos == -1) {
            throwException();
        }
        pos = next(source, ENTITY, pos);
        pos = next(source, WHITESPACE, pos);
        int start = pos;
        int end = source.indexOf(WHITESPACE, start);
        String entityName = original.substring(start, end);
        pos = end + WHITESPACE.length();
        pos = next(source, IS, pos);
        pos = next(source, WHITESPACE, pos);
        if (source.startsWith(PORT, pos)) {
            pos = port(ports, source, original, pos);
            if (source.startsWith(GENERIC, pos)) {
                pos = generic(source, pos);
            }
        } else if (source.startsWith(GENERIC, pos)) {
            pos = generic(source, pos);
            if (source.startsWith(PORT, pos)) {
                pos = port(ports, source, original, pos);
            }
        }
        pos = next(source, END, pos);
        pos = next(source, WHITESPACE, pos);
        if (source.startsWith(ENTITY, pos)) {
            pos += ENTITY.length();
            pos = next(source, WHITESPACE, pos);
        }
        pos = next(source, entityName.toUpperCase(Locale.ENGLISH), pos);
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, SEMICOLON, pos);
        try {
            CircuitInterface ci = new CircuitInterface(entityName);
            ci.addAll(ports);
            return ci;
        } catch (IllegalArgumentException e) {
            throwException();
            return null;
        }
    }

    private int generic(String source, int p) {
        int pos = p + GENERIC.length();
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, LEFT_BRACKET, pos);
        pos = source.indexOf(RIGHT_BRACKET, pos);
        if (pos == -1) {
            throwException();
        }
        pos += RIGHT_BRACKET.length();
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, SEMICOLON, pos);
        pos = maybeNext(source, WHITESPACE, pos);
        return pos;
    }

    private int port(List<Port> ports, String source, String original, int p) {
        int pos = p + PORT.length();
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, LEFT_BRACKET, pos);
        pos = maybeNext(source, WHITESPACE, pos);
        pos = parsePorts(ports, source, original, pos);
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, RIGHT_BRACKET, pos);
        pos = maybeNext(source, WHITESPACE, pos);
        pos = next(source, SEMICOLON, pos);
        pos = maybeNext(source, WHITESPACE, pos);
        return pos;
    }

    private void throwException() {
        throwException("Entity block is invalid.");
    }

    private void throwException(String message) {
        throw new CircuitInterfaceExtractionException(message);
    }

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        String source = file.getData();
        source = VhdlUtil.decomment(source);
        source = VhdlUtil.removeWhiteSpaces(source);
        return extract(source);
    }

    private Set<String> extract(String original) {
        Set<String> dependencies = new HashSet<String>();
        String source = original.toUpperCase(Locale.ENGLISH);

        int pos = 0;
        while (true) {
            pos = source.indexOf(ENTITY, pos);
            if (pos == -1) {
                break;
            }
            pos += ENTITY.length();
            if (!source.startsWith(WHITESPACE, pos)) {
                continue;
            }
            pos += WHITESPACE.length();
            if (!source.startsWith(WORK, pos)) {
                continue;
            }
            pos += WORK.length();
            pos = maybeNext(source, WHITESPACE, pos);
            if (!source.startsWith(DOT, pos)) {
                continue;
            }
            pos += DOT.length();
            pos = maybeNext(source, WHITESPACE, pos);
            int start = pos;
            pos = source.indexOf(WHITESPACE, start);
            if (pos == -1) {
                pos = start;
                continue;
            }
            String component = original.substring(start, pos).toLowerCase();
            if (!dependencies.contains(component)) {
                dependencies.add(component);
            }
        }

        pos = 0;
        while (true) {
            pos = source.indexOf(COMPONENT, pos);
            if (pos == -1) {
                break;
            }
            pos += COMPONENT.length();
            if (!source.startsWith(WHITESPACE, pos)) {
                continue;
            }
            pos += WHITESPACE.length();
            if (source.startsWith(SEMICOLON, pos)) {
                continue;
            }
            int start = pos;
            pos = source.indexOf(WHITESPACE, start);
            if (pos == -1) {
                pos = start;
                continue;
            }
            String component = original.substring(start, pos).toLowerCase();
            if (!dependencies.contains(component)) {
                dependencies.add(component);
            }
        }
        return dependencies;
    }

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        return new Result(file.getData());
    }

}
