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

import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.OwnedEntityTable;
import hr.fer.zemris.vhdllab.dao.impl.support.OwnedEntityTableDao;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link OwnedEntityTableDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class OwnedEntityDaoTest extends AbstractDaoSupport {

    @Resource(name = "ownedEntityTableDao")
    private EntityDao<OwnedEntityTable> dao;
    private OwnedEntityTable entity;

    @Before
    public void initEachTest() {
        entity = new OwnedEntityTable();
    }

    /**
     * UserId can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void userIdIsNull() {
        entity.setName("name");
        entity.setUserId(null);
        dao.persist(entity);
    }

    /**
     * UserId can't be empty.
     */
    @Test(expected = InvalidStateException.class)
    public void userIdIsEmpty() {
        entity.setName("name");
        entity.setUserId("");
        dao.persist(entity);
    }

    /**
     * UserId is too long.
     */
    @Test(expected = InvalidStateException.class)
    public void userIdTooLong() {
        entity.setName("name");
        entity.setUserId(RandomStringUtils.randomAlphabetic(256));
        dao.persist(entity);
    }

    /**
     * UserId can't be updated.
     */
    @Test
    public void userIdAndTypeNotUpdateable() {
        entity.setName("name");
        entity.setUserId("userId");
        dao.persist(entity);

        String newUserId = "newUserId";
        entity.setUserId(newUserId);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();

        OwnedEntityTable loaded = (OwnedEntityTable) entityManager.createQuery(
                "select e from OwnedEntityTable e").getSingleResult();
        assertFalse(newUserId.equals(loaded.getUserId()));
    }

}
