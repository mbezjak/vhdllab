package hr.fer.zemris.vhdllab.entity;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.ClientLogStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Test;

public class ClientLogTest extends ValueObjectTestSupport {

    @Test
    public void constructor() {
        ClientLog log = new ClientLog("userid");
        assertNotNull("name not set.", log.getName());
        assertNull("type is set.", log.getType());
    }

    @Test
    public void testToString() {
        toStringPrint(new ClientLogStub());
    }

}
