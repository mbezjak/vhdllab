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
package hr.fer.zemris.vhdllab.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.service.ci.Port;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class ConsistentRangeConstraintValidatorTest {

    private final Validator<?> validator = new ConsistentRangeConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValid() {
        Port port = new Port();
        port.setFrom(null);
        port.setTo(null);
        assertTrue(validator.isValid(port));
    }

    @Test
    public void isValid2() {
        Port port = new Port();
        port.setFrom(2);
        port.setTo(2);
        assertTrue(validator.isValid(port));
    }

    @Test
    public void isValid3() {
        Port port = new Port();
        port.setFrom(null);
        port.setTo(2);
        assertFalse(validator.isValid(port));
    }

    @Test
    public void isValid4() {
        Port port = new Port();
        port.setFrom(2);
        port.setTo(null);
        assertFalse(validator.isValid(port));
    }

}
