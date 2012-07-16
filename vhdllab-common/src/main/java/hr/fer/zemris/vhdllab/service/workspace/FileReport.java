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
package hr.fer.zemris.vhdllab.service.workspace;

import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;

import java.io.IOException;
import java.io.Serializable;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;

public final class FileReport implements Serializable {

    private static final long serialVersionUID = 2737580103123482939L;

    private final File file;
    private final Hierarchy hierarchy;

    public FileReport(File file, Hierarchy hierarchy) {
        Validate.notNull(file, "File can't be null");
        Validate.notNull(hierarchy, "Hierarchy can't be null");
        this.file = new File(file);
        this.hierarchy = hierarchy;
    }

    public File getFile() {
        return file;
    }

    public Hierarchy getHierarchy() {
        return hierarchy;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        file.setProject(hierarchy.getProject());
    }

}
