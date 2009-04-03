package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.CompilableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NameInToStringFileTransformer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NameInToStringProjectTransformer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.richclient.list.ComboBoxAutoCompletion;
import org.springframework.stereotype.Component;

@Component
public class FileListBinder extends AbstractBinder {

    @Autowired
    protected WorkspaceManager workspaceManager;

    protected FileListBinder() {
        super(File.class, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl(Map context) {
        return new FileSelectionComponent();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel,
            String formPropertyPath, Map context) {
        FileSelectionComponent fileComponent = (FileSelectionComponent) control;
        return new FileListBinding(fileComponent, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

    protected class FileSelectionComponent extends JPanel {

        private static final long serialVersionUID = 1L;

        private JComboBox projectsCombobox;
        private final JList list;
        protected DefaultListModel model;

        public FileSelectionComponent() {
            projectsCombobox = new JComboBox(projectList());
            new ComboBoxAutoCompletion(projectsCombobox);
            model = new DefaultListModel();
            list = new JList(model);
            list.setPreferredSize(new Dimension(150, 150));
            projectsCombobox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    if (e.getStateChange() == ItemEvent.SELECTED) {
                        updateList(e.getItem());
                    }
                }
            });
            Object selectedItem = projectsCombobox.getSelectedItem();
            if (selectedItem != null) {
                updateList(selectedItem);
            }

            setLayout(new BorderLayout());
            add(projectsCombobox, BorderLayout.NORTH);
            add(new JScrollPane(list), BorderLayout.CENTER);
        }

        public JComboBox getProjectsCombobox() {
            return projectsCombobox;
        }

        public JList getList() {
            return list;
        }

        protected void updateList(Object selectedProject) {
            model.clear();
            Predicate filter = CompilableFilesFilter.getInstance();
            Transformer transformer = NameInToStringFileTransformer
                    .getInstance();
            Set<File> files = workspaceManager.getFilesForProject(
                    (Project) selectedProject, filter, transformer);
            for (File f : files) {
                model.addElement(f);
            }
            list.clearSelection();
        }

        private Object[] projectList() {
            NotEmptyProjectFilter filter = new NotEmptyProjectFilter(
                    workspaceManager, CompilableFilesFilter.getInstance());
            Transformer transformer = NameInToStringProjectTransformer
                    .getInstance();
            return workspaceManager.getProjects(filter, transformer).toArray();
        }

    }

}
