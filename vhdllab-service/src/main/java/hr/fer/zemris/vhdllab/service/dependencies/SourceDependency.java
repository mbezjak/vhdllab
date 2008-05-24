package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.util.VhdlUtil;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * A dependency extractor for a {@link FileTypes#VHDL_SOURCE} file type.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class SourceDependency implements DependencyExtractor {

    // constants used in parsing
    private static final String ENTITY = "ENTITY";
    private static final String WORK = "WORK";
    private static final String COMPONENT = "COMPONENT";
    private final static String WHITESPACE = " ";
    private final static String DOT = ".";
    private final static String SEMICOLON = ";";

    /*
     * (non-Javadoc)
     *
     * @see hr.fer.zemris.vhdllab.service.DependencyExtractor#execute(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public Set<String> execute(File file) throws ServiceException {
        String source = file.getContent();
        source = VhdlUtil.decomment(source);
        source = VhdlUtil.removeWhiteSpaces(source);
        return extract(source);
    }

    private Set<String> extract(String original) {
        Set<String> dependencies = new HashSet<String>();
        Set<String> uppercaseDependencies = new HashSet<String>();
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
            String component = original.substring(start, pos);
            if (!uppercaseDependencies.contains(component
                    .toUpperCase(Locale.ENGLISH))) {
                dependencies.add(component);
                uppercaseDependencies
                        .add(component.toUpperCase(Locale.ENGLISH));
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
            String component = original.substring(start, pos);
            if (!uppercaseDependencies.contains(component
                    .toUpperCase(Locale.ENGLISH))) {
                dependencies.add(component);
                uppercaseDependencies
                        .add(component.toUpperCase(Locale.ENGLISH));
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
