package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link Resource} superclass entity.
 * 
 * @author Miro Bezjak
 */
public class ContainerTest {

	private static final Long ID = Long.valueOf(123456);
	private static final String NAME = "resource.name";
	private static final Date CREATED;
	private static final Long NEW_ID = Long.valueOf(654321);
	private static final String NEW_NAME = "new." + NAME;
	private static final Date NEW_CREATED;

	static {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH-mm");
		try {
			CREATED = df.parse("2008-01-02 13-45");
			NEW_CREATED = df.parse("2000-12-31 07-13");
		} catch (ParseException e) {
			// should never happen. but if pattern should change report it by
			// throwing exception.
			throw new IllegalStateException(e);
		}
	}

	private class CustomResource extends Resource<Container<CustomResource>> {
		private static final long serialVersionUID = 1L;

		public CustomResource() {
			super();
		}

		public CustomResource(CustomResource r) {
			super(r);
		}
	}

	private Container<CustomResource> container;
	private Container<CustomResource> container2;
	private CustomResource resource;
	private CustomResource resource2;
	private Set<CustomResource> children;

	@Before
	public void initEachTest() {
		container = new Container<CustomResource>();
		container.setId(ID);
		container.setName(NAME);
		container.setCreated(CREATED);
		container2 = new Container<CustomResource>(container);
		
		resource = new CustomResource();
		resource.setId(Long.valueOf(10));
		resource.setName("resource1.name");
		resource.setType("resource1.type");
		resource.setContent("resource1.content");
		resource.setCreated(Calendar.getInstance().getTime());
		resource2 = new CustomResource(); // not added immediately to container
		resource2.setId(Long.valueOf(20));
		resource2.setName("resource2.name");
		resource2.setType("resource2.type");
		resource2.setContent("resource2.content");
		resource2.setCreated(Calendar.getInstance().getTime());
		CustomResource resourceDuplicate = new CustomResource(resource);
		
		container.addChild(resource);
		container2.addChild(resourceDuplicate);
		children = new HashSet<CustomResource>();
		children.add(resource);
	}

	/**
	 * Test copy constructor
	 */
	@Test
	public void copyConstructor() {
		assertTrue(container != container2);
		assertEquals(container, container2);
		assertEquals(container.hashCode(), container2.hashCode());
		assertEquals(0, container.compareTo(container2));
		assertEquals(container.getChildren(), container2.getChildren());
	}

	/**
	 * Test getters and setters
	 */
	@Test
	public void gettersAndSetters() {
		/*
		 * Setters are tested indirectly. @Before method uses setters.
		 */
		assertEquals(ID, container.getId());
		assertEquals(NAME, container.getName());
		assertEquals(CREATED, container.getCreated());
		assertEquals(children, container.getChildren());
	}

	/**
	 * Test equals with self, null, and non-resource object
	 */
	@Test
	public void equals() {
		assertEquals(container, container);
		assertNotSame(container, null);
		assertNotSame(container, "a string object");
	}

	/**
	 * Null object as parameter to compareTo method
	 */
	@Test(expected = NullPointerException.class)
	public void compareTo() {
		container.compareTo(null);
	}

	/**
	 * Only ids (if set) are important in equals, hashCode and compareTo
	 */
	@Test
	public void equalsHashCodeAndCompareTo() {
		container2.setName(NEW_NAME);
		container2.setCreated(NEW_CREATED);
		container2.setChildren(new HashSet<CustomResource>());
		assertEquals(container, container2);
		assertEquals(container.hashCode(), container2.hashCode());
		assertEquals(0, container.compareTo(container2));
	}

	/**
	 * Ids are different
	 */
	@Test
	public void equalsHashCodeAndCompareTo2() {
		container2.setId(NEW_ID);
		assertNotSame(container, container2);
		assertNotSame(container.hashCode(), container2.hashCode());
		assertEquals(ID.compareTo(NEW_ID) < 0 ? -1 : 1, container
				.compareTo(container2));
	}

	/**
	 * Ids are null, then name is important
	 */
	@Test
	public void equalsHashCodeAndCompareTo3() {
		container.setId(null);
		container2.setId(null);
		container.setCreated(NEW_CREATED);
		container2.setCreated(NEW_CREATED);
		container.setChildren(new HashSet<CustomResource>());
		container2.setChildren(new HashSet<CustomResource>());
		assertEquals(container, container2);
		assertEquals(container.hashCode(), container2.hashCode());
		assertEquals(0, container.compareTo(container2));
	}

	/**
	 * Resource name is case insensitive
	 */
	@Test
	public void equalsHashCodeAndCompareTo4() {
		container.setId(null);
		container2.setId(null);
		container2.setName(NAME.toUpperCase());
		assertEquals(container, container2);
		assertEquals(container.hashCode(), container2.hashCode());
		assertEquals(0, container.compareTo(container2));
	}

	/**
	 * Ids are null and names are not equal
	 */
	@Test
	public void equalsHashCodeAndCompareTo5() {
		container.setId(null);
		container2.setId(null);
		container2.setName(NEW_NAME);
		assertNotSame(container, container2);
		assertNotSame(container.hashCode(), container2.hashCode());
		assertEquals(NAME.compareTo(NEW_NAME) < 0 ? -1 : 1, container
				.compareTo(container2));
	}

	/**
	 * Ids and names are null
	 */
	@Test
	public void equalsHashCodeAndCompareTo10() {
		container.setId(null);
		container2.setId(null);
		container.setName(null);
		container2.setName(null);
		assertEquals(container, container2);
		assertEquals(container.hashCode(), container2.hashCode());
		assertEquals(0, container.compareTo(container2));
	}
	
	/**
	 * Resource is null
	 */
	@Test(expected=NullPointerException.class)
	public void addChild() {
		container.addChild(null);
	}
	
	/**
	 * Add a resource
	 */
	@Test
	public void addChild2() {
		container.addChild(resource2);
		children.add(resource2);
		assertTrue(container.getChildren().contains(resource2));
		assertEquals(children, container.getChildren());
		assertEquals(container, resource2.getParent());
	}
	
	/**
	 * Add a resource that is already in another container
	 */
	@Test
	public void addChild3() {
		Container<CustomResource> newContainer = new Container<CustomResource>();
		newContainer.setId(NEW_ID);
		newContainer.setName(NEW_NAME);
		newContainer.setCreated(NEW_CREATED);
		Container<CustomResource> previousContainer = resource.getParent();
		newContainer.addChild(resource);
		assertEquals(children, newContainer.getChildren());
		assertEquals(newContainer, resource.getParent());
		assertEquals(false, previousContainer.getChildren().contains(resource));
	}
	
	/**
	 * Add a resource that is already in that container
	 */
	@Test
	public void addChild4() {
		container.addChild(resource);
		assertEquals(children, container.getChildren());
		assertEquals(1, container.getChildren().size());
		assertEquals(container, resource.getParent());
	}
	
	/**
	 * Resource is null
	 */
	@Test(expected=NullPointerException.class)
	public void removeChild() {
		container.removeChild(null);
	}
	
	/**
	 * Remove a resource
	 */
	@Test
	public void removeChild2() {
		container.removeChild(resource);
		assertEquals(new HashSet<CustomResource>(0), container.getChildren());
		assertEquals(null, resource.getParent());
	}
	
	/**
	 * Remove a resource that does not exists in a container
	 */
	@Test
	public void removeChild3() {
		container.removeChild(resource2);
		assertEquals(children, container.getChildren());
		assertEquals(null, resource2.getParent());
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() {
		System.out.println(container.toString());
	}

}
