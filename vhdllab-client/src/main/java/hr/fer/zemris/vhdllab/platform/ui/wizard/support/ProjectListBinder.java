package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NameInToStringProjectTransformer;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.form.binding.swing.AbstractListBinding;
import org.springframework.richclient.form.binding.swing.ComboBoxBinder;
import org.springframework.stereotype.Component;

@Component
public class ProjectListBinder extends ComboBoxBinder {

    @Autowired
    private WorkspaceManager workspaceManager;

    protected ProjectListBinder() {
        super(Project.class, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void applyContext(AbstractListBinding binding, Map context) {
        List<Project> projects = workspaceManager.getProjects(null,
                NameInToStringProjectTransformer.getInstance());
        setSelectableItems(projects);
        super.applyContext(binding, context);
    }

}
