package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entity.Project;

public interface HierarchyExtractor {

    Hierarchy extract(Project project);

}
