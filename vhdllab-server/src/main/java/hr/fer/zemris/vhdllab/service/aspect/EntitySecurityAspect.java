package hr.fer.zemris.vhdllab.service.aspect;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.PreferencesFile;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.impl.ServiceSupport;
import hr.fer.zemris.vhdllab.service.util.SecurityUtils;

import java.util.List;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.core.annotation.Order;
import org.springframework.security.AccessDeniedException;

@Aspect
@Order(500)
public class EntitySecurityAspect extends ServiceSupport {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(EntitySecurityAspect.class);

    @Before("(execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.deleteProject(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.extractHierarchy(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.findByName(..))) && args(id,..)")
    public void projectSecurity(JoinPoint jp, Integer id) throws Throwable {
        Project project = loadProject(id);
        checkSecurity(jp, project.getUserId(), id);
    }

    @Before("(execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.deleteFile(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.MetadataExtractionService.*(..))) && args(id,..)")
    public void fileSecurity(JoinPoint jp, Integer id) throws Throwable {
        File file = loadFile(id);
        checkSecurity(jp, file.getProject().getUserId(), id);
    }

    @Before("execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.save(..)) && args(file,..)")
    public void saveFileSecurity(JoinPoint jp, File file) throws Throwable {
        Project project = loadProject(file.getProject().getId());
        checkSecurity(jp, project.getUserId(), project.getId());
    }

    @Before("execution(* hr.fer.zemris.vhdllab.service.PreferencesFileService.save(..)) && args(files,..)")
    public void savePreferencesFileSecurity(JoinPoint jp,
            List<PreferencesFile> files) throws Throwable {
        for (PreferencesFile file : files) {
            checkSecurity(jp, file.getUserId(), file.getId());
        }
    }

    private void checkSecurity(JoinPoint jp, String entityUser, Integer id) {
        String loggedInUser = SecurityUtils.getUser();
        if (!loggedInUser.equalsIgnoreCase(entityUser)) {
            StringBuilder sb = new StringBuilder(200);
            sb.append(loggedInUser).append(" tried to invoke ");
            sb.append(getCallSignature(jp));
            sb.append(" with argument id=").append(id);
            sb.append(" that belongs to ").append(entityUser);
            LOG.error(sb.toString());
            throw new AccessDeniedException("Entity doesn't belong to user "
                    + loggedInUser);
        }
    }

    private String getCallSignature(JoinPoint jp) {
        Signature signature = jp.getSignature();
        return signature.getDeclaringType().getSimpleName() + "."
                + signature.getName();
    }

}
