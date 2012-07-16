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
package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileTarget;
import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

public class TestbenchFile extends FileTarget {

    @NameFormatConstraint
    @NotEmpty
    @Length(max = 255)
    private String testbenchName;

    public String getTestbenchName() {
        return testbenchName;
    }

    public void setTestbenchName(String testbenchName) {
        this.testbenchName = testbenchName;
    }

}
