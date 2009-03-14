package hr.fer.zemris.vhdllab.service.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class ServicePointcut {

    @Pointcut("execution(* hr.fer.zemris.vhdllab.service.FileService.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.ProjectService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.ClientLogService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.MetadataExtractionService.*(..))")
    public void services() {
    }

}
