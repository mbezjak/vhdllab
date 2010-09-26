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
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.ui.wizard.simulator.SimulationFile;
import hr.fer.zemris.vhdllab.platform.ui.wizard.testbench.TestbenchFile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.rules.Rules;
import org.springframework.rules.constraint.Constraint;
import org.springframework.rules.support.DefaultRulesSource;

public class ObjectValidationRulesSource extends DefaultRulesSource {

    @Autowired
    WorkspaceManager workspaceManager;

    public ObjectValidationRulesSource() {
        super();
        addRules(createProjectRules());
        addRules(createFileRules());
        addRules(createTestbenchFileRules());
        addRules(createSimulationFileRules());
    }

    private Rules createProjectRules() {
        return new Rules(Project.class) {
            @Override
            protected void initRules() {
                add("name", new Constraint[] { new ProjectExistsConstraint(
                        workspaceManager) });
            }
        };
    }

    private Rules createFileRules() {
        return new Rules(File.class) {
            @Override
            protected void initRules() {
                add("name", new Constraint[] { new FileExistsConstraint(
                        workspaceManager) });
            }
        };
    }

    private Rules createTestbenchFileRules() {
        return new Rules(TestbenchFile.class) {
            @Override
            protected void initRules() {
                add("testbenchName",
                        new Constraint[] { new TestbenchFileExistsConstraint(
                                workspaceManager) });
            }
        };
    }

    private Rules createSimulationFileRules() {
        return new Rules(SimulationFile.class) {
            @Override
            protected void initRules() {
                add("simulationName",
                        new Constraint[] { new SimulationFileExistsConstraint(
                                workspaceManager) });
            }
        };
    }
    
}
