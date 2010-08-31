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
package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.SimulatableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;

import java.util.List;

import org.apache.commons.collections.Predicate;
import org.springframework.richclient.core.Guarded;

public class WorkspaceHasSimulatableFileCommandGuard extends
        WorkspaceNotEmptyCommandGuard {

    public WorkspaceHasSimulatableFileCommandGuard(WorkspaceManager manager,
            WorkspaceInitializer initializer, Guarded guarded) {
        super(manager, initializer, guarded);
    }

    @Override
    protected void updateEnabledState() {
        Predicate filter = new NotEmptyProjectFilter(manager,
                SimulatableFilesFilter.getInstance());
        List<Project> projects = manager.getProjects(filter, null);
        setEnabled(!projects.isEmpty());
    }

}
