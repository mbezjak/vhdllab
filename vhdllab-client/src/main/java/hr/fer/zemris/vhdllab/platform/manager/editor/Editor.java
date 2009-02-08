package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

import javax.swing.JPanel;

public interface Editor {

    PlatformContainer getContainer();

    void setContainer(PlatformContainer container);

    void init();

    JPanel getPanel();

    void dispose();

    void undo();

    void redo();

    void setFile(FileInfo file);

    FileInfo getFile();

    boolean setModified(boolean flag);

    boolean isModified();

    void setEditable(boolean flag);

    void highlightLine(int line);

    EventPublisher<EditorListener> getEventPublisher();

    Caseless getFileName();

    Caseless getProjectName();

}
