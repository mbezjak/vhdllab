package hr.fer.zemris.vhdllab.view;

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainerListener;

import java.text.MessageFormat;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandManager;

public class TabbedEditorsView extends AbstractView implements
        EditorContainerListener {

    boolean addingTab;
    JTabbedPane tabbedPane;

    @Autowired
    EditorContainer container;

    @Override
    protected JComponent createControl() {
        CommandManager commandManager = getActiveWindow().getCommandManager();
        CommandGroup commandGroup = commandManager.createCommandGroup(
                "editorsMenu", new Object[] { "saveCommand", "saveAllCommand",
                        "separator", "closeCommand", "closeOtherCommand",
                        "closeAllCommand" });
        JPopupMenu popupMenu = commandGroup.createPopupMenu();
        tabbedPane = new JTabbedPane(JTabbedPane.TOP,
                JTabbedPane.WRAP_TAB_LAYOUT);
        tabbedPane.setComponentPopupMenu(popupMenu);
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if(!addingTab) {
                    int index = tabbedPane.getSelectedIndex();
                    container.setSelected(index);
                }
            }
        });
        return tabbedPane;
    }

    @Override
    public void editorAdded(Editor editor) {
        addingTab = true;
        String title = "editor title";
        String tooltip = "editor tooltip";
        Object[] args = new Object[] { editor.getFileName(),
                editor.getProjectName() };
        title = MessageFormat.format(title, args);
        tooltip = MessageFormat.format(tooltip, args);
        Icon editorIcon = getIconSource().getIcon("editor.icon");
        tabbedPane.addTab(title, editorIcon, editor.getPanel(), tooltip);
        addingTab = false;
    }

    @Override
    public void editorRemoved(Editor editor) {
        tabbedPane.remove(editor.getPanel());
    }

    @Override
    public void editorSelected(Editor editor) {
        if(editor != null) {
            tabbedPane.setSelectedComponent(editor.getPanel());
        }
    }

}
