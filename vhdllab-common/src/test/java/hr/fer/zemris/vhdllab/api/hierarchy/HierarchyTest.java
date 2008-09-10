package hr.fer.zemris.vhdllab.api.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import hr.fer.zemris.vhdllab.api.FileTypes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Hierarchy}.
 *
 * @author Miro Bezjak
 */
public class HierarchyTest {

    private final static String NAME = "project.name";
    private final static String NEW_NAME = "new" + NAME;
    private final static String ROOT_NODE_NAME = "root";

    private Hierarchy hierarchy;
    private Set<HierarchyNode> nodes;
    private HierarchyNode root;
    private HierarchyNode left;
    private HierarchyNode right;
    private HierarchyNode rightDep;

    @Before
    public void initEachTest() {
        nodes = new HashSet<HierarchyNode>();
        root = new HierarchyNode(ROOT_NODE_NAME, FileTypes.VHDL_SOURCE, null);
        right = new HierarchyNode("right", FileTypes.VHDL_SOURCE, root);
        left = new HierarchyNode("left", FileTypes.VHDL_SOURCE, root);
        rightDep = new HierarchyNode("right-dep", FileTypes.VHDL_SOURCE, right);
        nodes.add(root);
        nodes.add(right);
        nodes.add(left);
        nodes.add(rightDep);
        hierarchy = new Hierarchy(NAME, nodes);
    }

    /**
     * Project name is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new Hierarchy(null, nodes);
    }

    /**
     * Nodes is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new Hierarchy(NAME, null);
    }

    /**
     * Top level files are initialized
     */
    @Test
    public void constructor3() throws Exception {
        Set<String> names = new HashSet<String>(1);
        names.add(ROOT_NODE_NAME);
        assertEquals("top level files not equal.", names, hierarchy
                .getTopLevelFiles());

        String anotherRootName = "new.root.name";
        nodes.add(new HierarchyNode(anotherRootName, FileTypes.VHDL_SCHEMA,
                null));
        hierarchy = new Hierarchy(NAME, nodes);
        names.add(anotherRootName);

        assertEquals("top level files not equal.", names, hierarchy
                .getTopLevelFiles());

    }

    /**
     * Nodes are defensively copied
     */
    @Test
    public void constructor4() throws Exception {
        Field field = root.getClass().getDeclaredField("dependencies");
        field.setAccessible(true);
        field.set(root, Collections.emptySet());
        assertFalse("dependencies not defensively copied.", Collections
                .emptySet().equals(
                        hierarchy.getDependenciesForFile(ROOT_NODE_NAME)));
    }

    /**
     * Test getters and setters
     */
    @Test
    public void gettersAndSetters() throws Exception {
        assertEquals("getProjectName.", NAME, hierarchy.getProjectName());
    }

    /**
     * Returned set is immutable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getTopLevelFiles() throws Exception {
        hierarchy.getTopLevelFiles().remove(ROOT_NODE_NAME);
    }

    /**
     * Name is null
     */
    @Test(expected = NullPointerException.class)
    public void getDependenciesForFile() throws Exception {
        hierarchy.getDependenciesForFile(null);
    }

    /**
     * non-existing name
     */
    @Test(expected = IllegalArgumentException.class)
    public void getDependenciesForFile2() throws Exception {
        hierarchy.getDependenciesForFile("non-existing name");
    }

    /**
     * Name is case insensitive
     */
    @Test
    public void getDependenciesForFile3() throws Exception {
        Set<String> actual = hierarchy.getDependenciesForFile(ROOT_NODE_NAME
                .toUpperCase());
        Set<String> expected = new HashSet<String>(2);
        expected.add(left.getFileName());
        expected.add(right.getFileName());
        assertEquals("name is not case insensitive.", expected, actual);
    }

    /**
     * Returned set is immutable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getDependenciesForFile4() throws Exception {
        hierarchy.getDependenciesForFile(ROOT_NODE_NAME).remove(right);
    }

    /**
     * Name is null
     */
    @Test(expected = NullPointerException.class)
    public void getAllDependenciesForFile() throws Exception {
        hierarchy.getAllDependenciesForFile(null);
    }

