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
package hr.fer.zemris.vhdllab.platform.manager.workspace.collection;

import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.collections.Predicate;

public class CompilableFilesFilter implements Predicate {

    private static final Predicate INSTANCE = new CompilableFilesFilter();

    private CompilableFilesFilter() {
    }

    public static Predicate getInstance() {
        return INSTANCE;
    }

    @Override
    public boolean evaluate(Object object) {
        File file = (File) object;
        return file.getType().isCompilable();
    }

}
