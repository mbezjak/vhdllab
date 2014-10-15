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
package hr.fer.zemris.vhdllab.platform.manager.shutdown;

import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;

public final class ShutdownManager extends
        AbstractEventPublisher<ShutdownListener> {

    public ShutdownManager() {
        super(ShutdownListener.class);
    }

    public boolean shutdownWithGUI() {
        logger.debug("Closing application");
        ShutdownEvent event = new ShutdownEvent();
        fireShutdownWithGUI(event);
        return event.isShutdownCanceled();
    }

    public void shutdownWithoutGUI() {
        logger.debug("Shutting down application");
        fireShutdownWithoutGUI();
    }

    private void fireShutdownWithGUI(ShutdownEvent event) {
        for (ShutdownListener l : getListeners()) {
            l.shutdownWithGUI(event);
            if (event.isShutdownCanceled()) {
                break;
            }
        }
    }

    private void fireShutdownWithoutGUI() {
        for (ShutdownListener l : getListeners()) {
            try {
                l.shutdownWithoutGUI();
            } catch (RuntimeException e) {
                logger.debug("Error in shutdown listener", e);
                // suppress any error in shutdown procedure without GUI
            }
        }
    }

}
