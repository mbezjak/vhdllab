package hr.fer.zemris.vhdllab.service;

import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.entities.ProjectInfo;

public interface HierarchyExtractor {

    Hierarchy extract(ProjectInfo project);

}
