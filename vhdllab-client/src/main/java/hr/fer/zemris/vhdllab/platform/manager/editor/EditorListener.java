package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entity.File;

import java.util.EventListener;

public interface EditorListener extends EventListener {

    /**
     * Indicates that editor is either modified or not (i.e. editor has been
     * saved).
     * 
     * @param file
     *            a file that editor represents
     * @param flag
     *            <code>true</code> if editor has been modified or
     *            <code>false</code> otherwise (i.e. an editor has just been
     *            saved)
     */
    void modified(File file, boolean flag);

}
