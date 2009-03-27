package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.PredefinedFilesDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.WorkspaceService;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.extractor.MetadataExtractor;
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

import javax.annotation.Resource;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;

public class WorkspaceServiceImpl extends ServiceSupport implements
        WorkspaceService {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private FileDao fileDao;
    @Autowired
    private PredefinedFilesDao predefinedFilesDao;
    @Resource(name = "fileTypeBasedMetadataExtractor")
    private MetadataExtractor metadataExtractor;

    @Override
    public FileReport save(File file) {
        if (FileType.SOURCE.equals(file.getType())) {
            CircuitInterface ci = metadataExtractor
                    .extractCircuitInterface(file);
            if (!ci.getName().equalsIgnoreCase(file.getName().toString())) {
                throw new IllegalStateException("Resource " + file.getName()
                        + " must have only one entity with the same name.");
            }
        }

        File saved = new File(file);
        if (file.isNew()) {
            fileDao.persist(saved);
        } else {
            saved = fileDao.merge(saved);
        }
        return getReport(saved);
    }

    @Override
    public FileReport deleteFile(Integer fileId) {
        File file = fileDao.load(fileId);
        fileDao.delete(file);
        return getReport(file);
    }

    @Override
    public File findByName(Integer projectId, String name) {
        File file = findProjectOrPredefinedFile(projectId, name);
        return new File(file, EntityUtils.lightweightClone(file.getProject()));
    }

    private FileReport getReport(File file) {
        Hierarchy hierarchy = extractHierarchy(file.getProject().getId());
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
        Project preferencesProject = projectDao.getPreferencesProject(user);

        return new Workspace(projectMetadata, predefinedFiles,
                preferencesProject.getFiles());
    }

    public void savePreferences(List<File> files) {
        Validate.notNull(files, "Files can't be null");
        Project project = projectDao.getPreferencesProject(SecurityUtils
                .getUser());
        Set<File> allFiles = EntityUtils.cloneFiles(project.getFiles());
        for (File file : files) {
            if (!allFiles.contains(file)) {
                project.addFile(new File(file));
            }
        }
        projectDao.merge(project);
    }
}
