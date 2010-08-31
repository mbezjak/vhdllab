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
package hr.fer.zemris.vhdllab.platform.manager.simulation;

import hr.fer.zemris.vhdllab.applets.simulations.WaveAppletMetadata;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;
import hr.fer.zemris.vhdllab.platform.listener.AbstractEventPublisher;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorIdentifier;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManager;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorManagerFactory;
import hr.fer.zemris.vhdllab.platform.manager.editor.SaveContext;
import hr.fer.zemris.vhdllab.service.Simulator;
import hr.fer.zemris.vhdllab.service.exception.NoAvailableProcessException;
import hr.fer.zemris.vhdllab.service.exception.SimulatorTimeoutException;
import hr.fer.zemris.vhdllab.service.result.CompilationMessage;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultSimulationManager extends
        AbstractEventPublisher<SimulationListener> implements SimulationManager {

    private static final String COMPILED_MESSAGE = "notification.compiled";
    private static final String SIMULATED_MESSAGE = "notification.simulated";

    @Autowired
    private Simulator simulator;
    @Autowired
    private EditorManagerFactory editorManagerFactory;
    @Resource(name = "standaloneLocalizationSource")
    private LocalizationSource localizationSource;

    private File lastCompiledFile;
    private File lastSimulatedFile;

    public DefaultSimulationManager() {
        super(SimulationListener.class);
    }

    @Override
    public void compile(File file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isCompilable()) {
            logger.info(file.getName() + " isn't compilable");
            return;
        }
        Project project = file.getProject();
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project);
        boolean shouldCompile = em.save(true, SaveContext.COMPILE_AFTER_SAVE);
        if (shouldCompile) {
            List<CompilationMessage> messages;
            try {
                messages = simulator.compile(file.getId());
            } catch (SimulatorTimeoutException e) {
                String message = localizationSource.getMessage(
                        "simulator.compile.timout", new Object[] { file
                                .getName() });
                messages = Collections.singletonList(new CompilationMessage(
                        message));
            } catch (NoAvailableProcessException e) {
                String message = localizationSource.getMessage(
                        "simulator.compile.no.processes", new Object[] { file
                                .getName() });
                messages = Collections.singletonList(new CompilationMessage(
                        message));
            }
            lastCompiledFile = file;
            fireCompiled(file, messages);
            logger.info(localizationSource.getMessage(COMPILED_MESSAGE,
                    new Object[] { file.getName(), project.getName() }));
        }
    }

    @Override
    public File getLastCompiledFile() {
        return lastCompiledFile;
    }

    @Override
    public void compileLast() {
        compile(getLastCompiledFile());
    }

    private void fireCompiled(File compiledFile, List<CompilationMessage> messages) {
        for (SimulationListener l : getListeners()) {
            l.compiled(compiledFile, messages);
        }
    }

    @Override
    public void simulate(File file) {
        Validate.notNull(file, "File can't be null");
        if (!file.getType().isSimulatable()) {
            logger.info(file.getName() + " isn't simulatable");
            return;
        }
        Project project = file.getProject();
        EditorManager em = editorManagerFactory
                .getAllAssociatedWithProject(project);
        boolean shouldSimulate = em.save(true, SaveContext.SIMULATE_AFTER_SAVE);
        if (shouldSimulate) {
            Result result;
            try {
                result = simulator.simulate(file.getId());
            } catch (SimulatorTimeoutException e) {
                String message = localizationSource.getMessage(
                        "simulator.simulate.timout", new Object[] { file
                                .getName() });
                result = new Result(Collections.singletonList(message));
            } catch (NoAvailableProcessException e) {
                String message = localizationSource.getMessage(
                        "simulator.simulate.no.processes", new Object[] { file
                                .getName() });
                result = new Result(Collections.singletonList(message));
            }
            lastSimulatedFile = file;
            fireSimulated(file, result);
            openSimulationEditor(file, result);
            logger.info(localizationSource.getMessage(SIMULATED_MESSAGE,
                    new Object[] { file.getName(), project.getName() }));
        }
    }

    @Override
    public File getLastSimulatedFile() {
        return lastSimulatedFile;
    }

    @Override
    public void simulateLast() {
        simulate(getLastSimulatedFile());
    }

    private void fireSimulated(File simulatedFile, Result result) {
        for (SimulationListener l : getListeners()) {
            l.simulated(simulatedFile, result);
        }
    }

    private void openSimulationEditor(File file, Result result) {
        String waveform = result.getData();
        if (!StringUtils.isBlank(waveform)) {
            File simulationFile = new File(file.getName() + ":sim", FileType.SIMULATION,
                    waveform);
            simulationFile.setProject(file.getProject());
            EditorIdentifier identifier = new EditorIdentifier(
                    new WaveAppletMetadata(), simulationFile);
            EditorManager simulationEditor = editorManagerFactory
                    .get(identifier);
            if (simulationEditor.isOpened()) {
                simulationEditor.close();
            }
            simulationEditor.open();
        }
    }

}
