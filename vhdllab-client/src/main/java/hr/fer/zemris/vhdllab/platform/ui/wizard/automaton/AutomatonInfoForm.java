package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import javax.swing.JComponent;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class AutomatonInfoForm extends AbstractForm {

    public AutomatonInfoForm(FormModel formModel) {
        super(formModel, "newAutomatonInfo");
    }

    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        SwingBindingFactory factory = (SwingBindingFactory) getBindingFactory();
        builder.add(factory.createBoundComboBox("automatonType", new String[] {
                "Moore", "Mealy" }));
        builder.row();
        builder.add(factory.createBoundComboBox("resetValue", new String[] {
                "0", "1" }));
        builder.row();
        builder.add(factory.createBoundComboBox("clockValue", new String[] {
                "falling_edge", "rising_edge", "0", "1" }));
        return builder.getForm();
    }

}
