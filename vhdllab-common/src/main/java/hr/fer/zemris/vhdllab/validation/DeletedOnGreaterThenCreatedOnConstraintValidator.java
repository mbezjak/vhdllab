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

import hr.fer.zemris.vhdllab.entity.History;

import java.util.Date;

import org.hibernate.validator.Validator;

public class DeletedOnGreaterThenCreatedOnConstraintValidator implements
        Validator<DeletedOnGreaterThenCreatedOnConstraint> {

    @Override
    public void initialize(DeletedOnGreaterThenCreatedOnConstraint parameters) {
    }

    @Override
    public boolean isValid(Object value) {
        if (value instanceof History) {
            History history = (History) value;
            Date deletedOn = history.getDeletedOn();
            Date createdOn = history.getCreatedOn();
            return deletedOn != null ? deletedOn.compareTo(createdOn) > 0
                    : true;
        }
        return false;
    }

}
