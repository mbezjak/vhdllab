package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.FileResourceStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link FileResource}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileResourceTest {

    private FileResourceStub file;

    @Before
    public void initEachTest() throws Exception {
        file = new FileResourceStub();
    }

    /**
     * Type is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new FileResource(null, NAME, DATA);
    }

    /**
     * FileResource is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new FileResource(null, true);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        FileResource another = new FileResource(file, true);
        assertTrue("same reference.", file != another);
        assertEquals("not equal.", file, another);
        assertEquals("hashCode not same.", file.hashCode(), another.hashCode());
        assertEquals("types not same.", file.getType(), another.getType());
    }

    /**
     * Test equals to Resource since FileResource doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        Resource entity = new Resource(file, true);
        assertTrue("not same.", entity.equals(file));
        assertTrue("not same.", file.equals(entity));
        assertEquals("hashcode not same.", entity.hashCode(), file.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(file);
        assertEquals("not same.", file, deserialized);
    }

    /**
     * Simulate data tempering - type is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        file.setType(null);
        SerializationUtils.clone(file);
    }

    @Test
    public void testToString() {
        System.out.println(file);
    }

}
