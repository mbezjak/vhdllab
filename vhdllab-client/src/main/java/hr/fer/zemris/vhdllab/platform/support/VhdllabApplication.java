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
package hr.fer.zemris.vhdllab.platform.support;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.application.ApplicationDescriptor;
import org.springframework.richclient.application.config.ApplicationLifecycleAdvisor;

public class VhdllabApplication extends Application {

    private boolean forceShutdownInProgress;

    public VhdllabApplication(ApplicationDescriptor descriptor,
            ApplicationLifecycleAdvisor advisor) {
        super(descriptor, advisor);
    }

    public boolean isForceShutdownInProgress() {
        return forceShutdownInProgress;
    }

    @Override
    public void close(boolean force, int exitCode) {
        forceShutdownInProgress = force;
        super.close(force, exitCode);
    }

}
