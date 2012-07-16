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
package hr.fer.zemris.vhdllab.platform.ui.rule;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.lang.Validate;
import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;

public class TestbenchFileExistsConstraint extends AbstractPropertyConstraint {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager workspaceManager;

    public TestbenchFileExistsConstraint(WorkspaceManager workspaceManager) {
        Validate.notNull(workspaceManager, "Workspace Manager can't be null");
        this.workspaceManager = workspaceManager;
    }

    @Override
    protected boolean test(PropertyAccessStrategy access) {
        File targetFile = (File) access.getPropertyValue("targetFile");
        if (targetFile == null) {
            return true;
        }
        String testbenchName = (String) access.getPropertyValue("testbenchName");
        // File type and data is irrelevant
        File file = new File(testbenchName, FileType.TESTBENCH, "");
        file.setProject(targetFile.getProject());
        return !workspaceManager.exist(file);
    }
}
