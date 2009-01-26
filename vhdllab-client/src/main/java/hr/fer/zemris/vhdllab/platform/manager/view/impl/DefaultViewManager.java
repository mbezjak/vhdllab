package hr.fer.zemris.vhdllab.platform.manager.view.impl;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.file.FileManager;
import hr.fer.zemris.vhdllab.platform.manager.project.ProjectManager;
import hr.fer.zemris.vhdllab.platform.manager.view.View;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.swing.JPanel;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultViewManager extends LocalizationSupport implements
        ViewManager {

    private static final String TITLE_PREFIX = "title.for.";
    private static final String TOOLTIP_PREFIX = "title.for.";

    @Resource(name = "groupBasedComponentContainer")
    private ComponentContainer container;
    @Autowired
    private ISystemContainer systemContainer;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private FileManager fileManager;
    private final Map<Class<? extends View>, JPanel> openedViews;
    private IProjectExplorer projectExplorer;

    public DefaultViewManager() {
        openedViews = new HashMap<Class<? extends View>, JPanel>();
    }

    @Override
    public void open(Class<? extends View> viewClass) {
        Validate.notNull(viewClass, "View class can't be null");
        View instance;
        try {
            instance = viewClass.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        instance.setSystemContainer(systemContainer);
        instance.setEditorManagerFactory(editorManagerFactory);
        instance.setMapper(mapper);
        instance.setProjectManager(projectManager);
        instance.setFileManager(fileManager);
        instance.init();
        String title = getTitle(viewClass);
        String tooltip = getTooltip(viewClass);
        JPanel panel = instance.getPanel();
        container.add(title, tooltip, panel, getGroup(viewClass));
        openedViews.put(viewClass, panel);
        if (instance instanceof IProjectExplorer) {
            this.projectExplorer = (IProjectExplorer) instance;
        }
    }

    @Override
    public void select(Class<? extends View> viewClass) {
        Validate.notNull(viewClass, "View class can't be null");
        JPanel panel = openedViews.get(viewClass);
        if (panel == null) {
            throw new IllegalStateException("View isn't open");
        }
        container.setSelected(panel, getGroup(viewClass));
    }

    @Override
    public IProjectExplorer getProjectExplorer() {
        return projectExplorer;
    }

    private ComponentGroup getGroup(Class<? extends View> viewClass) {
        return IProjectExplorer.class.isAssignableFrom(viewClass) ? ComponentGroup.PROJECT_EXPLORER
                : ComponentGroup.VIEW;
    }

    private String getTitle(Class<?> clazz) {
        return getMessageWithPrefix(TITLE_PREFIX, clazz);
    }

    private String getTooltip(Class<?> clazz) {
        return getMessageWithPrefix(TOOLTIP_PREFIX, clazz);
    }

    private String getMessageWithPrefix(String prefix, Class<?> clazz) {
        String code = prefix + StringUtils.uncapitalize(clazz.getSimpleName());
        return getMessage(code, null);
    }

}
