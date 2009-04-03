package hr.fer.zemris.vhdllab.platform.ui.wizard.support;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.CompilableFilesFilter;
import hr.fer.zemris.vhdllab.platform.manager.workspace.collection.NotEmptyProjectFilter;

import java.util.Map;

import javax.swing.JComponent;

import org.apache.commons.collections.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.Binding;
import org.springframework.richclient.form.binding.support.AbstractBinder;
import org.springframework.stereotype.Component;

@Component
public class FileListBinder extends AbstractBinder {

    @Autowired
    private WorkspaceManager workspaceManager;

    protected FileListBinder() {
        super(File.class, new String[0]);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected JComponent createControl(Map context) {
        Predicate fileFilter = CompilableFilesFilter.getInstance();
        Predicate projectFilter = new NotEmptyProjectFilter(workspaceManager,
                fileFilter);
        return new FileSelectionComponent(workspaceManager, projectFilter,
                fileFilter);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Binding doBind(JComponent control, FormModel formModel,
            String formPropertyPath, Map context) {
        FileSelectionComponent fileComponent = (FileSelectionComponent) control;
        return new FileListBinding(fileComponent, formModel, formPropertyPath,
                getRequiredSourceClass());
    }

}
