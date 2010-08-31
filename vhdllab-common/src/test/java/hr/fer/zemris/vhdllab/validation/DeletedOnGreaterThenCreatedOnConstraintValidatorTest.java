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

import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.CREATED_ON;
import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.DELETED_ON;
import static hr.fer.zemris.vhdllab.entity.stub.HistoryStub.DELETED_ON_BEFORE_CREATED_ON;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.History;

import org.hibernate.validator.Validator;
import org.junit.Test;

public class DeletedOnGreaterThenCreatedOnConstraintValidatorTest {

    private final Validator<?> validator = new DeletedOnGreaterThenCreatedOnConstraintValidator();

    @Test
    public void isValidParametarNotFile() {
        assertFalse("string object is valid.", validator
                .isValid("a string object"));
    }

    @Test
    public void isValidNullDeletedOn() {
        assertTrue("not valid when deleted on is null.", validator
                .isValid(new History()));
    }

    @Test
    public void isValid() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(DELETED_ON);
        assertTrue("not valid when deleted on is greater then created on.",
                validator.isValid(history));
    }

    @Test
    public void isValidCreatedOnEqualsDeletedOn() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(CREATED_ON);
        assertFalse("valid when created on equals deleted on.", validator
                .isValid(history));
    }

    @Test
    public void isValidDeletedOnBeforeCreatedOnCreatedOn() {
        History history = new History();
        history.setCreatedOn(CREATED_ON);
        history.setDeletedOn(DELETED_ON_BEFORE_CREATED_ON);
        assertFalse("valid when deleted on before created on.", validator
                .isValid(history));
    }

}
