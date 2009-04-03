package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.collections.Predicate;

public class NotEmptyProjectFilter implements Predicate {

    private WorkspaceManager manager;
    private final Predicate fileFilter;

    public NotEmptyProjectFilter(WorkspaceManager manager, Predicate fileFilter) {
        this.manager = manager;
        this.fileFilter = fileFilter;
    }

    @Override
    public boolean evaluate(Object object) {
        return !manager.isEmpty((Project) object, fileFilter);
    }

}
