package hr.fer.zemris.vhdllab.platform.manager.editor;

import java.util.List;

public interface EditorContainer {

    void add(Editor editor);

    void remove(Editor editor);

    Editor getSelected();

    void setSelected(Editor editor);

    void setSelected(int index);

    boolean isSelected(Editor editor);

    List<Editor> getAll();

    List<Editor> getAllButSelected();

}
