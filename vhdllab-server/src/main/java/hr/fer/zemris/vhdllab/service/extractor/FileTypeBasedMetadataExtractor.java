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
import hr.fer.zemris.vhdllab.service.result.Result;

import java.util.Locale;
import java.util.Set;

import org.hibernate.validator.ClassValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class FileTypeBasedMetadataExtractor implements MetadataExtractor {

    private static final ClassValidator<CircuitInterface> CIRCUIT_INTERFACE_VALIDATOR =
                            new ClassValidator<CircuitInterface>(CircuitInterface.class);

    @Autowired
    private ApplicationContext context;

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        CircuitInterface ci = getBean(file).extractCircuitInterface(file);
        if (ci == null) {
            throw new IllegalStateException("Circuit interface is null");
        }
        CIRCUIT_INTERFACE_VALIDATOR.assertValid(ci);
        return ci;
    }

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        Set<String> dependencies = getBean(file).extractDependencies(file);
        if (dependencies == null) {
            throw new IllegalStateException("Dependencies is null");
        }
        return dependencies;
    }

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        Result result = getBean(file).generateVhdl(file);
        if (result == null) {
            throw new IllegalStateException("Result is null");
        }
        return result;
    }

    private MetadataExtractor getBean(File file) {
        String type = file.getType().toString().toLowerCase(Locale.ENGLISH);
        String interfaceName = MetadataExtractor.class.getSimpleName();
        String beanName = type + interfaceName;
        return (MetadataExtractor) context.getBean(beanName,
                MetadataExtractor.class);
    }

}
