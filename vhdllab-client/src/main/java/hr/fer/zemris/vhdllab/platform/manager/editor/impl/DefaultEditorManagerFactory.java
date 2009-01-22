package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.platform.manager.component.ComponentGroup;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;
import hr.fer.zemris.vhdllab.platform.manager.view.ViewIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractComponentManagerFactory;

import java.util.List;

import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultEditorManagerFactory extends
        AbstractComponentManagerFactory<EditorManager> implements
        EditorManagerFactory {

    @Autowired
    private WizardRegistry wizardRegistry;

    public DefaultEditorManagerFactory() {
        super(ComponentGroup.EDITOR);
    }

    @Override
    public EditorManager get(FileInfo file) {
        Validate.notNull(file, "File can't be null");
        EditorMetadata metadata = wizardRegistry.get(file.getType());
        EditorIdentifier identifier = new EditorIdentifier(metadata, file);
        return get(identifier);
    }

    @Override
    protected EditorManager newInstance(ViewIdentifier identifier) {
        return new MultiInstanceEditorManager((EditorIdentifier) identifier);
    }

    @Override
    protected EditorManager newMulticastInstance(List<EditorManager> managers) {
        return new MulticastEditorManager(managers);
    }

    @Override
    protected EditorManager newNoSelectionInstance() {
        return new NoSelectionEditorManager();
    }

    @Override
    protected boolean requiredIdentifierType(ViewIdentifier identifier) {
        return identifier.getClass() == EditorIdentifier.class;
    }

}
