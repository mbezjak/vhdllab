package hr.fer.zemris.vhdllab.platform.gui.dialog;

import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class AbstractDialog<T> extends JDialog {

    private static final long serialVersionUID = 1L;

    private LocalizationSource source;

    private T result = null;

    public AbstractDialog(LocalizationSource source) {
        super(source.getFrame(), true);
        this.source = source;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public T getResult() {
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    public void startDialog() {
        pack();
        setLocationRelativeTo(source.getFrame());
        setVisible(true);
    }

    public void closeDialog(T resultOfExecution) {
        setResult(resultOfExecution);
        setVisible(false);
    }

}