    /**
     * non-existing name
     */
    @Test(expected = IllegalArgumentException.class)
    public void getAllDependenciesForFile2() throws Exception {
        hierarchy.getAllDependenciesForFile("non-existing name");
    }

    /**
     * Name is case insensitive
     */
    @Test
    public void getAllDependenciesForFile3() throws Exception {
        Set<String> actual = hierarchy.getAllDependenciesForFile(ROOT_NODE_NAME
                .toUpperCase());
        Set<String> expected = new HashSet<String>(2);
        expected.add(left.getFileName());
        expected.add(right.getFileName());
        expected.add(rightDep.getFileName());
        assertEquals("name is not case insensitive.", expected, actual);
    }

    /**
     * Duplicate node - unaffected result
     */
    @Test
    public void getAllDependenciesForFile4() throws Exception {
        HierarchyNode newRightDep = new HierarchyNode(rightDep);
        nodes.add(newRightDep);
        hierarchy = new Hierarchy(NAME, nodes);

        Set<String> actual = hierarchy
                .getAllDependenciesForFile(ROOT_NODE_NAME);
        Set<String> expected = new HashSet<String>(2);
        expected.add(left.getFileName());
        expected.add(right.getFileName());
        expected.add(rightDep.getFileName());
        assertEquals("name is not case insensitive.", expected, actual);
    }

    /**
     * Test equals with self, null, and non-hierarchy object
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", hierarchy, hierarchy);
        assertFalse("node is equal to null.", root.equals(null));
        assertFalse("can compare with string object.", root
                .equals("a string object"));
    }

    /**
     * everything ok
     */
    @Test
    public void hashCodeAndEquals() throws Exception {
        Hierarchy newRoot = new Hierarchy(NAME, Collections
                .<HierarchyNode> emptySet());

        assertEquals("hierarchies not equal.", hierarchy, newRoot);
        assertEquals("hierarchies not equal.", hierarchy.hashCode(), newRoot
                .hashCode());
    }

    /**
     * Project name is case insensitive.
     */
    @Test
    public void hashCodeAndEquals2() throws Exception {
        Hierarchy newRoot = new Hierarchy(NAME.toUpperCase(), Collections
                .<HierarchyNode> emptySet());

        assertEquals("hierarchies not equal.", hierarchy, newRoot);
        assertEquals("hierarchies not equal.", hierarchy.hashCode(), newRoot
                .hashCode());
    }

    /**
     * Name is different
     */
    @Test
    public void hashCodeAndEquals3() throws Exception {
        Hierarchy newRoot = new Hierarchy(NEW_NAME, Collections
                .<HierarchyNode> emptySet());

        assertFalse("hierarchy nodes are equal.", root.equals(newRoot));
        assertFalse("hierarchy nodes are equal.", root.hashCode() == newRoot
                .hashCode());
    }

    /**
     * Simulate serialization tempering
     */
    @SuppressWarnings("unchecked")
    @Test(expected = NullPointerException.class)
    public void readResolve() throws Exception {
        Field field = hierarchy.getClass().getDeclaredField("projectName");
        field.setAccessible(true);
        field.set(hierarchy, null); // set illegal state of result object

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(hierarchy);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        bis.readObject();
    }

    /**
     * Serialize then deserialize
     */
    @SuppressWarnings("unchecked")
    @Test
    public void readResolve2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(hierarchy);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        Hierarchy newRoot = (Hierarchy) bis.readObject();

        Object value = readPrivateField(newRoot, "nodes");
        assertNotNull("nodes is null after serialization.", value);
        value = readPrivateField(newRoot, "topLevelFiles");
        assertNotNull("topLevelFiles is null after serialization.", value);
        assertEquals("hierarchies not equal.", hierarchy, newRoot);
    }

    @Ignore("must be tested by a user and this has already been tested")
    @Test
    public void asString() {
        System.out.println(hierarchy.toString());
    }

    /**
     * Returns a value of a private field for a specified object.
     */
    private Object readPrivateField(Object object, String fieldName)
            throws Exception {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(object);
        field.setAccessible(false);
        return value;
    }

}
