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
package hr.fer.zemris.vhdllab.platform.listener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EventListener;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

public abstract class AbstractEventPublisher<T extends EventListener>
        implements EventPublisher<T> {

    /**
     * Logger for this class
     */
    protected final Logger logger = Logger.getLogger(getClass());

    private final List<T> listeners;
    private final Class<T> listenerClass;

    protected AbstractEventPublisher(Class<T> listenerClass) {
        Validate.notNull(listenerClass, "Listener class can't be null");
        this.listenerClass = listenerClass;
        listeners = new ArrayList<T>();
    }

    @Override
    public void addListener(T listener) {
        Validate.notNull(listener, "Listener can't be null");
        listeners.add(listener);
        if (logger.isTraceEnabled()) {
            logger.trace("Added " + listenerClass.getSimpleName() + ": "
                    + listener);
        }
    }

    @Override
    public void removeListener(T listener) {
        Validate.notNull(listener, "Listener can't be null");
        listeners.remove(listener);
        if (logger.isTraceEnabled()) {
            logger.trace("Removed " + listenerClass.getSimpleName() + ": "
                    + listener);
        }
    }

    @Override
    public void removeListeners() {
        listeners.clear();
        if (logger.isTraceEnabled()) {
            logger.trace("Removed all listeners");
        }
    }

    @Override
    public List<T> getListeners() {
        return Collections.unmodifiableList(listeners);
    }

}
