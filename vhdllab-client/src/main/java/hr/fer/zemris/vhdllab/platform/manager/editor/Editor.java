package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.component.IComponent;

public interface Editor extends IComponent {

    boolean isModified();

    void undo();

    void redo();

    void setFile(FileInfo file);

    FileInfo getFile();

    void highlightLine(int line);

    IWizard getWizard();

}
