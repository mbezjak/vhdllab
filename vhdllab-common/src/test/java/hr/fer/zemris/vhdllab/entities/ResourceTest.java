package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static hr.fer.zemris.vhdllab.entities.stub.IResourceStub.DATA_TOO_LONG;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.ResourceStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link Resource}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ResourceTest {

    private ResourceStub res;
    
    @Before
    public void initEachTest() throws Exception {
        res = new ResourceStub();
    }

    /**
     * Data is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructor() throws Exception {
        new Resource(NAME, null);
    }

    /**
     * Data is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void constructor2() throws Exception {
        new Resource(NAME, DATA_TOO_LONG);
    }

    /**
     * Resource is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new Resource(null, true);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        Resource another = new Resource(res, true);
        assertTrue("same reference.", res != another);
        assertEquals("not equal.", res, another);
        assertEquals("hashCode not same.", res.hashCode(), another.hashCode());
        assertEquals("data not same.", res.getData(), another.getData());
    }

    /**
     * Test equals to EntityObject since Resource doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        EntityObject entity = new EntityObject(res, true);
        assertTrue("not same.", entity.equals(res));
        assertTrue("not same.", res.equals(entity));
        assertEquals("hashcode not same.", entity.hashCode(), res.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(res);
        assertEquals("not same.", res, deserialized);
    }

    /**
     * Simulate data tempering - data is null.
     */
    @Test(expected = NullPointerException.class)
    public void serialization2() throws Exception {
        res.setData(null);
        SerializationUtils.clone(res);
    }

    /**
     * Simulate data tempering - data is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        res.setData(DATA_TOO_LONG);
        SerializationUtils.clone(res);
    }

    @Test
    public void testToString() {
        System.out.println(res);
    }

}
