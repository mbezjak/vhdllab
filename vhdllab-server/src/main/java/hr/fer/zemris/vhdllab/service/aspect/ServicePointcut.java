package hr.fer.zemris.vhdllab.service.aspect;

import org.aspectj.lang.annotation.Pointcut;

public class ServicePointcut {

    @Pointcut("remoteServices() || target(hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor)")
            public void services() {
    }

    @Pointcut("target(hr.fer.zemris.vhdllab.service.FileService) || "
            + "target(hr.fer.zemris.vhdllab.service.ProjectService) ||"
            + "target(hr.fer.zemris.vhdllab.service.LibraryFileService) ||"
            + "target(hr.fer.zemris.vhdllab.service.UserFileService) ||"
            + "target(hr.fer.zemris.vhdllab.service.Compiler) ||"
            + "target(hr.fer.zemris.vhdllab.service.Simulator) ||"
            + "target(hr.fer.zemris.vhdllab.service.HierarchyExtractor) ||"
            + "target(hr.fer.zemris.vhdllab.service.filetype.CircuitInterfaceExtractor) ||"
            + "target(hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator)")
    public void remoteServices() {
    }

}
