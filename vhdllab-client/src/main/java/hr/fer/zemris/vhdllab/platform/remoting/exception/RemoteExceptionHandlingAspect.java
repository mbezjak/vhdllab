package hr.fer.zemris.vhdllab.platform.remoting.exception;

import hr.fer.zemris.vhdllab.platform.context.ApplicationContext;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;
import hr.fer.zemris.vhdllab.platform.context.ApplicationState;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RemoteExceptionHandlingAspect {

    @Resource(name = "exceptionHandlerProvider")
    private ExceptionHandler exceptionHandler;

    @Around(value = "execution(* org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor.executeRequest(..))")
    public Object processExceptionWhenConnectingToServer(ProceedingJoinPoint pjp)
            throws Throwable {
        Object returnValue;
        try {
            returnValue = pjp.proceed();
        } catch (Exception e) {
            exceptionHandler.handleException(e);
            throw e;
        }
        ApplicationContext context = ApplicationContextHolder.getContext();
        if (!context.isRunning()) {
            context.setState(ApplicationState.RUNNING);
        }
        return returnValue;
    }

}
