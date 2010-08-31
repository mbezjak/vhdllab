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
package hr.fer.zemris.vhdllab.platform.manager.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;

import java.util.List;
import java.util.Set;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;

public interface WorkspaceManager extends EventPublisher<WorkspaceListener> {

    void create(File file) throws FileAlreadyExistsException;

    void save(File file);

    void delete(File file);

    void create(Project project) throws ProjectAlreadyExistsException;

    void delete(Project project);

    List<Project> getProjects();

    List<Project> getProjects(Predicate filter, Transformer transformer);

    Set<File> getFilesForProject(Project project);

    Set<File> getFilesForProject(Project project, Predicate filter,
            Transformer transformer);

    boolean isEmpty(Project project, Predicate filter);

    Hierarchy getHierarchy(Project project);

    boolean exist(Project project);

    boolean exist(File file);

    Workspace getWorkspace();

}
