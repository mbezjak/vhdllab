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
package hr.fer.zemris.vhdllab.platform.javaws;

import hr.fer.zemris.vhdllab.platform.gui.dialog.DialogManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.jnlp.ServiceManager;
import javax.jnlp.SingleInstanceListener;
import javax.jnlp.SingleInstanceService;
import javax.jnlp.UnavailableServiceException;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public final class JavawsIntegration {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger.getLogger(JavawsIntegration.class);

    @Resource(name = "alreadyOpenedDialogManager")
    private DialogManager dialogManager;
    private SingleInstanceService service;
    private SingleInstanceListener listener;

    @PostConstruct
    public void registerServices() {
        try {
            service = (SingleInstanceService) ServiceManager
                    .lookup("javax.jnlp.SingleInstanceService");
        } catch (UnavailableServiceException ignored) {
            service = null;
            LOG.debug("Single instance service isn't available");
            /*
             * Simply ignore exception because Javaws is not required to run
             * vhdllab-client.
             */
        }
        if (service != null) {
            listener = new EnforceSingleInstanceListener(dialogManager);
            service.addSingleInstanceListener(listener);
            LOG.debug("Successfully integated single instance service with javaws");
        }
    }

    @PreDestroy
    public void unregisterServices() {
        if (service != null) {
            service.removeSingleInstanceListener(listener);
            LOG.debug("Removed single instance listener from javaws service");
        }
    }

}
