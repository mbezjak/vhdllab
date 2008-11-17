package hr.fer.zemris.vhdllab.entities;

import static hr.fer.zemris.vhdllab.entities.stub.IEntityObjectStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entities.stub.LibraryInfoStub;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link LibraryInfo}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class LibraryInfoTest {

    private LibraryInfoStub library;

    @Before
    public void initEachTest() throws Exception {
        library = new LibraryInfoStub();
    }

    /**
     * Test that properties are correctly set in constructor.
     */
    @Test
    public void constructor() throws Exception {
        assertEquals("names not same.", NAME, library.getName());
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
