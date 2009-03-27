package hr.fer.zemris.vhdllab.entity.stub;

import hr.fer.zemris.vhdllab.entity.PreferencesFile;

import org.apache.commons.beanutils.BeanUtils;

public class PreferencesFileStub extends PreferencesFile {

    private static final long serialVersionUID = 1L;

    public static final String DATA = "preferences data";

    public PreferencesFileStub() throws Exception {
        BeanUtils.copyProperties(this, new OwnedEntityStub());
        setData(DATA);
    }

}
