package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.applets.editor.newtb.TestbenchEditorMetadata;
import hr.fer.zemris.vhdllab.applets.texteditor.TextEditorMetadata;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.springframework.stereotype.Component;

@Component
public final class WizardRegistry {

    private final Map<FileType, EditorMetadata> wizards;

    public WizardRegistry() {
        wizards = new HashMap<FileType, EditorMetadata>();
        add(FileType.SOURCE, new TextEditorMetadata());
        add(FileType.TESTBENCH, new TestbenchEditorMetadata());
    }

    public void add(FileType type, EditorMetadata metadata) {
        Validate.notNull(type, "File type can't be null");
        Validate.notNull(metadata, "Editor metadata can't be null");
        wizards.put(type, metadata);
    }

    public void remove(FileType type) {
        Validate.notNull(type, "File type can't be null");
        wizards.remove(type);
    }

    public EditorMetadata get(FileType type) {
        Validate.notNull(type, "File type can't be null");
        return wizards.get(type);
    }

}
