package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.platform.util.FormModelUtils;

import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

import javax.swing.JComponent;

import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.builder.TableFormBuilder;

public abstract class AbstractMultiValidationForm extends AbstractForm {

    protected JComponent componentToGiveFocusTo;

    public AbstractMultiValidationForm(Object formObject, String formId) {
        super(FormModelUtils.createFormModel(formObject), formId);
    }

    @Override
    protected JComponent createFormControl() {
        TableFormBuilder builder = new TableFormBuilder(getBindingFactory());
        doBuildForm(builder);

        JComponent control = builder.getForm();
        control.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (componentToGiveFocusTo != null) {
                    componentToGiveFocusTo.requestFocusInWindow();
                }
            }
        });
        return control;
    }

    protected void focusOnBeginning(JComponent c) {
        componentToGiveFocusTo = c;
    }

    protected abstract void doBuildForm(TableFormBuilder builder);

}
