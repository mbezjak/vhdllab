package hr.fer.zemris.vhdllab.entity;

import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub2;
import hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub;
import hr.fer.zemris.vhdllab.entity.stub.ProjectHistoryStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class ProjectHistoryTest extends ValueObjectTestSupport {

    private ProjectHistory entity;

    @Before
    public void initEntity() throws Exception {
        entity = new ProjectHistoryStub();
    }

    @Test
    public void basics() {
        ProjectHistory another = new ProjectHistory();
        assertNull("history is set.", another.getHistory());

        another.setHistory(new HistoryStub());
        assertNotNull("history is null.", another.getHistory());
        another.setHistory(null);
        assertNull("history not cleared.", another.getHistory());
    }

    @Test
    public void constructorProjectInfoHistory() throws Exception {
        History history = new HistoryStub();
        ProjectHistory another = new ProjectHistory(new OwnedEntityStub(),
                history);
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("history not same.", history, another.getHistory());
        assertNull("id must not be copied.", another.getId());
        assertNull("version must not be copied.", another.getVersion());
    }

    @Test
    public void copyConstructor() {
        ProjectHistory another = new ProjectHistory(entity);
        assertEquals("userId not same.", entity.getUserId(), another
                .getUserId());
        assertEquals("history not same.", entity.getHistory(), another
                .getHistory());
    }

    @Test(expected = NullPointerException.class)
    public void copyConstructorNullArgument() {
        new ProjectHistory(null);
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        ProjectHistory another = new ProjectHistory(entity);
        assertEqualsAndHashCode(entity, another);

        another.setHistory(new HistoryStub2());
        assertNotEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
