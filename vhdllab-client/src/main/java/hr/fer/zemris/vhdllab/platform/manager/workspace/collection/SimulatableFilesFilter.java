package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.collections.Predicate;

public class SimulatableFilesFilter implements Predicate {

    private static final Predicate INSTANCE = new SimulatableFilesFilter();

    private SimulatableFilesFilter() {
    }

    public static Predicate getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean evaluate(Object object) {
        File file = (File) object;
        return file.getType().isSimulatable();
    }

}
