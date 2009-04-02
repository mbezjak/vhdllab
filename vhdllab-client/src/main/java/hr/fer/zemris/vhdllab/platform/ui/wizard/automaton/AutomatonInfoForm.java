package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.swing.SwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;

public class AutomatonInfoForm extends AbstractMultiValidationForm {

    public AutomatonInfoForm() {
        super(new AutomatonInfo(), "newAutomatonInfo");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        focusOnBeginning(builder
                .add(combobox("automatonType", "Moore", "Mealy"))[1]);
        builder.row();
        builder.add(combobox("resetValue", "0", "1"));
        builder.row();
        builder.add(combobox("clockValue", "falling_edge", "rising_edge", "0",
                "1"));
    }

    private Binding combobox(String formProperty, String... selectableItems) {
        SwingBindingFactory factory = (SwingBindingFactory) getBindingFactory();
        return factory.createBoundComboBox(formProperty, selectableItems);
    }

}
