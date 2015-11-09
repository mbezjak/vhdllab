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

import hr.fer.zemris.vhdllab.dao.OwnedEntityDao;

import java.util.List;

import org.apache.commons.lang.Validate;

public abstract class AbstractOwnedEntityDao<T> extends AbstractEntityDao<T>
        implements OwnedEntityDao<T> {

    public AbstractOwnedEntityDao(Class<T> clazz) {
        super(clazz);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<T> findByUser(String userId) {
        Validate.notNull(userId, "User identifier can't be null");
        String query = "select e from " + clazz.getSimpleName()
                + " as e where e.userId = ?1 order by e.id";
        return getJpaTemplate().find(query, userId);
    }

}
