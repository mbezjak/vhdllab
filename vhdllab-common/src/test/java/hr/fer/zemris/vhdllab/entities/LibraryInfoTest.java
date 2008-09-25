package hr.fer.zemris.vhdllab.entities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link LibraryInfo}.
 * 
 * @author Miro Bezjak
 */
public class LibraryInfoTest {

    private LibraryInfo library;

    @Before
    public void initEachTest() throws Exception {
        library = StubFactory.create(LibraryInfo.class, 1);
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        Caseless name = StubFactory.getStubValue("name", 1);
        assertEquals("names not same.", name, library.getName());
    }

    /**
     * Test copy constructor.
     */
    @Test
    public void copyConstructor() throws Exception {
        LibraryInfo another = new LibraryInfo(library);
        assertTrue("same reference.", library != another);
        assertEquals("not equal.", library, another);
        assertEquals("hashCode not same.", library.hashCode(), another
                .hashCode());
    }

    /**
     * Test equals to EntityObject since LibraryInfo doesn't override equals and
     * hashCode.
     */
    @Test
    public void equalsAndHashCode() throws Exception {
        EntityObject info = new EntityObject(library);
        assertTrue("not same.", info.equals(library));
        assertTrue("not same.", library.equals(info));
        assertEquals("hashcode not same.", info.hashCode(), library.hashCode());
    }

    /**
     * Entities are same after deserialization.
     */
    @Test
    public void serialization() throws Exception {
        Object deserialized = SerializationUtils.clone(library);
        assertEquals("not same.", library, deserialized);
    }

    @Test
    public void testToString() {
        System.out.println(library);
    }

}
