package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for Resource.
 * 
 * @author Miro Bezjak
 */
public class ResourceTest {

    private static final Caseless NAME = StubFactory.getStubValue("name", 1);
    private Resource res;
    
    @Before
    public void initEachTest() throws Exception {
        res = StubFactory.create(Resource.class, 1);
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
        String data = StubFactory.getStubValue("data", 301);
        new Resource(NAME, data);
    }

    /**
     * Resource is null.
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() throws Exception {
        new Resource(null);
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor2() throws Exception {
        Resource another = new Resource(res);
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
        EntityObject entity = new EntityObject(res);
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
        StubFactory.setProperty(res, "data", 300);
        SerializationUtils.clone(res);
    }

    /**
     * Simulate data tempering - data is too long.
     */
    @Test(expected = IllegalArgumentException.class)
    public void serialization3() throws Exception {
        StubFactory.setProperty(res, "data", 301);
        SerializationUtils.clone(res);
    }

    @Test
    public void testToString() {
        System.out.println(res);
    }

}
