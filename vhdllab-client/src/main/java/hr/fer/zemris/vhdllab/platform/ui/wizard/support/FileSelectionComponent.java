package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NameInToStringFileTransformer;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NameInToStringProjectTransformer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.apache.commons.collections.Predicate;
import org.apache.commons.collections.Transformer;
import org.springframework.richclient.list.ComboBoxAutoCompletion;

public class FileSelectionComponent extends JPanel {

    private static final long serialVersionUID = 1L;

    private final WorkspaceManager manager;
    private final Predicate fileFilter;

    private JComboBox projectsCombobox;
    private JList list;
    protected DefaultListModel model;

    public FileSelectionComponent(WorkspaceManager manager,
            Predicate projectFilter, Predicate fileFilter) {
        this.manager = manager;
        this.fileFilter = fileFilter;
        createComponent(projectFilter);
    }

    private void createComponent(Predicate projectFilter) {
        projectsCombobox = new JComboBox(projectList(projectFilter));
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

        setLayout(new BorderLayout());
        add(projectsCombobox, BorderLayout.NORTH);
        add(new JScrollPane(list), BorderLayout.CENTER);
    }

    public JList getList() {
        return list;
    }

    public void selectLastProject() {
        int selectProjectIndex = projectsCombobox.getItemCount() - 1;
        if (selectProjectIndex == projectsCombobox.getSelectedIndex()) {
            updateList(projectsCombobox.getSelectedItem());
        } else {
            projectsCombobox.setSelectedIndex(selectProjectIndex);
        }
    }

    public File getSelectedFile() {
        return (File) list.getSelectedValue();
    }

    protected void updateList(Object selectedProject) {
        model.clear();
        Transformer transformer = NameInToStringFileTransformer.getInstance();
        Set<File> files = manager.getFilesForProject((Project) selectedProject,
                fileFilter, transformer);
        for (File f : files) {
            model.addElement(f);
        }
        list.clearSelection();
        if (files.size() == 1) {
            list.setSelectedIndex(0);
        }
    }

    private Object[] projectList(Predicate projectFilter) {
        Transformer transformer = NameInToStringProjectTransformer
                .getInstance();
        return manager.getProjects(projectFilter, transformer).toArray();
    }

}
