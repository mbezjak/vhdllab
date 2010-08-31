/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.service.result;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Test;

public class CompilationMessageTest extends ValueObjectTestSupport {

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullText() {
        new CompilationMessage(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullText2() {
        new CompilationMessage("entity_name", 1, 1, null);
    }

    @Test
    public void constructorText() {
        CompilationMessage message = new CompilationMessage("a message text");
        assertNull(message.getEntityName());
        assertEquals(0, message.getRow());
        assertEquals(0, message.getColumn());
        assertEquals("a message text", message.getText());
    }

    @Test
    public void constructor() {
        CompilationMessage message = new CompilationMessage("entity_name", 3,
                57, "a message text");
        assertEquals("entity_name", message.getEntityName());
        assertEquals(3, message.getRow());
        assertEquals(57, message.getColumn());
        assertEquals("a message text", message.getText());
    }

    @Test
    public void testToString() {
        CompilationMessage message = new CompilationMessage("entity_name", 3,
                57, "a message text");
        toStringPrint(message);
        assertEquals("[entity_name][3,57]a message text", message.toString());

        message = new CompilationMessage(null, -1, -1, "another text");
        toStringPrint(message);
        assertEquals("[-1,-1]another text", message.toString());
    }

}
