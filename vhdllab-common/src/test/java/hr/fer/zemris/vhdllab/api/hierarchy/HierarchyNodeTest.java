package hr.fer.zemris.vhdllab.api.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.FileType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link HierarchyNode}.
 * 
 * @author Miro Bezjak
 */
public class HierarchyNodeTest {

    private final static String NAME = "file.name";
    private final static FileType TYPE = FileType.SOURCE;
    private final static String NEW_NAME = "new" + NAME;

    private HierarchyNode root;

    @Before
    public void initEachTest() {
        root = new HierarchyNode(NAME, TYPE, null);
    }

    /**
     * everything ok
     */
    @Test
    public void copyConstructor() throws Exception {
        HierarchyNode newRoot = new HierarchyNode(root);
        assertEquals("dependencies not equal.", root.getDependencies(), newRoot
                .getDependencies());
        assertEquals("file types not equal.", root.getFileType(), newRoot
                .getFileType());
        assertEquals("nodes not equal.", root, newRoot);
    }

    /**
     * File name is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new HierarchyNode(null, TYPE, root);
    }

    /**
     * File type is null
     */
    @Test(expected = NullPointerException.class)
    public void constructor2() throws Exception {
        new HierarchyNode(NAME, null, root);
    }

    /**
     * Duplicate dependency
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor3() throws Exception {
        new HierarchyNode(NAME, TYPE, root);
        new HierarchyNode(NAME, TYPE, root); // duplicate
    }

    /**
     * Duplicate dependency - element in the middle
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor4() throws Exception {
        new HierarchyNode(NAME, TYPE, root);
        new HierarchyNode(NEW_NAME, TYPE, root);
        new HierarchyNode(NAME, TYPE, root); // duplicate
    }

    /**
     * Self dependency
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor5() throws Exception {
        new HierarchyNode(NAME, TYPE, root);
    }

    /**
     * cyclic dependency
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor6() throws Exception {
        HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, root);
        new HierarchyNode(NAME, TYPE, node);
    }

    /**
     * Node is null.
     */
    @Test(expected = NullPointerException.class)
    public void addDependency() throws Exception {
        root.addDependency(null);
    }

    /**
     * Duplicate dependency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addDependency2() throws Exception {
        root.addDependency(new HierarchyNode(NAME, TYPE, null));
        root.addDependency(new HierarchyNode(NAME, TYPE, null)); // duplicate
    }

    /**
     * Duplicate dependency - element in the middle.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addDependency3() throws Exception {
        root.addDependency(new HierarchyNode(NAME, TYPE, null));
        root.addDependency(new HierarchyNode(NEW_NAME, TYPE, null));
        root.addDependency(new HierarchyNode(NAME, TYPE, null)); // duplicate
    }

    /**
     * Self dependency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addDependency4() throws Exception {
        root.addDependency(root);
    }

    /**
     * cyclic dependency.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addDependency5() throws Exception {
        HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, null);
        root.addDependency(node);
        node.addDependency(new HierarchyNode(NAME, TYPE, null));
    }

    /**
     * Parent is set after dependency is added.
     */
    @Test
    public void addDependency6() throws Exception {
        HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, null);
        root.addDependency(node);
        HierarchyNode parent = (HierarchyNode) readPrivateField(node, "parent");
        assertNotNull("parent in dependency not set.s", parent);
        assertEquals("parent is not root.", root, parent);
    }

    /**
     * Test getters and setters
     */
    @Test
    public void gettersAndSetters() throws Exception {
        assertEquals("getFileName.", NAME, root.getFileName());
        assertEquals("getFileType.", TYPE, root.getFileType());
        assertEquals("getDependencies.", Collections.emptySet(), root
                .getDependencies());
    }

    /**
     * Returned set is immutable.
     */
    @Test(expected = UnsupportedOperationException.class)
    public void getDependencies() throws Exception {
        HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, root);
        root.getDependencies().remove(node);
    }

    /**
     * Name case in not changed
     */
    @Test
    public void getDependencies2() throws Exception {
        String name = "A fIlE NaMe.";
        new HierarchyNode(name, TYPE, root);
        String returnedName = root.getDependencies().iterator().next().toString();
        assertEquals("names not equal.", name, returnedName);
    }

    /**
     * Test equals with self, null, and non-hierarchy-node object
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", root, root);
        assertFalse("node is equal to null.", root.equals(null));
        assertFalse("can compare with string object.", root
                .equals("a string object"));
    }

    /**
     * everything ok
     */
    @Test
    public void hashCodeAndEquals() throws Exception {
        HierarchyNode newRoot = new HierarchyNode(root.getFileName(),
                FileType.AUTOMATON, null);

        assertEquals("hierarchy nodes not equal.", root, newRoot);
        assertEquals("hierarchy nodes not equal.", root.hashCode(), newRoot
                .hashCode());
    }

    /**
     * Name is different
     */
    @Test
    public void hashCodeAndEquals2() throws Exception {
        HierarchyNode newRoot = new HierarchyNode(NEW_NAME, root.getFileType(),
                null);

        assertFalse("hierarchy nodes are equal.", root.equals(newRoot));
        assertFalse("hierarchy nodes are equal.", root.hashCode() == newRoot
                .hashCode());
    }

    /**
     * Name is of different case
     */
    @Test
    public void hashCodeAndEquals3() throws Exception {
        HierarchyNode newRoot = new HierarchyNode(NAME.toUpperCase(), root
                .getFileType(), null);

        assertTrue("hierarchy nodes not equal.", root.equals(newRoot));
        assertTrue("hierarchy nodes not equal.", root.hashCode() == newRoot
                .hashCode());
    }

    /**
     * Simulate serialization tempering
     */
    @Test(expected = NullPointerException.class)
    public void readResolve() throws Exception {
        Field field = root.getClass().getDeclaredField("fileName");
        field.setAccessible(true);
        field.set(root, null); // set illegal state of result object

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(root);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        bis.readObject();
    }

    /**
     * Serialize then deserialize
     */
    @Test
    public void readResolve2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(root);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        HierarchyNode newRoot = (HierarchyNode) bis.readObject();

        Object parent = readPrivateField(newRoot, "parent");
        assertNull("parent not null after serialization.", parent);
        boolean modified = (Boolean) readPrivateField(newRoot, "mutable");
        assertFalse("node still mutable after serialization.", modified);
        assertEquals("hierarchy nodes not equal.", root, newRoot);
    }

    /**
     * After deserialization node is immutable.
     */
    @Test(expected = IllegalStateException.class)
    public void readResolve3() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(root);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        HierarchyNode newRoot = (HierarchyNode) bis.readObject();
        new HierarchyNode(NEW_NAME, TYPE, newRoot);
    }

    @Ignore("must be tested by a user and this has already been tested")
    @Test
    public void asString() {
        System.out.println(root.toString());
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
