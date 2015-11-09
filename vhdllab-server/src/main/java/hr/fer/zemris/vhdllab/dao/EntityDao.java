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
package hr.fer.zemris.vhdllab.dao;

import org.springframework.transaction.annotation.Transactional;

/**
 * Defines common methods for manipulating entities. An entity is any object
 * that can be persisted by its data access object (DAO).
 *
 * @param <T>
 *            type of an entity
 */
@Transactional
public interface EntityDao<T> {

    /**
     * Persists a new entity. All constraints are defined in entity itself
     * through hibernate-validator annotations.
     *
     * @param entity
     *            an entity that should be persisted
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    void persist(T entity);

    /**
     * Updates an existing entity. All constraints are defined in entity itself
     * through hibernate-validator annotations.
     *
     * @param entity
     *            an entity that should be updated
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    T merge(T entity);

    /**
     * Retrieves an entity with specified identifier. <code>null</code> value
     * will be returned if such entity doesn't exist.
     *
     * @param id
     *            identifier of an entity
     * @return an entity with specified identifier or <code>null</code> if such
     *         entity doesn't exist
     * @throws NullPointerException
     *             if <code>id</code> is <code>null</code>
     */
    T load(Integer id);

    /**
     * Deletes an entity.
     *
     * @param entity
     *            an entity to delete
     * @throws NullPointerException
     *             if <code>entity</code> is <code>null</code>
     */
    void delete(T entity);

}
