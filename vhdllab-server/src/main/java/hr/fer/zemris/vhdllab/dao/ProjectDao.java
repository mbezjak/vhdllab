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

import hr.fer.zemris.vhdllab.entity.Project;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

/**
 * This interface extends {@link EntityDao} to define extra methods for
 * {@link Project} entity.
 * <p>
 * An implementation of this interface must be stateless!
 * </p>
 */
@Transactional
public interface ProjectDao extends EntityDao<Project> {

    /**
     * Finds a project whose <code>userId</code> and <code>name</code> are
     * specified by parameters. <code>null</code> value will be returned if such
     * project doesn't exist.
     *
     * @param userId
     *            owner of project
     * @param name
     *            a name of a project
     * @throws NullPointerException
     *             if any parameter is <code>null</code>
     * @return specified project or <code>null</code> if such project doesn't
     *         exist
     */
    Project findByName(String userId, String name);

    /**
     * Finds all projects whose owner is specified user. Return value will never
     * be <code>null</code>, although it can be an empty list.
     *
     * @param userId
     *            owner of projects
     * @return list of user's projects
     */
    List<Project> findByUser(String userId);

}
