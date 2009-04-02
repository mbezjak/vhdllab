package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import org.apache.commons.collections.Predicate;

public class NotEmptyProjectFilter implements Predicate {

    private WorkspaceManager manager;
    private final Predicate filter;

    public NotEmptyProjectFilter(WorkspaceManager manager, Predicate filter) {
        this.manager = manager;
        this.filter = filter;
    }

    @Override
    public boolean evaluate(Object object) {
        return !manager.isEmpty((Project) object, filter);
    }

}
