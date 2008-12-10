package hr.fer.zemris.vhdllab.platform.remoting.exception;

import javax.annotation.Resource;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class RemoteExceptionHandlingAspect {

    @Resource(name = "exceptionHandlerProvider")
    private ExceptionHandler exceptionHandler;

    @AfterThrowing(value = "execution(* org.springframework.remoting.httpinvoker.HttpInvokerRequestExecutor.executeRequest(..))", throwing = "e")
    public void processExceptionWhenConnectingToServer(Exception e) {
        exceptionHandler.handleException(e);
    }

}
