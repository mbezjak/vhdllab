package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.Project;

import org.apache.commons.collections.Transformer;

public class NameInToStringProjectTransformer implements Transformer {

    private static final Transformer INSTANCE = new NameInToStringProjectTransformer();

    private NameInToStringProjectTransformer() {
    }

    public static Transformer getInstance() {
        return INSTANCE;
    }

    @Override
    public Object transform(Object input) {
        return new Project((Project) input) {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return getName();
            }
        };
    }

}
