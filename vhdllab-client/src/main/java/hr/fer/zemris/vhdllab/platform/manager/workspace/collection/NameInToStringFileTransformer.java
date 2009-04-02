package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.collections.Transformer;

public class NameInToStringFileTransformer implements Transformer {

    private static final Transformer INSTANCE = new NameInToStringFileTransformer();

    private NameInToStringFileTransformer() {
    }

    public static Transformer getInstance() {
        return INSTANCE;
    }

    @Override
    public Object transform(Object input) {
        return new File((File) input, true) {
            private static final long serialVersionUID = 1L;

            @Override
            public String toString() {
                return getName();
            }
        };
    }

}
