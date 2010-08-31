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
package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;
import hr.fer.zemris.vhdllab.service.workspace.ProjectMetadata;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;

public class WorkspaceServiceImpl extends ServiceSupport implements
        WorkspaceService {

    @Override
    public FileReport createFile(Integer projectId, String name, FileType type,
            String data) {
        File file = new File(name, type, data);
        loadProject(projectId).addFile(file);
        fileDao.persist(file);
        return getReport(file, file.getProject());
    }

    @Override
    public FileReport saveFile(Integer fileId, String data) {
        File file = loadFile(fileId);
        file.setData(data);
        checkCircuitInterfaceName(file);
        File saved = fileDao.merge(file);
        return getReport(saved, saved.getProject());
    }

    private void checkCircuitInterfaceName(File file) {
        if (FileType.SOURCE.equals(file.getType())) {
            CircuitInterface ci = metadataExtractor
                    .extractCircuitInterface(file);
            if (!ci.getName().equalsIgnoreCase(file.getName())) {
                throw new IllegalStateException("Resource " + file.getName()
                        + " must have only one entity with the same name.");
            }
        }
    }

    @Override
    public FileReport deleteFile(Integer fileId) {
        File file = loadFile(fileId);
        // project reference will be removed during delete method
        Project project = file.getProject();
        fileDao.delete(file);
        return getReport(file, project);
    }

    @Override
    public File findByName(Integer projectId, String name) {
        File file = findProjectOrPredefinedFile(projectId, name);
        if (file == null) {
            return null;
        }
        return new File(file, EntityUtils.lightweightClone(file.getProject()));
    }

    private FileReport getReport(File file, Project project) {
        Hierarchy hierarchy = extractHierarchy(project.getId());
        return new FileReport(file, hierarchy);
    }

    @Override
    public Project persist(String name) {
        Validate.notNull(name, "Project can't be null");
        Project project = new Project(SecurityUtils.getUser(), name);
        projectDao.persist(project);
        return EntityUtils.lightweightClone(project);
    }

    @Override
    public void deleteProject(Integer projectId) {
        Project project = loadProject(projectId);
        projectDao.delete(project);
    }

    @Override
    public Hierarchy extractHierarchy(Integer projectId) {
        Project project = loadProject(projectId);
        Set<File> files = project.getFiles();
        Map<File, HierarchyNode> resolvedNodes = new HashMap<File, HierarchyNode>(
                files.size());
        for (File file : files) {
            if (resolvedNodes.containsKey(file)) {
                continue;
            }
            HierarchyNode node = new HierarchyNode(file, null);
            resolvedNodes.put(file, node);
            resolveHierarchy(file, resolvedNodes);
        }
        Hierarchy hierarchy = new Hierarchy(project,
                new HashSet<HierarchyNode>(resolvedNodes.values()));
        return hierarchy;
    }

    /**
     * Resolves a hierarchy tree for specified file. Resolved hierarchy trees
     * gets added to <code>resolvedNodes</code> parameter.
     * 
     * @param file
     *            a file for whom to resolve hierarchy tree
     * @param resolvedNodes
     *            contains hierarchy tree for all resolved files
     */
    private void resolveHierarchy(File file,
            Map<File, HierarchyNode> resolvedNodes) {
        /*
         * This method resolves a hierarchy by recursively invoking itself.
         */
        Set<String> dependencies;
        try {
            dependencies = metadataExtractor.extractDependencies(file);
        } catch (DependencyExtractionException e) {
            dependencies = Collections.emptySet();
        }
        for (String name : dependencies) {
            File dep = findProjectOrPredefinedFile(file.getProject().getId(),
                    name);
            if (dep == null)
                continue;
            HierarchyNode parent = resolvedNodes.get(file);
            if (resolvedNodes.containsKey(dep)) {
                HierarchyNode depNode = resolvedNodes.get(dep);
                parent.addDependency(depNode);
            } else {
                HierarchyNode depNode = new HierarchyNode(dep, parent);
                resolvedNodes.put(dep, depNode);
                resolveHierarchy(dep, resolvedNodes);
            }
        }
    }

    @Override
    public Workspace getWorkspace() {
        String user = SecurityUtils.getUser();
        List<Project> projects = projectDao.findByUser(user);

        Map<Project, ProjectMetadata> projectMetadata = new LinkedHashMap<Project, ProjectMetadata>(
                projects.size());
        for (Project project : projects) {
            // projectMetadata.put(project, null);
            Hierarchy hierarchy = extractHierarchy(project.getId());
            projectMetadata.put(project, new ProjectMetadata(hierarchy, project
                    .getFiles()));
        }
        // if (!projects.isEmpty()) {
        // Project last = projects.get(projects.size() - 1);
        // Hierarchy hierarchy = extractHierarchy(last.getId());
        // projectMetadata.put(last, new ProjectMetadata(hierarchy, last
        // .getFiles()));
        // }

        Set<File> predefinedFiles = predefinedFilesDao.getPredefinedFiles();
        List<PreferencesFile> preferencesFiles = preferencesFileDao
                .findByUser(user);

        return new Workspace(projectMetadata, predefinedFiles, preferencesFiles);
    }

}
