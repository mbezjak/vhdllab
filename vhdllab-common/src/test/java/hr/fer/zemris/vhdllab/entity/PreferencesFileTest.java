package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID;
import static hr.fer.zemris.vhdllab.entity.stub.PreferencesFileStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.PreferencesFileStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class PreferencesFileTest extends ValueObjectTestSupport {

    private PreferencesFile entity;

    @Before
    public void initEntity() throws Exception {
        entity = new PreferencesFileStub();
    }

    @Test
    public void basics() {
        PreferencesFile another = new PreferencesFile();
        assertNull("data is set.", another.getData());

        another.setData(DATA);
        assertNotNull("data is null.", another.getData());
        another.setData(null);
        assertNull("data not cleared.", another.getData());
    }

    @Test
    public void constructorNameData() {
        PreferencesFile another = new PreferencesFile(NAME, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertNull(another.getUserId());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void constructorUserIdNameData() {
        PreferencesFile another = new PreferencesFile(USER_ID, NAME, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void copyConstructor() {
        PreferencesFile another = new PreferencesFile(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("UserId not same.", entity.getUserId(), another
                .getUserId());
        assertEquals("data not same.", entity.getData(), another.getData());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
