package hr.fer.zemris.vhdllab.platform.ui;

import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.support.WorkspaceInitializer;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.command.ActionCommand;

public abstract class GuardedActionCommand extends ActionCommand {

    @Autowired
    protected WorkspaceManager workspaceManager;
    @Autowired
    protected SimulationManager simulationManager;
    @Autowired
    protected WorkspaceInitializer workspaceInitializer;

    public GuardedActionCommand() {
        setId(BeanUtil.beanName(getClass()));
    }

    @Override
    public void afterPropertiesSet() {
        super.afterPropertiesSet();
        installCommandGuard(workspaceManager, workspaceInitializer,
                simulationManager);
    }

    /**
     * Subclasses may override this method to install a command guard.
     * 
     * @param wm
     *            a workspace manager
     * @param wi
     *            a workspace initializer
     * @param sm
     *            a simulation manager
     */
    protected void installCommandGuard(WorkspaceManager wm,
            WorkspaceInitializer wi, SimulationManager sm) {
        // default no implementation, subclasses may override
    }

}
