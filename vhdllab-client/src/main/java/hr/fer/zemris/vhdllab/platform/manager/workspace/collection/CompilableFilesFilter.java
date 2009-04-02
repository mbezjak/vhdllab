package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.collections.Predicate;

public class CompilableFilesFilter implements Predicate {

    private static final Predicate INSTANCE = new CompilableFilesFilter();

    private CompilableFilesFilter() {
    }

    public static Predicate getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean evaluate(Object object) {
        File file = (File) object;
        return file.getType().isCompilable();
    }

}
