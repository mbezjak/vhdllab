package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.ID_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_2;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_NOT_CORRECTLY_FORMATTED;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME_TOO_LONG;
import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.VERSION_2;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.EntityObjectStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link EntityObject}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class EntityObjectTest {

    private EntityObjectStub entity;

    @Before
    public void initEachTest() throws Exception {
        entity = new EntityObjectStub();
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
        new EntityObject(NAME_TOO_LONG);
    }

    /**
     * Name isn't correctly formatted.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor3() throws Exception {
        new EntityObject(NAME_NOT_CORRECTLY_FORMATTED);
    }

    /**
     * EntityObject is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new EntityObject((EntityObject) null, true);
    }

    /**
     * Test copy constructor - copy id and version.
     */
    @Test
    public void copyConstructor2() throws Exception {
        EntityObject another = new EntityObject(entity, true);
        assertTrue("same reference.", entity != another);
        assertEquals("not equal.", entity, another);
        assertEquals("hashCode not same.", entity.hashCode(), another
                .hashCode());
        assertEquals("ids not same.", entity.getId(), another.getId());
        assertEquals("versions not same.", entity.getVersion(), another.getVersion());
        assertEquals("names not same.", entity.getName(), another.getName());
    }

    /**
     * Test copy constructor - do not copy id and version.
     */
    @Test
    public void copyConstructor3() throws Exception {
        EntityObject another = new EntityObject(entity, false);
        assertTrue("same reference.", entity != another);
        assertEquals("not equal.", entity, another);
        assertEquals("hashCode not same.", entity.hashCode(), another
                .hashCode());
        assertNull("id not null.", another.getId());
        assertNull("version not null.", another.getVersion());
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
        EntityObjectStub another = new EntityObjectStub();
        another.setId(ID_2);
        another.setVersion(VERSION_2);
        assertEquals("not equal.", entity, another);
        assertEquals("hashCode not same.", entity.hashCode(), another
                .hashCode());
    }

    /**
     * Names are different.
     */
    @Test
    public void equalsAndHashCode2() throws Exception {
        EntityObjectStub another = new EntityObjectStub(entity);
        another.setName(NAME_2);
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
        entity.setName(null);
        SerializationUtils.clone(entity);
    }

    /**
     * Simulate data tempering - name is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        entity.setName(NAME_TOO_LONG);
        SerializationUtils.clone(entity);
    }

    /**
     * Simulate data tempering - name isn't correctly formatted.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization4() throws Exception {
        entity.setName(NAME_NOT_CORRECTLY_FORMATTED);
        SerializationUtils.clone(entity);
    }

    @Test
    public void testToString() {
        System.out.println(entity);
    }

}
