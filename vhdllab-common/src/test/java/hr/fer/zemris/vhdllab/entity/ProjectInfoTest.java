package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub.TYPE;
import static hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub.TYPE_DIFFERENT;
import static hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub.USER_ID;
import static hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub.USER_ID_DIFFERENT;
import static hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub.USER_ID_UPPERCASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.ProjectInfoStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class ProjectInfoTest extends ValueObjectTestSupport {

    private ProjectInfo entity;

    @Before
    public void initEntity() {
        entity = new ProjectInfoStub();
    }

    @Test
    public void basics() {
        ProjectInfo another = new ProjectInfo();
        assertNull("userId is set.", another.getUserId());
        assertNotNull("type not set.", another.getType());

        another.setUserId(USER_ID);
        assertNotNull("userId is null.", another.getUserId());
        another.setUserId(null);
        assertNull("userId not cleared.", another.getUserId());

        another.setType(TYPE);
        assertNotNull("type is null.", another.getType());
        another.setType(null);
        assertNull("type not cleared.", another.getType());
    }

    @Test
    public void constructorUserId() {
        ProjectInfo another = new ProjectInfo(USER_ID);
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertNull("name not null", another.getName());
        assertEquals("type not same.", ProjectType.USER, another.getType());
    }

    @Test
    public void constructorUserIdAndName() {
        ProjectInfo another = new ProjectInfo(USER_ID, NAME);
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("type not same.", ProjectType.USER, another.getType());
    }

    @Test
    public void copyConstructor() {
        ProjectInfo another = new ProjectInfo(entity);
        assertEquals("userId not same.", entity.getUserId(), another
                .getUserId());
        assertEquals("userId not same.", entity.getType(), another.getType());
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        ProjectInfo another = new ProjectInfo(entity);
        assertEqualsAndHashCode(entity, another);

        another.setUserId(USER_ID_DIFFERENT);
        assertNotEqualsAndHashCode(entity, another);

        another.setUserId(USER_ID_UPPERCASE);
        assertEqualsAndHashCode(entity, another);

        another = new ProjectInfo(entity);
        another.setType(TYPE_DIFFERENT);
        assertNotEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
