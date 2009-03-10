package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.platform.util.BeanUtil;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

public abstract class AbstractResourceCreatingCommand extends ActionCommand
        implements ApplicationContextAware {

    private ApplicationContext context;

    public AbstractResourceCreatingCommand() {
        setDisplaysInputDialog(true);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        Validate.notNull(applicationContext,
                "Application context can't be null");
        this.context = applicationContext;
    }

    @Override
    protected void doExecuteCommand() {
        Form form = createForm();
        Wizard wizard = getWizard(form);
        createWizardDialog(wizard, form).showDialog();
    }

    protected abstract Form createForm();

    protected abstract Class<? extends Wizard> getWizardClass();

    private Wizard getWizard(Form form) {
        String wizardBeanName = BeanUtil.getBeanName(getWizardClass());
        AbstractResourceCreatingWizard wizard = (AbstractResourceCreatingWizard) context
                .getBean(wizardBeanName);
        wizard.setForm(form);
        return wizard;
    }

    private WizardDialog createWizardDialog(Wizard wizard, final Form form) {
        return new WizardDialog(wizard) {
            @Override
            protected void onAboutToShow() {
                super.onAboutToShow();
                if (form instanceof Focusable) {
                    ((Focusable) form).requestFocusInWindow();
                }
            }
        };
    }

}
