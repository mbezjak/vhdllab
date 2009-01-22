package hr.fer.zemris.vhdllab.platform.gui.aspect;

import javax.swing.SwingUtilities;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class ExecuteInEDTAspect {

    /**
     * Logger for this class
     */
    static final Logger LOG = Logger.getLogger(ExecuteInEDTAspect.class);

    @Around("execution(* hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.platform.manager.component.ComponentContainer.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.platform.manager.view.ViewManager.*(..))")
    public Object executeInEDT(final ProceedingJoinPoint pjp) throws Throwable {
        final String className = pjp.getSourceLocation().getWithinType()
                .getSimpleName();
        if (SwingUtilities.isEventDispatchThread()) {
            if (LOG.isTraceEnabled()) {
                LOG.trace("Executing " + className
                                + " in EDT (current thread)");
            }
            return pjp.proceed();
        }
        final Object[] resultHolder = new Object[1];
        SwingUtilities.invokeAndWait(new Runnable() {
            @Override
            public void run() {
                try {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("Executing " + className + " in EDT");
                    }
                    resultHolder[0] = pjp.proceed();
                } catch (Throwable e) {
                    throw new IllegalStateException(e);
                }
            }
        });
        return resultHolder[0];
    }

}
