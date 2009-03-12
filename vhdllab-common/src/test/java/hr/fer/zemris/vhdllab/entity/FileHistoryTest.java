package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub.PROJECT_ID;
import static hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub.PROJECT_ID_DIFFERENT;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub;
import hr.fer.zemris.vhdllab.entity.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub2;
import hr.fer.zemris.vhdllab.entity.stub.ProjectStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class FileHistoryTest extends ValueObjectTestSupport {

    private FileHistory entity;

    @Before
    public void initEntity() {
        entity = new FileHistoryStub();
    }

    @Test
    public void basics() {
        FileHistory another = new FileHistory();
        assertNull("project id is set.", another.getProjectId());
        assertNull("history is set.", another.getHistory());

        another.setProjectId(PROJECT_ID);
        assertNotNull("project id is null.", another.getProjectId());
        another.setProjectId(null);
        assertNull("project id not cleared.", another.getProjectId());

        another.setHistory(new HistoryStub());
        assertNotNull("history is null.", another.getHistory());
        another.setHistory(null);
        assertNull("history not cleared.", another.getHistory());
    }

    @Test
    public void constructorFileHistory() {
        History history = new HistoryStub();
        File file = (File) CollectionUtils.get(new ProjectStub().getFiles(), 0);
        FileHistory another = new FileHistory(file, history);
        assertEquals("name not same.", file.getName(), another.getName());
        assertEquals("project id not same.", file.getProject().getId(), another
                .getProjectId());
        assertEquals("history not same.", history, another.getHistory());
    }

    @Test
    public void constructorFileInfoProjectIdHistory() {
        History history = new HistoryStub();
        FileHistory another = new FileHistory(new FileInfoStub(), PROJECT_ID,
                history);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("project id not same.", PROJECT_ID, another.getProjectId());
        assertEquals("history not same.", history, another.getHistory());
    }

    @Test
    public void copyConstructor() {
        FileHistory another = new FileHistory(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("project id not same.", entity.getProjectId(), another
                .getProjectId());
        assertEquals("history not same.", entity.getHistory(), another
                .getHistory());
    }

    @Test(expected = NullPointerException.class)
    public void copyConstructorNullArgument() {
        new FileHistory(null);
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        FileHistory another = new FileHistory(entity);
        assertEqualsAndHashCode(entity, another);

        another.setProjectId(PROJECT_ID_DIFFERENT);
        assertNotEqualsAndHashCode(entity, another);

        another = new FileHistory(entity);
        another.setHistory(new HistoryStub2());
        assertNotEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
