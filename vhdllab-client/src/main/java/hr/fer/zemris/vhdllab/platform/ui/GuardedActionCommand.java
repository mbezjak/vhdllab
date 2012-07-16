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
package hr.fer.zemris.vhdllab.platform.ui;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public abstract class GuardedActionCommand extends ActionCommand {

    @Autowired
    protected WorkspaceManager workspaceManager;
    @Autowired
    protected SimulationManager simulationManager;
    @Autowired
    protected WorkspaceInitializer workspaceInitializer;

    public GuardedActionCommand() {
        setId(BeanUtil.beanName(getClass()));
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installCommandGuard(workspaceManager, workspaceInitializer,
                simulationManager);
    }

    /**
     * Subclasses may override this method to install a command guard.
     * 
     * @param wm
     *            a workspace manager
     * @param wi
     *            a workspace initializer
     * @param sm
     *            a simulation manager
     */
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        // default no implementation, subclasses may override
    }

}
