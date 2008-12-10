package hr.fer.zemris.vhdllab.platform.remoting.exception.handler;

import hr.fer.zemris.vhdllab.platform.remoting.exception.AbstractExceptionHandler;

import org.springframework.stereotype.Component;

@Component
public class SecurityExceptionHandler extends AbstractExceptionHandler {

    @Override
    public boolean handleException(Exception e) {
        if (isOfType(e, SecurityException.class)) {
            /*
             * This means that user refused to provide proper username and
             * password (i.e. he clicked cancel on a login dialog).
             * 
             * When this happens no dialog is raised. Application simply shuts
             * down.
             */
            exitWithError();
        }
        return false;
    }

}
