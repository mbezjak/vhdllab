package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceListener;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializationListener;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.service.workspace.FileReport;
import hr.fer.zemris.vhdllab.service.workspace.Workspace;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.richclient.command.ActionCommand;
import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

public abstract class AbstractShowWizardDialogCommand extends ActionCommand
        implements ApplicationContextAware, WorkspaceInitializationListener,
        WorkspaceListener {

    private ApplicationContext context;

    @Autowired
    protected WorkspaceManager manager;
    @Autowired
    private WorkspaceInitializer workspaceInitializer;

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

    @PostConstruct
    public void registerWorkspaceListeners() {
        workspaceInitializer.addListener(this);
        manager.addListener(this);
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

    @Override
    public void initialize(Workspace workspace) {
        updateEnabledCommandState();
    }

    @Override
    public void fileCreated(FileReport report) {
        updateEnabledCommandState();
    }

    @Override
    public void fileSaved(FileReport report) {
        updateEnabledCommandState();
    }

    @Override
    public void fileDeleted(FileReport report) {
        updateEnabledCommandState();
    }

    @Override
    public void projectCreated(Project project) {
        updateEnabledCommandState();
    }

    @Override
    public void projectDeleted(Project project) {
        updateEnabledCommandState();
    }

    protected void updateEnabledCommandState() {
        setEnabled(manager.getWorkspace().getProjectCount() != 0);
    }

}
