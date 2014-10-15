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
package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.platform.manager.shutdown.ShutdownAdapter;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.log4j.Logger;

public class FlushPreferencesOnShutdownListener extends ShutdownAdapter {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(FlushPreferencesOnShutdownListener.class);

    @Override
    public void shutdownWithoutGUI() {
        try {
            Preferences.userRoot().flush();
        } catch (BackingStoreException e) {
            LOG.error("Error while flushing preferences", e);
        }
    }

}
