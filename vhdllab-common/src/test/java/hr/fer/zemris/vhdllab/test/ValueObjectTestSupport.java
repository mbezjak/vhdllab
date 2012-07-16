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
package hr.fer.zemris.vhdllab.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class ValueObjectTestSupport {

    protected void assertEqualsAndHashCode(Object expected, Object actual) {
        assertTrue("not equal.", expected.equals(actual));
        assertTrue("hash code not same.", expected.hashCode() == actual
                .hashCode());
    }

    protected void assertNotEqualsAndHashCode(Object expected, Object actual) {
        assertFalse("equal when shouldn't.", expected.equals(actual));
        assertFalse("hash code same but shouldn't.",
                expected.hashCode() == actual.hashCode());
    }

    protected void basicEqualsTest(Object obj) {
        assertTrue("not equal with self.", obj.equals(obj));
        assertFalse("equal with null.", obj.equals(null));
        assertFalse("equal with a string object.", obj
                .equals("a string object"));
    }

    protected void toStringPrint(Object obj) {
        System.out.println(obj);
    }

}
