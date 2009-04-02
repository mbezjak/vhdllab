package hr.fer.zemris.vhdllab.platform.ui.wizard.project;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractMultiValidationForm;

import org.springframework.richclient.form.builder.TableFormBuilder;

public class ProjectForm extends AbstractMultiValidationForm {

    public ProjectForm() {
        super(new Project(), "newProject");
    }

    @Override
    protected void doBuildForm(TableFormBuilder builder) {
        focusOnBeginning(builder.add("name")[1]);
    }

}
