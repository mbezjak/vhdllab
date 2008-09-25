package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for EntityObject.
 * 
 * @author Miro Bezjak
 */
public class EntityObjectTest {

    private EntityObject entity;

    @Before
    public void initEachTest() throws Exception {
        entity = StubFactory.create(EntityObject.class, 1);
    }

    /**
     * Name is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new EntityObject((Caseless) null);
    }

    /**
     * Name is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        Caseless name = StubFactory.getStubValue("name", 301);
        new EntityObject(name);
    }

    /**
     * Name isn't correctly formatted.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor3() throws Exception {
        Caseless name = StubFactory.getStubValue("name", 302);
        new EntityObject(name);
    }

    /**
     * EntityObject is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new EntityObject((EntityObject) null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        EntityObject another = new EntityObject(entity);
        assertTrue("same reference.", entity != another);
        assertEquals("not equal.", entity, another);
        assertEquals("hashCode not same.", entity.hashCode(), another
                .hashCode());
        assertEquals("names not same.", entity.getName(), another.getName());
    }

    /**
     * Test equals with self, null, and non-EntityObject.
     */
    @Test
    public void equals() throws Exception {
        assertEquals("not equal.", entity, entity);
        assertFalse("resource is equal to null.", entity.equals(null));
        assertFalse("can compare with string object.", entity
                .equals("string object"));
    }

    /**
     * Only names are important in equals and hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        EntityObject another = StubFactory.create(EntityObject.class, 2);
        StubFactory.setProperty(another, "name", 1);
        assertEquals("not equal.", entity, another);
        assertEquals("hashCode not same.", entity.hashCode(), another
                .hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        EntityObject another = new EntityObject(entity);
        StubFactory.setProperty(another, "name", 2);
        assertFalse("equal.", entity.equals(another));
        assertNotSame("same hashCode.", entity.hashCode(), another.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(entity);
        assertEquals("not same.", entity, deserialized);
    }

    /**
     * Simulate data tempering - name is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        StubFactory.setProperty(entity, "name", 300);
        SerializationUtils.clone(entity);
    }

    /**
     * Simulate data tempering - name is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        StubFactory.setProperty(entity, "name", 301);
        SerializationUtils.clone(entity);
    }

    /**
     * Simulate data tempering - name isn't correctly formatted.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization4() throws Exception {
        StubFactory.setProperty(entity, "name", 302);
        SerializationUtils.clone(entity);
    }

    @Test
    public void testToString() {
        System.out.println(entity);
    }

}
