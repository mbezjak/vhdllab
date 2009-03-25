package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_2;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_UPPERCASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class NamedEntityTest extends ValueObjectTestSupport {

    private NamedEntity entity;

    @Before
    public void initEntity() throws Exception {
        entity = new NamedEntityStub();
    }

    @Test
    public void basics() {
        NamedEntity another = new NamedEntity();
        assertNull("name is set.", another.getName());

        another.setName(NAME);
        assertNotNull("name is null.", another.getName());
        another.setName(null);
        assertNull("name not cleared.", another.getName());
    }

    @Test
    public void constructorName() {
        NamedEntity another = new NamedEntity(NAME);
        assertEquals("name not same.", NAME, another.getName());
    }

    @Test
    public void copyConstructor() {
        NamedEntity another = new NamedEntity(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        NamedEntity another = new NamedEntity(entity);
        assertEqualsAndHashCode(entity, another);

        another.setName(NAME_2);
        assertNotEqualsAndHashCode(entity, another);

        another.setName(NAME_UPPERCASE);
        assertEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
