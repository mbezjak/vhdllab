package hr.fer.zemris.vhdllab.api.results;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * A test case for {@link CompilationResult}.
 * 
 * @author Miro Bezjak
 */
public class CompilationResultTest {

    private static final Integer STATUS = Integer.valueOf(5);
    private static final boolean SUCCESSFUL = false;
    private static final List<CompilationMessage> MESSAGES = Collections
            .emptyList();

    private static CompilationResult result;

    @Before
    public void initEachTest() {
        result = new CompilationResult(STATUS, SUCCESSFUL, MESSAGES);
    }
    
    /**
     * Result is null
     */
    @Test(expected = NullPointerException.class)
    public void copyConstructor() {
        new CompilationResult(null);
    }
    
    /**
     * Everything ok.
     */
    @Test
    public void copyConstructor2() {
        CompilationResult copiedResult = new CompilationResult(result);
        assertEquals("result objects not same.", result, copiedResult);
    }
    
    /**
     * Result is null or equals to wrong object type.
     */
    @Test
    public void equals() {
        assertFalse("result equals to null.", result.equals(null));
        assertFalse("result equals to string object.", result
                .equals("a string object"));
        assertFalse("compilation result equals to result object.", result
                .equals(new Result<CompilationMessage>(result)));
    }

    /**
     * Simulate serialization tempering
     */
    @Test(expected = NullPointerException.class)
    public void readResolve() throws Exception {
        Field field = result.getClass().getSuperclass().getDeclaredField(
                "status");
        field.setAccessible(true);
        field.set(result, null); // set illegal state of message object

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(result);

        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        bis.readObject();
    }

    /**
     * Deserialization returns object of type CompilationResult.
     */
    @Test
    public void readResolve2() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(result);
        
        ObjectInputStream bis = new ObjectInputStream(new ByteArrayInputStream(
                bos.toByteArray()));
        Object readObject = bis.readObject();
        assertTrue("wrong object type.", readObject.getClass() == CompilationResult.class);
        assertEquals("result objects not same.", result, readObject);
    }
    
}
