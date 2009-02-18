package hr.fer.zemris.vhdllab.view;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorContainerListener;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorListener;
import hr.fer.zemris.vhdllab.platform.ui.command.RedoCommand;
import hr.fer.zemris.vhdllab.platform.ui.command.UndoCommand;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.PageComponentContext;
import org.springframework.richclient.application.support.AbstractView;
import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandManager;
import org.springframework.richclient.command.support.GlobalCommandIds;

public class TabbedEditorsView extends AbstractView implements
        EditorContainerListener {

    boolean addingTab;
    JTabbedPane tabbedPane;

    @Autowired
    EditorContainer container;

    private Map<FileInfo, Editor> editors = new HashMap<FileInfo, Editor>();

    @Override
    protected void registerLocalCommandExecutors(PageComponentContext context) {
        context.register(GlobalCommandIds.UNDO, new UndoCommand());
        context.register(GlobalCommandIds.REDO, new RedoCommand());
    }

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
                if (!addingTab) {
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
        editors.put(editor.getFile(), editor);
        String title = editor.getTitle();
        String tooltip = editor.getCaption();
        Object[] args = new Object[] { editor.getFileName(),
                editor.getProjectName() };
        title = MessageFormat.format(title, args);
        tooltip = MessageFormat.format(tooltip, args);
        Icon editorIcon = getIconSource().getIcon("editor.icon");
        tabbedPane.addTab(title, editorIcon, editor.getPanel(), tooltip);
        editor.getEventPublisher().addListener(new EditorModifiedListener());
        addingTab = false;
    }

    @Override
    public void editorRemoved(Editor editor) {
        tabbedPane.remove(editor.getPanel());
        editors.remove(editor.getFile());
    }

    @Override
    public void editorSelected(Editor editor) {
        if (editor != null) {
            tabbedPane.setSelectedComponent(editor.getPanel());
        }
    }

    void resetEditorTitle(FileInfo file, boolean modified) {
        int index = container.indexOf(editors.get(file));
        String title = tabbedPane.getTitleAt(index);
        if (modified) {
            title = "*" + title;
        } else {
            title = title.substring(1); // omit leading * character
        }
        tabbedPane.setTitleAt(index, title);
    }

    class EditorModifiedListener implements EditorListener {
        @Override
        public void modified(FileInfo file, boolean flag) {
            resetEditorTitle(file, flag);
        }
    }

}
