package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.testbench.FileListBinder.FileSelectionComponent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class TestbenchFileForm extends AbstractMultiValidationForm {

    public TestbenchFileForm() {
        super(new TestbenchFile(), "newTestbenchFile");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        FileSelectionComponent fileComponent = (FileSelectionComponent) builder
                .add("targetFile")[1];
        focusOnBeginning(fileComponent.getList());
        builder.row();
        builder.add("testbenchName");

        installSuggestionTestbenchName();
    }

    private void installSuggestionTestbenchName() {
        getFormModel().getValueModel("targetFile").addValueChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        File selectedFile = (File) evt.getNewValue();
                        String testbenchName = selectedFile.getName() + "_tb";
                        getFormModel().getValueModel("testbenchName").setValue(
                                testbenchName);
                    }
                });
    }

}
