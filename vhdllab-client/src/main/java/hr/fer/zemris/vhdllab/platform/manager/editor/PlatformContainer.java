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

import hr.fer.zemris.vhdllab.platform.i18n.AbstractLocalizationSource;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationManager;
import hr.fer.zemris.vhdllab.platform.manager.workspace.IdentifierToInfoObjectMapper;
import hr.fer.zemris.vhdllab.platform.manager.workspace.WorkspaceManager;
import hr.fer.zemris.vhdllab.service.MetadataExtractionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PlatformContainer extends AbstractLocalizationSource {

    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Autowired
    private IdentifierToInfoObjectMapper mapper;
    @Autowired
    private SimulationManager simulationManager;
    @Autowired
    private WorkspaceManager workspaceManager;
    @Autowired
    private MetadataExtractionService metadataExtractionService;

    public EditorManagerFactory getEditorManagerFactory() {
        return editorManagerFactory;
    }


    public IdentifierToInfoObjectMapper getMapper() {
        return mapper;
    }

    public SimulationManager getSimulationManager() {
        return simulationManager;
    }

    public WorkspaceManager getWorkspaceManager() {
        return workspaceManager;
    }

    public MetadataExtractionService getMetadataExtractionService() {
        return metadataExtractionService;
    }

}
