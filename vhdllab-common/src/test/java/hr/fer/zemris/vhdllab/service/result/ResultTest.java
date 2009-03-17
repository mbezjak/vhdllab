package hr.fer.zemris.vhdllab.service.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class ResultTest extends ValueObjectTestSupport {

    private Result result;

    @Before
    public void initObject() {
        result = new Result("important data");
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullMessages() {
        new Result((List<String>) null);
    }

    @Test
    public void constructorData() {
        result = new Result("important data");
        assertEquals("important data", result.getData());
        assertTrue(result.getMessages().isEmpty());
    }

    @Test
    public void constructorMessages() {
        List<String> messages = new ArrayList<String>(1);
        messages.add("message");
        result = new Result(messages);
        assertEquals(1, result.getMessages().size());
    }

    @Test
    public void constructorDataMessages() {
        List<String> messages = new ArrayList<String>(2);
        messages.add("message1");
        result = new Result("important data", messages);
        assertEquals("important data", result.getData());
        assertEquals(1, result.getMessages().size());

        messages.add("message2");
        assertEquals(1, result.getMessages().size());
    }

    @SuppressWarnings("unchecked")
    @Test(expected = IllegalArgumentException.class)
    public void constructorDataMessages2() {
        new Result(null, Collections.EMPTY_LIST);
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void getMessages() {
        result.getMessages().add("message");
    }

    @Test
    public void isSuccessful() {
        assertTrue(result.isSuccessful());

        List<String> messages = new ArrayList<String>(2);
        messages.add("message");
        result = new Result("data", messages);
        assertFalse(result.isSuccessful());
    }

    @Test
    public void testToString() {
        toStringPrint(result);
    }

}
