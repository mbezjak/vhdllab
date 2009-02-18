package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class ProjectForm extends AbstractForm {

    private JComponent nameField;

    public ProjectForm(FormModel formModel) {
        super(formModel, "newProject");
    }

    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        nameField = builder.add("projectName")[1];
        return builder.getForm();
    }

    public void requestFocusInWindow() {
        nameField.requestFocusInWindow();
    }

}
