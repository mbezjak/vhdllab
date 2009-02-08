package hr.fer.zemris.vhdllab.view;

import hr.fer.zemris.vhdllab.platform.gui.menu.MenuGenerator;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainerListener;

import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.support.AbstractView;

public class TabbedEditorsView extends AbstractView implements
        EditorContainerListener {

    private final Icon editorIcon = getIconSource().getIcon("editor.icon");
    private JTabbedPane tabbedPane;
    
    @Autowired
    private MenuGenerator generator;

    @Override
    protected JComponent createControl() {
        tabbedPane = new JTabbedPane(JTabbedPane.TOP,
                JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setComponentPopupMenu(generator.generateEditorPopupMenu());
        return tabbedPane;
    }

    @Override
    public void editorAdded(Editor editor) {
        String title = "editor title";
        String tooltip = "editor tooltip";
        Object[] args = new Object[] { editor.getFileName(),
                editor.getProjectName() };
        title = MessageFormat.format(title, args);
        tooltip = MessageFormat.format(tooltip, args);
        tabbedPane.addTab(title, editorIcon, editor.getPanel(), tooltip);
    }

    @Override
    public void editorRemoved(Editor editor) {
        tabbedPane.remove(editor.getPanel());
    }

    @Override
    public void editorSelected(Editor editor) {
        tabbedPane.setSelectedComponent(editor.getPanel());
    }

}
