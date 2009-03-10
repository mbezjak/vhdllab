package hr.fer.zemris.vhdllab.platform.ui.wizard.file;

import hr.fer.zemris.vhdllab.platform.ui.wizard.Focusable;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class FileForm extends AbstractForm implements Focusable {

    private JComboBox comboBox;
    private JComponent nameField;

    public FileForm(FormModel formModel) {
        super(formModel, "newFile");
    }

    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        comboBox = (JComboBox) builder.add("project")[1];
        builder.row();
        nameField = builder.add("fileName")[1];
        return builder.getForm();
    }

    @Override
    public void requestFocusInWindow() {
        nameField.requestFocusInWindow();
        /*
         * Hack for setting selected item in combo box. For some reason combo
         * box must be visible in order for editor (comboBox.getEditor()) to
         * display selected item.
         */
        comboBox.setSelectedIndex(comboBox.getItemCount() - 1);
    }

}
