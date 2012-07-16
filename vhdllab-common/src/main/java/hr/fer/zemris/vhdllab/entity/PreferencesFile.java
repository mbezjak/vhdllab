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
package hr.fer.zemris.vhdllab.entity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.Length;
import org.hibernate.validator.NotNull;

@Entity
@Table(name = "preferences_files", uniqueConstraints = { @UniqueConstraint(columnNames = {
        "user_id", "name" }) })
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PreferencesFile extends OwnedEntity {

    private static final long serialVersionUID = 2916992978945246032L;

    @NotNull
    @Length(max = 16000000) // ~ 16MB
    private String data;

    public PreferencesFile() {
        super();
    }

    public PreferencesFile(String name, String data) {
        this(null, name, data);
    }

    public PreferencesFile(String userId, String name, String data) {
        super(userId, name);
        setData(data);
    }

    public PreferencesFile(PreferencesFile clone) {
        super(clone);
        setData(clone.data);
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                    .appendSuper(super.toString())
                    .append("dataLength", StringUtils.length(data))
                    .toString();
    }

}
