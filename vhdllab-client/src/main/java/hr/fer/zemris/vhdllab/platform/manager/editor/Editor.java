package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

import org.springframework.richclient.factory.ControlFactory;

public interface Editor extends ControlFactory {

    PlatformContainer getContainer();

    void setContainer(PlatformContainer container);

    void dispose();

    void undo();

    void redo();

    void setFile(File file);

    File getFile();

    boolean setModified(boolean flag);

    boolean isModified();

    void setEditable(boolean flag);

    void highlightLine(int line);

    EventPublisher<EditorListener> getEventPublisher();

    String getFileName();

    String getProjectName();

    void setMetadata(EditorMetadata metadata);

    EditorMetadata getMetadata();

    String getTitle();

    String getCaption();

}
