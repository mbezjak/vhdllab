package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.FileInfoStub.DATA;
import static hr.fer.zemris.vhdllab.entity.stub.FileInfoStub.TYPE;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class FileInfoTest extends ValueObjectTestSupport {

    private FileInfo entity;

    @Before
    public void initEntity() throws Exception {
        entity = new FileInfoStub();
    }

    @Test
    public void basics() {
        FileInfo another = new FileInfo();
        assertNull("type is set.", another.getType());
        assertNull("data is set.", another.getData());

        another.setType(TYPE);
        assertNotNull("type is null.", another.getType());
        another.setType(null);
        assertNull("type not cleared.", another.getType());

        another.setData(DATA);
        assertNotNull("type is null.", another.getData());
        another.setData(null);
        assertNull("type not cleared.", another.getData());
    }

    @Test
    public void constructor() {
        FileInfo another = new FileInfo(NAME, TYPE, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("type not same.", TYPE, another.getType());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void copyConstructor() {
        FileInfo another = new FileInfo(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("type not same.", entity.getType(), another.getType());
        assertEquals("data not same.", entity.getData(), another.getData());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
