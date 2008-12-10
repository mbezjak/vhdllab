package hr.fer.zemris.vhdllab.platform.remoting.exception.handler;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;
import hr.fer.zemris.vhdllab.platform.remoting.exception.AbstractExceptionHandler;

import java.net.ConnectException;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class ConnectExceptionHandler extends
        AbstractExceptionHandler {

    @Resource(name = "cantConnectDialogManager")
    private DialogManager<?> dialogManager;

    @Override
    public boolean handleException(Exception e) {
        if (isOfType(e, ConnectException.class)) {
            dialogManager.showDialog();
            return exitIfNecessary();
        }
        return false;
    }

}
