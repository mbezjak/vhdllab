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

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public class EditorIdentifier {

    private final EditorMetadata metadata;
    private final File instanceModifier;

    public EditorIdentifier(EditorMetadata metadata) {
        this(metadata, null);
    }

    public EditorIdentifier(EditorMetadata metadata, File instanceModifier) {
        Validate.notNull(metadata, "Editor metadata can't be null");
        this.metadata = metadata;
        this.instanceModifier = instanceModifier;
    }

    public EditorMetadata getMetadata() {
        return metadata;
    }


    public File getInstanceModifier() {
        return instanceModifier;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(metadata)
                    .append(instanceModifier)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof EditorIdentifier))
            return false;
        EditorIdentifier other = (EditorIdentifier) obj;
        return new EqualsBuilder()
                    .append(metadata, other.metadata)
                    .append(instanceModifier, other.instanceModifier)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("metadata", metadata)
                    .append("instanceModifier", instanceModifier)
                    .toString();
    }

}
