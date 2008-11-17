package hr.fer.zemris.vhdllab.service.filetype.source;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.service.filetype.source.VhdlUtil;

import org.junit.Test;

/**
 * A test case for {@link VhdlUtil} class.
 *
 * @author Miro Bezjak
 */
public class VhdlUtilTest {

    /**
     * A comment for vhdl code.
     */
    private static final String COMMENT = "-- a vhdl code comment; end entity;";
    /**
     * A comment that stretches throughout the whole line.
     */
    private static final String COMMENT_LINE = COMMENT + "\n";
    /**
     * A vhdl code snippet.
     */
    private static final String CODE = "entity circuitAND\n\tis port;\na:= b-3;\n";
    /**
     * Another vhdl code snippet.
     */
    private static final String CODE2 = "architecture\t  arch of  \r\n    circuitAND\n\t\t\t;";

    /**
     * Source is empty.
     */
    @Test
    public void decomment() {
        assertEquals("empty string.", "", VhdlUtil.decomment(""));
    }

    /**
     * Source is one commented line.
     */
    @Test
    public void decomment2() {
        assertEquals("single comment.", "", VhdlUtil.decomment(COMMENT));
    }

    /**
     * Source is one commented line and a new line.
     */
    @Test
    public void decomment3() {
        assertEquals("one comment line.", "", VhdlUtil.decomment(COMMENT_LINE));
    }

    /**
     * Source is one commented line and 2 new lines.
     */
    @Test
    public void decomment4() {
        assertEquals("comment line then 2 new lines.", "\n", VhdlUtil
                .decomment(COMMENT_LINE + "\n"));
    }

    /**
     * Comment first then rest of the code.
     */
    @Test
    public void decomment5() {
        assertEquals("comment line then code.", CODE, VhdlUtil
                .decomment(COMMENT_LINE + CODE));
    }

    /**
     * Comment-code-comment.
     */
    @Test
    public void decomment6() {
        assertEquals("comment-code-comment.", CODE, VhdlUtil
                .decomment(COMMENT_LINE + CODE + COMMENT_LINE));
    }

    /**
     * Code then comment.
     */
    @Test
    public void decomment7() {
        assertEquals("code then comment.", CODE, VhdlUtil.decomment(CODE
                + COMMENT_LINE));
    }

    /**
     * Code then multiple comments.
     */
    @Test
    public void decomment8() {
        assertEquals("code then multiple comments.", CODE, VhdlUtil
                .decomment(CODE + COMMENT_LINE + COMMENT));
    }

    /**
     * Code-comment-code-comment.
     */
    @Test
    public void decomment9() {
        assertEquals("code-comment-code-comment.", CODE + CODE2, VhdlUtil
                .decomment(CODE + COMMENT_LINE + CODE2 + COMMENT));
    }

    /**
     * Source is empty.
     */
    @Test
    public void removeWhiteSpaces() {
        assertEquals("empty string.", "", VhdlUtil.removeWhiteSpaces(""));
    }

    /**
     * Source is all whitespace.
     */
    @Test
    public void removeWhiteSpaces2() {
        assertEquals("all whitespace.", "", VhdlUtil
                .removeWhiteSpaces(" \t\r\n"));
    }

    /**
     * Source is filled with whitespaces.
     */
    @Test
    public void removeWhiteSpaces3() {
        assertEquals("complex source.", "architecture arch of circuitAND ;",
                VhdlUtil.removeWhiteSpaces(CODE2));
    }

    /**
     * Whitespaces at the end are erased.
     */
    @Test
    public void removeWhiteSpaces4() {
        assertEquals("whitespace at the end.", "entity circuit;", VhdlUtil
                .removeWhiteSpaces("entity\t\tcircuit;\r\n"));
    }

    /**
     * Whitespaces at the beginning are erased.
     */
    @Test
    public void removeWhiteSpaces5() {
        assertEquals("whitespace at the beginning.", "entity circuit;",
                VhdlUtil.removeWhiteSpaces("\r\nentity circuit;"));
    }

}
