package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import java.util.ArrayList;
import java.util.List;

import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.AbstractWizard;
import org.springframework.richclient.wizard.WizardPage;

public abstract class AbstractFormSupportingWizard extends AbstractWizard {

    private String id;

    private List<Form> formPages = new ArrayList<Form>();

    public AbstractFormSupportingWizard() {
        this.id = BeanUtil.beanName(getClass());
    }

    @Override
    public WizardPage addForm(Form formPage) {
        formPages.add(formPage);
        return super.addForm(formPage);
    }

    @Override
    public String getId() {
        return this.id;
    }

    @Override
    protected boolean onFinish() {
        for (Form form : formPages) {
            form.commit();
        }
        onWizardFinished();
        return true;
    }

    protected abstract void onWizardFinished();

    // ************************************************************************
    // Supporting methods:
    // ************************************************************************

    protected File getFile(FileForm fileForm) {
        return (File) fileForm.getFormObject();
    }

    protected CircuitInterface getCircuitInterface(FileForm fileForm,
            PortWizardPage portPage) {
        File file = getFile(fileForm);
        CircuitInterface ci = new CircuitInterface(file.getName());
        ci.addAll(portPage.getPorts());
        return ci;
    }

    protected File createFile(Project project, String name, FileType type,
            String data) {
        File file = new File(name, type, data);
        file.setProject(new Project(project));
        return file;
    }

}
