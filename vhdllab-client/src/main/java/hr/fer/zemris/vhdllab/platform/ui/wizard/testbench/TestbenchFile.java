package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileTarget;
import hr.fer.zemris.vhdllab.validation.NameFormatConstraint;

import org.hibernate.validator.Length;
import org.hibernate.validator.NotEmpty;

public class TestbenchFile extends FileTarget {

    @NameFormatConstraint
    @NotEmpty
    @Length(max = 255)
    private String testbenchName;

    public String getTestbenchName() {
        return testbenchName;
    }

    public void setTestbenchName(String testbenchName) {
        this.testbenchName = testbenchName;
    }

}
