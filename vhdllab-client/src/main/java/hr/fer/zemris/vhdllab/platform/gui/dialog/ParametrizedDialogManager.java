package hr.fer.zemris.vhdllab.platform.gui.dialog;

public interface ParametrizedDialogManager<PARAMETER, RETURN> extends
        DialogManager<RETURN> {

    RETURN showDialog(PARAMETER parameter);

}
