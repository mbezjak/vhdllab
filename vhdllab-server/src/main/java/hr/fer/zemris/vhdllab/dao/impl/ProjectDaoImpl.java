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
package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.Project;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link ProjectDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class ProjectDaoImpl extends AbstractOwnedEntityDao<Project> implements
        ProjectDao {

    /**
     * Default constructor.
     */
    public ProjectDaoImpl() {
        super(Project.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.ProjectDao#findByName(java.lang.String,
     * java.lang.String)
     */
    @Override
    public Project findByName(String userId, String name) {
        Validate.notNull(userId, "User identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select p from Project as p where p.userId = ?1 and p.name = ?2 order by p.id";
        return findUniqueResult(query, userId, name);
    }

}
