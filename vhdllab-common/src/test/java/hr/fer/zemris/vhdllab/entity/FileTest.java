package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME_DIFFERENT;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.ProjectStub;
import hr.fer.zemris.vhdllab.entity.stub.ProjectStub2;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class FileTest extends ValueObjectTestSupport {

    private File entity;

    @Before
    public void initEntity() {
        Project project = new ProjectStub();
        entity = (File) CollectionUtils.get(project.getFiles(), 0);
    }

    @Test
    public void basics() {
        File another = new File();
        assertNull("project is set.", another.getProject());
        assertNull("post remove project reference is set.", another
                .getPostRemoveProjectReference());

        another.setProject(new ProjectStub2());
        assertNotNull("project is null.", another.getProject());
        another.setProject(null);
        assertNull("project not cleared.", another.getProject());
    }

    @Test
    public void copyConstructor() {
        File another = new File(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("type not same.", entity.getType(), another.getType());
        assertEquals("data not same.", entity.getData(), another.getData());
        assertNull("project not null.", another.getProject());
        assertNull("post remove project reference not null.", another
                .getPostRemoveProjectReference());
    }

    @Test
    public void setProject() {
        Project anotherProject = new ProjectStub2();
        entity.setProject(anotherProject);
        assertEquals("projects not same.", anotherProject, entity.getProject());
        assertNull("post remove project reference not null.", entity
                .getPostRemoveProjectReference());

        entity.setProject(null);
        assertNull("project not null.", entity.getProject());
        assertEquals("post remove project reference not set.", anotherProject,
                entity.getPostRemoveProjectReference());
    }

    @Test
    public void getPostRemoveProjectReference() {
        entity.setProject(null);
        assertNotNull("post remove project reference is null.", entity
                .getPostRemoveProjectReference());
        assertNull("post remove project reference not null.", entity
                .getPostRemoveProjectReference());
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        File another = new File(entity);
        another.setProject(entity.getProject());
        assertEqualsAndHashCode(entity, another);

        another.setName(NAME_DIFFERENT);
        assertNotEqualsAndHashCode(entity, another);

        another = new File(entity);
        another.setProject(new ProjectStub2());
        assertNotEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
