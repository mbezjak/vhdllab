package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

public abstract class AbstractShowWizardDialogCommand extends ActionCommand
        implements ApplicationContextAware {

    private ApplicationContext context;

    public AbstractShowWizardDialogCommand() {
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
        Wizard wizard = getWizard();
        new WizardDialog(wizard).showDialog();
    }

    protected abstract Class<? extends Wizard> getWizardClass();

    private Wizard getWizard() {
        String wizardBeanName = BeanUtil.beanName(getWizardClass());
        return (Wizard) context.getBean(wizardBeanName);
    }

}
