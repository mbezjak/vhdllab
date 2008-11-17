/*package hr.fer.zemris.vhdllab.service.dependencies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.entities.StubFactory;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

*//**
 * A test case for {@link SourceDependency}.
 *
 * @author Miro Bezjak
 *//*
public class SourceDependencyTest {

    private static DependencyExtractor extractor;
    private static File file;

    @BeforeClass
    public static void initClass() throws Exception {
        extractor = new SourceDependency();
        file = StubFactory.create(File.class, 400);
        Project project = StubFactory.create(Project.class, 400);
        project.addFile(file);
    }

    *//**
     * everything ok.
     *//*
    @Test
    public void executeExample() throws Exception {
        Project project = StubFactory.create(Project.class, 400);
        FileType type = FileType.SOURCE;
        List<NameAndContent> list = FileContentProvider.getContent(type);
        assumeTrue(list.size() >= 1);
        NameAndContent nc = list.get(0);
        File f = new File(type, nc.getName(), nc.getContent());
        project.addFile(f);
        DependencyExtractor dep = new SourceDependency();
        assertEquals("wrong dependencies extrated.", Collections.emptySet(),
                dep.execute(f));
    }

    *//**
     * Comments are ignored.
     *//*
    @Test
    public void executeComments() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("-- ENTITY WORK.circuitOR IS\n");
        sb.append("ENTITY WORK.circuitAND IS ");
        sb.append("PORT(a, b, f);");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

    *//**
     * Comments are ignored.
     *//*
    @Test
    public void executeComments2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.-- circuitOR\n");
        sb.append("circuitAND IS ");
        sb.append("PORT(a, b, f);");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        assertEquals("wrong dependency.", new Caseless("circuitAND"), deps.iterator().next());
    }

    *//**
     * Multiple whitespaces are merged into one.
     *//*
    @Test
    public void executeWhitespaces() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append(" ENTITY \n WORK  \t. \r\n circuitAND\t");
        sb.append(" \t\t  IS PORT(a, b, f);\n ");
        sb.append("\tCOMPONENT \t\r\n   circuitOR \tIS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 2, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitOR"), iterator.next());
    }

    *//**
     * Source uses minimal whitespaces.
     *//*
    @Test
    public void executeWhitespaces2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS PORT(a,b,f);");
        sb.append("COMPONENT circuitOR IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 2, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitOR"), iterator.next());
    }

    *//**
     * VHDL code is case insensitive
     *//*
    @Test
    public void executeCaseInsensitive() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("eNtItY WoRk.circuitAND IS PORT(a,b,f);");
        sb.append("CompoNEnt circuitOR IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 2, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitOR"), iterator.next());
    }

    *//**
     * Empty content.
     *//*
    @Test
    public void executeEmpty() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("empty content");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after ENTITY.
     *//*
    @Test
    public void executeEntity() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after ENTITY.
     *//*
    @Test
    public void executeEntity2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITYwrong");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * WORK must be after ENTITY.
     *//*
    @Test
    public void executeEntity3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY circuitAND IS PORT(\n");
        sb.append("a:IN std_logic);\n");
        sb.append("END circuitAND;");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Dot must be after WORK.
     *//*
    @Test
    public void executeWORK() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after component name.
     *//*
    @Test
    public void executeWORK2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace is after component name.
     *//*
    @Test
    public void executeWORK3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

    *//**
     * Multiple entities are defined.
     *//*
    @Test
    public void executeMultipleEntities() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        sb.append("ENTITY WORK.circuitOR IS...");
        sb.append("ENTITY WORK.circuitXOR IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 3, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitOR"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitXOR"), iterator.next());
    }

    *//**
     * Whitespace must be after COMPONENT.
     *//*
    @Test
    public void executeComponent() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENT");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after COMPONENT.
     *//*
    @Test
    public void executeComponent2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENTwrong");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after component name.
     *//*
    @Test
    public void executeComponent3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENT circuitAND");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 0, deps.size());
    }

    *//**
     * Whitespace must be after component name.
     *//*
    @Test
    public void executeComponent4() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENT circuitAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

    *//**
     * Multiple components are defined.
     *//*
    @Test
    public void executeMultipleComponents() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENT circuitAND IS...");
        sb.append("COMPONENT circuitOR IS...");
        sb.append("COMPONENT circuitXOR IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 3, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitOR"), iterator.next());
        assertEquals("wrong dependency.", new Caseless("circuitXOR"), iterator.next());
    }

    *//**
     * Multiple entities and components are defined.
     *//*
    @Test
    public void executeMultipleMixed() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        sb.append("ENTITY WORK.circuitOR IS...");
        sb.append("COMPONENT circuitXOR IS...");
        sb.append("COMPONENT circuitNAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 4, deps.size());
        Set<Caseless> expected = new HashSet<Caseless>(4);
        expected.add(new Caseless("circuitAND"));
        expected.add(new Caseless("circuitOR"));
        expected.add(new Caseless("circuitXOR"));
        expected.add(new Caseless("circuitNAND"));
        assertEquals("wrong dependency.", expected, deps);
    }

    *//**
     * Multiple entities and components are defined.
     *//*
    @Test
    public void executeMultipleMixed2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        sb.append("COMPONENT circuitXOR IS...");
        sb.append("ENTITY WORK.circuitOR IS...");
        sb.append("COMPONENT circuitNAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 4, deps.size());
        Set<Caseless> expected = new HashSet<Caseless>(4);
        expected.add(new Caseless("circuitAND"));
        expected.add(new Caseless("circuitOR"));
        expected.add(new Caseless("circuitXOR"));
        expected.add(new Caseless("circuitNAND"));
        assertEquals("wrong dependency.", expected, deps);
    }

    *//**
     * Duplicate components are defined.
     *//*
    @Test
    public void executeDuplicate() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        sb.append("ENTITY WORK.circuitAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

    *//**
     * Duplicate components are defined.
     *//*
    @Test
    public void executeDuplicate2() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("COMPONENT circuitAND IS...");
        sb.append("COMPONENT circuitAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

    *//**
     * Duplicate components are defined.
     *//*
    @Test
    public void executeDuplicate3() throws Exception {
        StringBuilder sb = new StringBuilder(50);
        sb.append("ENTITY WORK.circuitAND IS...");
        sb.append("COMPONENT circuitAND IS...");
        file.setData(sb.toString());
        Set<Caseless> deps = extractor.execute(file);
        assertEquals("wrong dependencies extracted.", 1, deps.size());
        Iterator<Caseless> iterator = deps.iterator();
        assertEquals("wrong dependency.", new Caseless("circuitAND"), iterator.next());
    }

}
*/