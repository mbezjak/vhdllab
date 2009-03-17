package hr.fer.zemris.vhdllab.service.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class HierarchyNodeTest extends ValueObjectTestSupport {

    private HierarchyNode root;

    @Before
    public void initObject() {
        root = new HierarchyNode(
                new File("file_name", FileType.SOURCE, "data"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullFile() {
        new HierarchyNode(null, root);
    }

    @Test
    public void constructor() {
        File file = new File("another_file", null, "data");
        root = new HierarchyNode(file, null);
        assertEquals(file, root.getFile());
        assertNotSame(file, root.getFile());
        assertNull(root.getFile().getData());
        assertNull(root.getFile().getProject());

        assertTrue(root.getDependencies().isEmpty());
        assertNull(root.getParent());
    }

    @Test
    public void constructor2() {
        File file = new File("another_file", null, null);
        HierarchyNode node = new HierarchyNode(file, root);
        assertEquals(root, node.getParent());
        assertTrue(root.getDependencies().contains(file));
    }

    @Test
    public void hasDependencies() {
        assertFalse(root.hasDependencies());
        root.addDependency(new HierarchyNode(new File(), null));
        assertTrue(root.hasDependencies());
    }

    @Test(expected = NullPointerException.class)
    public void addDependencyNullArgument() {
        root.addDependency(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDependencyDuplicate() {
        HierarchyNode node = new HierarchyNode(new File(), null);
        root.addDependency(node);
        root.addDependency(node);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDependencyDuplicate2() {
        HierarchyNode node = new HierarchyNode(new File(), null);
        root.addDependency(node);
        root.addDependency(new HierarchyNode(
                new File("middle_file", null, null), null));
        root.addDependency(node);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDependencySelf() throws Exception {
        root.addDependency(root);
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDependencyCyclic() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);
        node.addDependency(new HierarchyNode(file, null));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addDependencyCyclic2() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);

        HierarchyNode noodDeepInHierarchy = new HierarchyNode(new File(), null);
        node.addDependency(noodDeepInHierarchy);
        noodDeepInHierarchy.addDependency(new HierarchyNode(root.getFile(),
                null));
    }

    @Test
    public void addDependency() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);
        assertEquals(root, node.getParent());
        assertTrue(root.getDependencies().contains(file));
    }

    /**
     * Returns direct reference.
     */
    @Test
    public void getFile() {
        File file = root.getFile();
        file.setName("name");
        assertEquals("name", root.getFile().getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDependencies() {
        root.getDependencies().add(new File());
    }

    @Test
    public void hashCodeAndEquals() throws Exception {
        basicEqualsTest(root);

        HierarchyNode newRoot = new HierarchyNode(new File(), null);
        HierarchyNode another = new HierarchyNode(root.getFile(), newRoot);
        assertEqualsAndHashCode(root, another);

        another = new HierarchyNode(new File("another_file", null, null),
                newRoot);
        assertNotEqualsAndHashCode(root, another);
    }

    @Test
    public void testToString() {
        toStringPrint(root);
        assertEquals("file_name []", root.toString());

        new HierarchyNode(new File("another_file", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [another_file]", root.toString());

        new HierarchyNode(new File("new_file", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [another_file,new_file]", root.toString());

        new HierarchyNode(new File(), root);
        toStringPrint(root);
        assertEquals("file_name [another_file,new_file,null]", root.toString());
    }

}
