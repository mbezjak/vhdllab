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
package hr.fer.zemris.vhdllab.service.extractor.simulation;

import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.extractor.AbstractMetadataExtractor;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Collections;
import java.util.Set;

public class SimulationMetadataExtractor extends AbstractMetadataExtractor {

    @Override
    protected CircuitInterface doExtractCircuitInterface(String data) throws CircuitInterfaceExtractionException {
        throw new UnsupportedOperationException("Cant generate circuit interface for a simulation");
    }

    @Override
    protected Set<String> doExtractDependencies(String data) throws DependencyExtractionException {
        return Collections.emptySet();
    }

    @Override
    protected Result doGenerateVhdl(String data) throws VhdlGenerationException {
        throw new UnsupportedOperationException("Cant generate VHDL for a simulation");
    }

}
