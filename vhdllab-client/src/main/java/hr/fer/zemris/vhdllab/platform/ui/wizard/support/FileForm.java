package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JComponent;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class FileForm extends AbstractMultiValidationForm {

    public FileForm() {
        super(new File(), "newFile");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        focusOnBeginning(builder.add("project")[1]);
        builder.row();
        builder.add("name");
    }
    
    @Override
    protected JComponent createFormControl() {
        JComponent control = super.createFormControl();
        control.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentShown(ComponentEvent e) {
                System.out.println("\n\n\n\n\n\n");
                System.out.println(e);
                System.out.println("\n\n\n\n\n\n");
            }
        });
        return control;
    }

}
