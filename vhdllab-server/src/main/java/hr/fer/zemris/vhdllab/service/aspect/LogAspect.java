package hr.fer.zemris.vhdllab.service.aspect;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;

@Aspect
@Order(1000)
public class LogAspect {

    private static final Logger LOG = Logger.getLogger(LogAspect.class);

    @Pointcut("execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.ClientLogService.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.MetadataExtractionService.*(..))")
    public void services() {
    }

    @Around("services()")
    public Object logExecution(ProceedingJoinPoint pjp) throws Throwable {
        long start = 0;
        String callSignature = null;
        if (LOG.isTraceEnabled()) {
            Signature signature = pjp.getSignature();
            callSignature = signature.getDeclaringType().getSimpleName() + "."
                    + signature.getName();
            LOG.trace("Entering " + callSignature);
            start = System.currentTimeMillis();
        }
        Object returnValue = pjp.proceed();
        if (LOG.isTraceEnabled()) {
            long end = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder(1000);
            sb.append(callSignature);
            sb.append(" finished execution in ").append(end - start);
            sb.append(" ms for argument: ").append(
                    Arrays.toString(pjp.getArgs()));
            sb.append(" and returned: ").append(returnValue);
            LOG.trace(sb.toString());
        }
        return returnValue;
    }

}
