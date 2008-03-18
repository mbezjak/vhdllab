package hr.fer.zemris.vhdllab.api.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.api.FileTypes;

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
	private final static String TYPE = FileTypes.VHDL_SOURCE;
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
	 * Duplicate dependency - name is case insensitive
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor5() throws Exception {
		new HierarchyNode(NAME, TYPE, root);
		new HierarchyNode(NAME.toUpperCase(), TYPE, root); // duplicate
	}

	/**
	 * Self dependency
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor6() throws Exception {
		new HierarchyNode(NAME, TYPE, root);
	}

	/**
	 * Self dependency - name is case insensitive
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor7() throws Exception {
		new HierarchyNode(NAME.toUpperCase(), TYPE, root);
	}

	/**
	 * Circular dependency
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor8() throws Exception {
		HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, root);
		new HierarchyNode(NAME, TYPE, node);
	}

	/**
	 * Circular dependency - name is case insensitive
	 */
	@Test(expected = IllegalArgumentException.class)
	public void constructor9() throws Exception {
		HierarchyNode node = new HierarchyNode(NEW_NAME, TYPE, root);
		new HierarchyNode(NAME.toUpperCase(), TYPE, node);
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
		String returnedName = root.getDependencies().iterator().next();
		assertEquals("names not equal.", name, returnedName);
	}

	/**
	 * Test equals with self, null, and non-hierarchy-node object
	 */
	@Test
	public void equals() throws Exception {
		assertEquals("not equal.", root, root);
		assertNotSame("port is equal to null.", root, null);
		assertNotSame("can compare with string object.", root,
				"a string object");
	}

	/**
	 * everything ok
	 */
	@Test
	public void hashCodeAndEquals() throws Exception {
		HierarchyNode newRoot = new HierarchyNode(root.getFileName(),
				FileTypes.VHDL_AUTOMATON, null);

		assertEquals("hierarchy nodes not equal.", root, newRoot);
		assertEquals("hierarchy nodes not equal.", root.hashCode(), newRoot
				.hashCode());
	}

	/**
	 * File name is case insensitive.
	 */
	@Test
	public void hashCodeAndEquals2() throws Exception {
		HierarchyNode newRoot = new HierarchyNode(root.getFileName()
				.toUpperCase(), root.getFileType(), null);

		assertEquals("hierarchy nodes not equal.", root, newRoot);
		assertEquals("hierarchy nodes not equal.", root.hashCode(), newRoot
				.hashCode());
	}

	/**
	 * Name is different
	 */
	@Test
	public void hashCodeAndEquals3() throws Exception {
		HierarchyNode newRoot = new HierarchyNode(NEW_NAME, root.getFileType(),
				null);

		assertNotSame("hierarchy nodes are equal.", root, newRoot);
		assertNotSame("hierarchy nodes are equal.", root.hashCode(), newRoot
				.hashCode());
	}

	/**
	 * Simulate serialization tempering
	 */
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
	@SuppressWarnings("unchecked")
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
