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
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.ui.wizard.simulator.SimulationFile;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileTarget;
import hr.fer.zemris.vhdllab.platform.ui.wizard.testbench.TestbenchFile;

import org.springframework.binding.PropertyAccessStrategy;
import org.springframework.rules.constraint.property.AbstractPropertyConstraint;

public class FileNameDiffersFromProjectConstraint extends AbstractPropertyConstraint {

    private static final long serialVersionUID = 1L;

    @Override
    protected boolean test(PropertyAccessStrategy access) {
        String projectName = projectName(access);
        String resourceName = resourceName(access);

        boolean valid = true;
        if (projectName != null && resourceName != null) {
            valid = !resourceName.equalsIgnoreCase(projectName);
        }

        return valid;
    }

    private String projectName(PropertyAccessStrategy access) {
        Object domain = access.getDomainObject();
        String name = null;

        if (domain instanceof FileTarget) {
            File target = (File) access.getPropertyValue("targetFile");

            if (target != null) {
                name = target.getProject().getName();
            }
        } else if (domain instanceof File) {
            Project project = (Project) access.getPropertyValue("project");

            if (project != null) {
                name = project.getName();
            }
        }

        return name;
    }

    private String resourceName(PropertyAccessStrategy access) {
        Object domain = access.getDomainObject();
        String name = null;

        if (domain instanceof File) {
            name = (String) access.getPropertyValue("name");
        } else if (domain instanceof TestbenchFile) {
            name = (String) access.getPropertyValue("testbenchName");
        } else if (domain instanceof SimulationFile) {
            name = (String) access.getPropertyValue("simulationName");
        }

        return name;
    }

}
