package hr.fer.zemris.vhdllab.service.result;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class CompilationMessageTest extends ValueObjectTestSupport {

    private CompilationMessage message;

    @Before
    public void initObject() {
        message = new CompilationMessage("entity_name", 3, 57, "a message text");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullText() {
        new CompilationMessage("entity_name", 1, 1, null);
    }

    @Test
    public void constructor() {
        assertEquals("entity_name", message.getEntityName());
        assertEquals(3, message.getRow());
        assertEquals(57, message.getColumn());
        assertEquals("a message text", message.getText());
    }

    @Test
    public void testToString() {
        toStringPrint(message);
        assertEquals("[entity_name][3,57]a message text", message.toString());

        message = new CompilationMessage(null, -1, -1, "another text");
        toStringPrint(message);
        assertEquals("[-1,-1]another text", message.toString());
    }

}
