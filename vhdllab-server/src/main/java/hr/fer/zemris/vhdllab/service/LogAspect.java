package hr.fer.zemris.vhdllab.service;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class LogAspect {

    private static final Logger LOG = Logger.getLogger(LogAspect.class);

    @Around(value = "execution(* hr.fer.zemris.vhdllab.service.Compiler.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.HierarchyExtractor.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator.*(..))")
    public Object afterExecution(ProceedingJoinPoint pjp) throws Throwable {
        long start = System.currentTimeMillis();
        Object returnedValue = pjp.proceed();
        if (LOG.isDebugEnabled()) {
            long end = System.currentTimeMillis();
            StringBuilder sb = new StringBuilder(500);
            sb.append(pjp.getSignature().getDeclaringType().getSimpleName());
            sb.append(" finished execution in ").append(end - start);
            sb.append(" ms for argument: ").append(Arrays.toString(pjp.getArgs()));
            sb.append(" and returned: ").append(returnedValue.toString());
            LOG.debug(sb.toString());
        }
        return returnedValue;
    }

}
