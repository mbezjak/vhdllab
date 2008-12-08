package hr.fer.zemris.vhdllab.service.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class ServicePointcut {

    @Pointcut("remoteServices() || execution(* hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor.*(..))")
            public void services() {
    }

    @Pointcut("execution(* hr.fer.zemris.vhdllab.service.FileService.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.ProjectService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.LibraryFileService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.UserFileService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.Compiler.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.HierarchyExtractor.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor.*(..)) ||"
            + "execution(* hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator.*(..))")
    public void remoteServices() {
    }

}
