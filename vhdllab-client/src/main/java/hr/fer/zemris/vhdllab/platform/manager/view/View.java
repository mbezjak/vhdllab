package hr.fer.zemris.vhdllab.platform.manager.view;

import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;

import javax.swing.JPanel;

public interface View {

    ISystemContainer getSystemContainer();

    void setSystemContainer(ISystemContainer systemContainer);

    EditorManagerFactory getEditorManagerFactory();

    void setEditorManagerFactory(EditorManagerFactory editorManagerFactory);

    IdentifierToInfoObjectMapper getMapper();

    void setMapper(IdentifierToInfoObjectMapper mapper);

    void init();

    JPanel getPanel();

}
