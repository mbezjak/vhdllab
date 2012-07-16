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
package hr.fer.zemris.vhdllab.platform.manager.editor;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.platform.listener.EventPublisher;

import org.springframework.richclient.factory.ControlFactory;

public interface Editor extends ControlFactory {

    PlatformContainer getContainer();

    void setContainer(PlatformContainer container);

    void dispose();

    void undo();

    void redo();

    void setFile(File file);

    File getFile();

    boolean setModified(boolean flag);

    boolean isModified();

    void setEditable(boolean flag);

    void highlightLine(int line);

    EventPublisher<EditorListener> getEventPublisher();

    String getFileName();

    String getProjectName();

    void setMetadata(EditorMetadata metadata);

    EditorMetadata getMetadata();

    String getTitle();

    String getCaption();

}
