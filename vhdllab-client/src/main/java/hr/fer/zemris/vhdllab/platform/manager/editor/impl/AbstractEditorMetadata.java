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
package hr.fer.zemris.vhdllab.platform.manager.editor.impl;

import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;
import hr.fer.zemris.vhdllab.platform.manager.editor.EditorMetadata;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

public abstract class AbstractEditorMetadata implements EditorMetadata {

    private final Class<? extends Editor> editorClass;
    protected final String code;

    public AbstractEditorMetadata(Class<? extends Editor> editorClass) {
        Validate.notNull(editorClass, "Editor class can't be null");
        this.editorClass = editorClass;
        this.code = StringUtils.uncapitalize(getClass().getSimpleName()
                .replace("Metadata", ""));
    }

    @Override
    public Class<? extends Editor> getEditorClass() {
        return editorClass;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                    .append(code)
                    .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractEditorMetadata))
            return false;
        AbstractEditorMetadata other = (AbstractEditorMetadata) obj;
        return new EqualsBuilder()
                    .append(code, other.code)
                    .isEquals();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .append("viewName", code)
                    .toString();
    }

}
