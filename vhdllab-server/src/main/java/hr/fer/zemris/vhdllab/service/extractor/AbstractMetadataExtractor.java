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
package hr.fer.zemris.vhdllab.service.extractor;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.impl.ServiceSupport;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Set;

public abstract class AbstractMetadataExtractor extends ServiceSupport
        implements MetadataExtractor {

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        return doExtractCircuitInterface(file.getData());
    }

    protected abstract CircuitInterface doExtractCircuitInterface(String data)
            throws CircuitInterfaceExtractionException;

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        return doExtractDependencies(file.getData());
    }

    protected abstract Set<String> doExtractDependencies(String data)
            throws DependencyExtractionException;

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        return doGenerateVhdl(file.getData());
    }

    protected abstract Result doGenerateVhdl(String data)
            throws VhdlGenerationException;

}
