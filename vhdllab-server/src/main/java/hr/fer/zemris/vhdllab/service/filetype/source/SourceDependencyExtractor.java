package hr.fer.zemris.vhdllab.service.filetype.source;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

public class SourceDependencyExtractor implements DependencyExtractor {

    // constants used in parsing
    private static final String ENTITY = "ENTITY";
    private static final String WORK = "WORK";
    private static final String COMPONENT = "COMPONENT";
    private final static String WHITESPACE = " ";
    private final static String DOT = ".";
    private final static String SEMICOLON = ";";

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        String source = file.getData();
        source = VhdlUtil.decomment(source);
        source = VhdlUtil.removeWhiteSpaces(source);
        return extract(source);
    }

    private Set<Caseless> extract(String original) {
        Set<Caseless> dependencies = new HashSet<Caseless>();
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
            Caseless component = new Caseless(original.substring(start, pos));
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
            Caseless component = new Caseless(original.substring(start, pos));
            if (!dependencies.contains(component)) {
                dependencies.add(component);
            }
        }
        return dependencies;
    }

    private int maybeNext(String source, String keyword, int offset) {
        int nextPosition = offset;
        if (source.startsWith(keyword, offset)) {
            nextPosition += keyword.length();
        }
        return nextPosition;
    }

}
