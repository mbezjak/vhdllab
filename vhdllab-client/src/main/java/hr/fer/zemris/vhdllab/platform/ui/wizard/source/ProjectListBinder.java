package hr.fer.zemris.vhdllab.platform.ui.wizard.source;

import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JComponent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.richclient.form.binding.swing.ComboBoxBinding;
import org.springframework.stereotype.Component;

@Component
public class ProjectListBinder extends AbstractBinder {

    @Autowired
    private WorkspaceManager workspaceManager;

    protected ProjectListBinder() {
        super(Project.class, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl(Map context) {
        return getComponentFactory().createComboBox();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel,
            String formPropertyPath, Map context) {
        ComboBoxBinding binding = new ComboBoxBinding((JComboBox) control,
                formModel, formPropertyPath);
        List<Project> projects = workspaceManager.getProjects();
        List<Project> improvedToStringProjects = new ArrayList<Project>(
                projects.size());
        for (Project p : projects) {
            improvedToStringProjects.add(new Project(p) {
                private static final long serialVersionUID = 1L;

                @Override
                public String toString() {
                    return getName();
                }
            });
        }
        binding.setSelectableItems(improvedToStringProjects);
        return binding;
    }

}
