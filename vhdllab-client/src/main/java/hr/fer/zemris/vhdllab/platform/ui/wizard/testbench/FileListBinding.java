package hr.fer.zemris.vhdllab.platform.ui.wizard.testbench;

import hr.fer.zemris.vhdllab.platform.ui.wizard.testbench.FileListBinder.FileSelectionComponent;

import javax.swing.JComponent;
import javax.swing.JList;

import org.springframework.binding.form.FormModel;
import org.springframework.richclient.form.binding.swing.ListBinding;

public class FileListBinding extends ListBinding {

    private FileSelectionComponent fileComponent;

    @SuppressWarnings("unchecked")
    public FileListBinding(FileSelectionComponent fileComponent,
            FormModel formModel, String formFieldPath, Class requiredSourceClass) {
        super(fileComponent.getList(), formModel, formFieldPath,
                requiredSourceClass);
        this.fileComponent = fileComponent;
    }

    @Override
    public JComponent getComponent() {
        return fileComponent;
    }

    @Override
    public JList getList() {
        return fileComponent.getList();
    }

}
