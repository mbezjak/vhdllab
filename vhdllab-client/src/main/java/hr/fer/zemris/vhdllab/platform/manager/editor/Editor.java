package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.view.View;

public interface Editor extends View {

    void dispose();

    void undo();

    void redo();

    void setFile(FileInfo file);

    FileInfo getFile();

    boolean setModified(boolean flag);

    boolean isModified();

    boolean isSavable();

    void highlightLine(int line);

    EventPublisher<EditorListener> getEventPublisher();

    IWizard getWizard();

}
