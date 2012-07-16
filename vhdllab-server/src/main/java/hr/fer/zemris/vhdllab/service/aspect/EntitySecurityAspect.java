/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.createFile(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.extractHierarchy(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.findByName(..))) && args(id,..)")
    public void projectSecurity(JoinPoint jp, Integer id) throws Throwable {
        Project project = loadProject(id);
        checkSecurity(jp, project.getUserId(), id);
    }

    @Before("(execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.deleteFile(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.WorkspaceService.saveFile(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.Simulator.*(..)) || "
            + "execution(* hr.fer.zemris.vhdllab.service.MetadataExtractionService.*(..))) && args(id,..)")
    public void fileSecurity(JoinPoint jp, Integer id) throws Throwable {
        File file = loadFile(id);
        if (file != null) {
            Project project = file.getProject();

            if (project != null) {
                checkSecurity(jp, project.getUserId(), id);
            }
        }
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
