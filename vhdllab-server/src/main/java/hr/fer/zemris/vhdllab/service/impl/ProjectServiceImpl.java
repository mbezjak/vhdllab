package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.PredefinedFilesDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;
import hr.fer.zemris.vhdllab.service.ProjectService;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;
import hr.fer.zemris.vhdllab.util.EntityUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class ProjectServiceImpl implements ProjectService {

    @Autowired
    private FileService fileService;
    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private PredefinedFilesDao predefinedFilesDao;
    @Resource(name = "metadataExtractionService")
    private MetadataExtractionService extractionService;

    @Override
    public Project persist(Project project) {
        Validate.notNull(project, "Project can't be null");
        Validate.isTrue(!project.isNew(), "Project must be new");
        projectDao.persist(project);
        Project persisted = new Project(project);
        persisted.setFiles(null);
        return persisted;
    }

    @Override
    public void delete(Integer projectId) {
        Project project = projectDao.load(projectId);
        projectDao.delete(project);
    }

    @Override
    public Hierarchy extractHierarchy(Integer projectId) {
        Project project = projectDao.load(projectId);
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
            dependencies = extractionService.extractDependencies(file.getId());
        } catch (DependencyExtractionException e) {
            dependencies = Collections.emptySet();
        }
        for (String name : dependencies) {
            File dep = fileService.findByName(file.getProject().getId(), name);
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
        EntityUtils.setNullFiles(projects);

        Hierarchy hierarchy = null;
        if (!projects.isEmpty()) {
            Project last = projects.get(projects.size() - 1);
            hierarchy = extractHierarchy(last.getId());
        }

        Project predefinedProject = new Project(user, "predefined");
        predefinedProject.setFiles(predefinedFilesDao.getPredefinedFiles());

        Project preferencesProject = projectDao.getPreferencesProject(user);
        return new Workspace(projects, hierarchy, predefinedProject,
                preferencesProject.getFiles());
    }

}
