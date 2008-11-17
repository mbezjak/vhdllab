package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;
import hr.fer.zemris.vhdllab.service.FileService;
import hr.fer.zemris.vhdllab.service.HierarchyExtractor;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;

public class HierarchyExtractionService implements HierarchyExtractor {
    
    @Autowired
    private FileService fileService;
    @Resource(name = "dependencyExtractionService")
    private DependencyExtractor dependencyExtractor;

    @Override
    public Hierarchy extract(ProjectInfo project) {
        List<FileInfo> files = fileService.findByProject(project.getId());
        Map<Caseless, HierarchyNode> resolvedNodes = new HashMap<Caseless, HierarchyNode>(
                files.size());
        for (FileInfo file : files) {
            Caseless name = file.getName();
            if (resolvedNodes.containsKey(name)) {
                continue;
            }
            HierarchyNode node = new HierarchyNode(name, file.getType(), null);
            resolvedNodes.put(name, node);
            resolveHierarchy(file, resolvedNodes);
        }
        Hierarchy hierarchy = new Hierarchy(project.getName(),
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
    private void resolveHierarchy(FileInfo file,
            Map<Caseless, HierarchyNode> resolvedNodes) {
        /*
         * This method resolves a hierarchy by recursively invoking itself.
         */
        Set<Caseless> dependencies;
        try {
            dependencies = dependencyExtractor.extract(file);
        } catch (DependencyExtractionException e) {
            dependencies = Collections.emptySet();
        }
        for (Caseless name : dependencies) {
            HierarchyNode parent = resolvedNodes.get(file.getName());
            if (resolvedNodes.containsKey(name)) {
                HierarchyNode depNode = resolvedNodes.get(name);
                parent.addDependency(depNode);
            } else {
                FileType type;
                FileInfo dep = fileService.findByName(file.getProjectId(), name);
                if (dep != null) {
                    type = dep.getType();
                } else {
                    // else its a predefined file
                    type = FileType.PREDEFINED;
                }
                HierarchyNode depNode = new HierarchyNode(name, type, parent);
                resolvedNodes.put(name, depNode);
                if (dep != null) {
                    // predefined files don't have dependencies
                    resolveHierarchy(dep, resolvedNodes);
                }
            }
        }
    }

}
