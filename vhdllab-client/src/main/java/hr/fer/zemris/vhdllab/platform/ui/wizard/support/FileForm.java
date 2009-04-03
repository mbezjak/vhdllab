package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import javax.swing.JComboBox;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class FileForm extends AbstractMultiValidationForm {

    private JComboBox projectCombobox;

    public FileForm() {
        super(new File(), "newFile");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        projectCombobox = (JComboBox) builder.add("project")[1];
        builder.row();
        focusOnBeginning(builder.add("name")[1]);
    }

    @Override
    public void onAboutToShow() {
        projectCombobox.setSelectedIndex(projectCombobox.getItemCount() - 1);
    }

}
