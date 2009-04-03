package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.testbench.FileListBinder.FileSelectionComponent;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComboBox;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class TestbenchFileForm extends AbstractMultiValidationForm {

    private FileSelectionComponent fileComponent;

    public TestbenchFileForm() {
        super(new TestbenchFile(), "newTestbenchFile");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        fileComponent = (FileSelectionComponent) builder.add("targetFile")[1];
        focusOnBeginning(fileComponent.getList());
        builder.row();
        builder.add("testbenchName");

        installSuggestionTestbenchName();
    }

    @Override
    public void onAboutToShow() {
        JComboBox combobox = fileComponent.getProjectsCombobox();
        combobox.setSelectedIndex(combobox.getItemCount() - 1);
    }

    private void installSuggestionTestbenchName() {
        getFormModel().getValueModel("targetFile").addValueChangeListener(
                new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        File selectedFile = (File) evt.getNewValue();
                        String testbenchName = null;
                        if (selectedFile != null) {
                            testbenchName = selectedFile.getName() + "_tb";
                        }
                        getFormModel().getValueModel("testbenchName").setValue(
                                testbenchName);
                    }
                });
    }

}
