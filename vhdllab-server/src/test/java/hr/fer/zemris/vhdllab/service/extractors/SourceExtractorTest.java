package hr.fer.zemris.vhdllab.service.extractors;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.api.vhdl.Port;
import hr.fer.zemris.vhdllab.api.vhdl.PortDirection;
import hr.fer.zemris.vhdllab.api.vhdl.Range;
import hr.fer.zemris.vhdllab.api.vhdl.Type;
import hr.fer.zemris.vhdllab.api.vhdl.TypeName;
import hr.fer.zemris.vhdllab.api.vhdl.VectorDirection;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link SourceExtractor}.
 *
 * @author Miro Bezjak
 */
public class SourceExtractorTest {

    private static CircuitInterfaceExtractor extractor;
    private static File file;

    @BeforeClass
    public static void initClass() {
        extractor = new SourceExtractor();
        Project project = new Project("user.id", "project_name");
        file = new File(project, "file_name", FileTypes.VHDL_SOURCE);
    }

    /**
     * Comments are ignored.
     */
    @Test
    public void executeExample() throws Exception {
        List<NameAndContent> list = FileContentProvider
                .getContent(FileTypes.VHDL_SOURCE);
        NameAndContent nc = list.get(0); // comp_and
        file.setContent(nc.getContent());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "comp_and", ci.getName());
        List<Port> ports = new ArrayList<Port>(3);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        ports.add(new Port("f", PortDirection.OUT, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Comments are ignored.
     */
    @Test
    public void executeComments() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("-- ENTITY circuitOR IS\n");
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Comments are ignored.
     */
    @Test
    public void executeComments2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY -- circuitOR\n");
        sb.append("circuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Multiple whitespaces are merged into one.
     */
    @Test
    public void executeWhitespaces() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append(" ENTITY \n circuitAND \t\t  IS    \r\n");
        sb.append("PORT     (\t\n\n\n");
        sb.append("a   ,   b  :  IN\t\t std_logic  \t\t;  ");
        sb.append("c :  OUT\t\t std_logic_vector (  8   DOWNTO   1  )  \t\t");
        sb.append("\t\t) \t ;  \n");
        sb.append("   END \r\n\t   circuitAND   ; ");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(3);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("c", PortDirection.OUT, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Multiple whitespaces are merged into one.
     */
    @Test
    public void executeWhitespaces2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append(" ENTITY \n circuitAND \t\t  IS    \r\n");
        sb.append("PORT     (\t\n\n\n");
        sb.append("c :  OUT\t\t std_logic_vector (  8   DOWNTO   1  )  \t\t;");
        sb.append("a   ,   b  :  IN\t\t std_logic  \t\t  ");
        sb.append("\t\t) \t ;  \n");
        sb.append("   END \r\n\t   circuitAND   ; ");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(3);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("c", PortDirection.OUT, type));
        type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Source uses minimal whitespaces.
     */
    @Test
    public void executeWhitespaces3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(");
        sb.append("a,b:IN std_logic;");
        sb.append("c:OUT std_logic_vector(8 DOWNTO 1));");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(3);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("c", PortDirection.OUT, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Source uses minimal whitespaces.
     */
    @Test
    public void executeWhitespaces4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(");
        sb.append("c:OUT std_logic_vector(8 DOWNTO 1);");
        sb.append("a,b:IN std_logic);");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(3);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("c", PortDirection.OUT, type));
        type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * VHDL code is case insensitive
     */
    @Test
    public void executeCaseInsensitive() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("eNtItY circuitAND is\n");
        sb.append("enD CiRCUitAnd;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Anything before ENTITY doesn't matter.
     */
    @Test
    public void executeEntity() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("before doesn't matter.\n");
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * No ENTITY keyword.
     */
    @Test(expected = ServiceException.class)
    public void executeEntity2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("circuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace must be after ENTITY.
     */
    @Test(expected = ServiceException.class)
    public void executeEntity3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITYcircuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Entity name is not correct.
     */
    @Test(expected = ServiceException.class)
    public void executeEntityName() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY _circuitAND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Entity names must match.
     */
    @Test(expected = ServiceException.class)
    public void executeEntityName2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END circuitOR;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * IS must be after entity name.
     */
    @Test(expected = ServiceException.class)
    public void executeIs() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuit AND IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * IS must be after entity name.
     */
    @Test(expected = ServiceException.class)
    public void executeIs2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND wrong IS\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace must be after IS.
     */
    @Test(expected = ServiceException.class)
    public void executeIs3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND ISEND circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Either PORT, GENERIC or END must be after IS.
     */
    @Test(expected = ServiceException.class)
    public void executeIs4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS wrong\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or left bracket must be after GENERIC.
     */
    @Test(expected = ServiceException.class)
    public void executeGeneric() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERICwrong(\n");
        sb.append("n: positive := 2);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or semicolon must be after right bracket of GENERIC clause.
     */
    @Test(expected = ServiceException.class)
    public void executeGeneric2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC (\n");
        sb.append("n: positive := 2)wrong;\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after GENERIC.
     */
    @Test
    public void executeGeneric3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC (\n");
        sb.append("n: positive := 2);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Left bracket is after GENERIC.
     */
    @Test
    public void executeGeneric4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Whitespace is after right bracket.
     */
    @Test
    public void executeGeneric5() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2) ;\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * After whitespace must be semicolon.
     */
    @Test(expected = ServiceException.class)
    public void executeGeneric6() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2) wrong;\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Semicolon is after right bracket.
     */
    @Test
    public void executeGeneric7() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * Whitespace, END or PORT must be after GENERIC clause.
     */
    @Test(expected = ServiceException.class)
    public void executeGeneric8() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2);wrong\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after GENERIC clause.
     */
    @Test
    public void executeGeneric9() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2); ");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

    /**
     * PORT is after GENERIC clause.
     */
    @Test
    public void executeGeneric10() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS GENERIC(\n");
        sb.append("n: positive := 2);\n");
        sb.append("PORT(a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace or left bracket must be after PORT.
     */
    @Test(expected = ServiceException.class)
    public void executePort() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORTwrong(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Left bracket is after PORT.
     */
    @Test
    public void executePort2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace then left bracket is after PORT.
     */
    @Test
    public void executePort3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace may be after left bracket.
     */
    @Test
    public void executePort4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Without whitespace after left bracket.
     */
    @Test
    public void executePort5() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * PORT must end with right bracket.
     */
    @Test(expected = ServiceException.class)
    public void executePort6() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic(;\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or semicolon must be after right bracket.
     */
    @Test(expected = ServiceException.class)
    public void executePort7() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic),\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or semicolon must be after right bracket.
     */
    @Test(expected = ServiceException.class)
    public void executePort8() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic) ,\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * With whitespace after right bracket.
     */
    @Test
    public void executePort9() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic) ;\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Without whitespace after right bracket.
     */
    @Test
    public void executePort10() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace, END or GENERIC must be after semicolon.
     */
    @Test(expected = ServiceException.class)
    public void executePort11() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic);wrong");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * GENERIC or END must be after semicolon.
     */
    @Test(expected = ServiceException.class)
    public void executePort12() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT (");
        sb.append("a:IN std_logic); wrong");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Without whitespace after semicolon.
     */
    @Test
    public void executePort13() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * With whitespace after semicolon.
     */
    @Test
    public void executePort14() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic); ");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Generic is after PORT clause.
     */
    @Test
    public void executePort15() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("GENERIC(a:positive := 2);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Port declaration must start with port name.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("_a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Comma, colon or whitespace must be after port name.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a.:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after port name.
     */
    @Test
    public void executePortDeclaration3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a :IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Colon is after port name.
     */
    @Test
    public void executePortDeclaration4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace or port name must be after comma.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration5() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a,.:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Port name must be after whitespace.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration6() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a, .:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Port name is after comma.
     */
    @Test
    public void executePortDeclaration7() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a,b:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * 3 port names defined.
     */
    @Test
    public void executePortDeclaration8() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a,b,c:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        ports.add(new Port("c", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Two defined ports must not share the same name.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration9() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a,a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or port direction must be after colon.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration10() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:wrong std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after colon.
     */
    @Test
    public void executePortDeclaration11() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a: IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Port type is after port direction.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration12() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_wrong);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Port type std_logic can't have range.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration13() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic(8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * After type std_logic whitespace, semicolon or right bracket must follow.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration14() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic,\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Right bracket after std_logic.
     */
    @Test
    public void executePortDeclaration15() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace after std_logic.
     */
    @Test
    public void executePortDeclaration16() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic );\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Semicolon after std_logic.
     */
    @Test
    public void executePortDeclaration17() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic;");
        sb.append("b:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC, Range.SCALAR);
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Can't declare two same ports.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration18() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic;");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Can't declare two same ports with same name.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration19() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic;");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace or another port declaration must be after std_logic.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration20() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic;);");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * If port type is std_logic_vector then whitespace or left bracket must
     * follow.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration21() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector;(8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after std_logic_vector.
     */
    @Test
    public void executePortDeclaration22() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector (8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace or number must be after left bracket.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration23() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(.8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after left bracket.
     */
    @Test
    public void executePortDeclaration24() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector( 8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * After left bracket must be a number.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration25() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(n DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Vector direction must be after number.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration26() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 wrong 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace must be after vector direction.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration27() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO_1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Number must be after vector direction.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration28() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO n));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * If vector direction is DOWNTO then left number >= right number!
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration29() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 11));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * If vector direction is TO then left number <= right number!
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration30() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 TO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Left number == right number and vector direction is DOWNTO.
     */
    @Test
    public void executePortDeclaration31() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(1 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(1,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Left number == right number and vector direction is TO.
     */
    @Test
    public void executePortDeclaration32() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(1 TO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(1,
                VectorDirection.TO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace or right bracket must be after right number.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration33() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1.));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after right number.
     */
    @Test
    public void executePortDeclaration34() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1 ));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace or semicolon must be after right bracket.
     */
    @Test(expected = ServiceException.class)
    public void executePortDeclaration35() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1).);\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace is after right bracket.
     */
    @Test
    public void executePortDeclaration36() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1) );\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Semicolon is after right bracket.
     */
    @Test
    public void executePortDeclaration37() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic_vector(8 DOWNTO 1) ;");
        sb.append("b:IN std_logic_vector(8 DOWNTO 1));\n");
        sb.append("END circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        List<Port> ports = new ArrayList<Port>(1);
        Type type = new Type(TypeName.STD_LOGIC_VECTOR, new Range(8,
                VectorDirection.DOWNTO, 1));
        ports.add(new Port("a", PortDirection.IN, type));
        ports.add(new Port("b", PortDirection.IN, type));
        assertEquals("ports not equal.", ports, ci.getPorts());
    }

    /**
     * Whitespace must be after END.
     */
    @Test(expected = ServiceException.class)
    public void executeEnd() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("ENDcircuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Either entity name or ENTITY must be after END.
     */
    @Test(expected = ServiceException.class)
    public void executeEnd2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END wrong circuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Semicolon or whitespace must be after entity name.
     */
    @Test(expected = ServiceException.class)
    public void executeEnd3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END circuitAND.");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Semicolon or whitespace must be after entity name.
     */
    @Test(expected = ServiceException.class)
    public void executeEnd4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END circuitAND .");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Whitespace must be after ENTITY.
     */
    @Test(expected = ServiceException.class)
    public void executeEnd5() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END ENTITYcircuitAND;");
        file.setContent(sb.toString());
        extractor.execute(file);
    }

    /**
     * Anything before ENTITY doesn't matter.
     */
    @Test
    public void executeEnd6() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS\n");
        sb.append("END ENTITY circuitAND;");
        file.setContent(sb.toString());
        CircuitInterface ci = extractor.execute(file);
        assertEquals("wrong entity name.", "circuitAND", ci.getName());
        assertEquals("ports not empty.", Collections.emptyList(), ci.getPorts());
    }

}
