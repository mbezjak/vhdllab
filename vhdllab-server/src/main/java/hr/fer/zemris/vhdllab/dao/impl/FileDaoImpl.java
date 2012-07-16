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

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.entity.File;

import org.apache.commons.lang.Validate;

/**
 * This is a default implementation of {@link FileDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileDaoImpl extends AbstractEntityDao<File> implements FileDao {

    /**
     * Default constructor.
     */
    public FileDaoImpl() {
        super(File.class);
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.dao.EntityDAO#delete(java.lang.Object)
     */
    @Override
    public void delete(File entity) {
        /*
         * If file and it's project are persisted and then a file attempts to be
         * deleted, all in the same session (EntityManager), file needs to be
         * removed from project before deleting it. Check
         * FileDAOImplTest#delete2() test to see an example.
         */
        entity.getProject().removeFile(entity);
        super.delete(entity);
    }

    /* (non-Javadoc)
     * @see hr.fer.zemris.vhdllab.dao.FileDao#findByName(java.lang.Integer, java.lang.String)
     */
    @Override
    public File findByName(Integer projectId, String name) {
        Validate.notNull(projectId, "Project identifier can't be null");
        Validate.notNull(name, "Name can't be null");
        String query = "select f from File as f where f.project.id = ?1 and f.name = ?2 order by f.id";
        return findUniqueResult(query, projectId, name);
    }

}
