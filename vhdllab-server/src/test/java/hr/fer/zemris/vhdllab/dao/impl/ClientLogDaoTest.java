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

import hr.fer.zemris.vhdllab.dao.ClientLogDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.entity.ClientLog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.UnhandledException;
import org.apache.commons.lang.time.DateFormatUtils;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

public class ClientLogDaoTest extends AbstractDaoSupport {

    public static final Date CREATED_ON;

    static {
        SimpleDateFormat formatter = new SimpleDateFormat(
                DateFormatUtils.ISO_DATE_FORMAT.getPattern());
        try {
            CREATED_ON = formatter.parse("2008-01-05");
        } catch (ParseException e) {
            throw new UnhandledException(e);
        }
    }

    @Autowired
    private ClientLogDao dao;
    private ClientLog entity;

    @Before
    public void initEachTest() {
        entity = new ClientLog();
    }

    /**
     * Data can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void dataIsNull() {
        entity.setName("name");
        entity.setUserId("userId");
        entity.setCreatedOn(new Date());
        entity.setData(null);
        dao.persist(entity);
    }

    /**
     * Created on can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void createdOnIsNull() {
        entity.setName("name");
        entity.setUserId("userId");
        entity.setData("data");
        entity.setCreatedOn(null);
        dao.persist(entity);
    }

    /**
     * Created on can't be updated.
     */
    @Test
    public void createdOnNotUpdateable() {
        entity.setName("name");
        entity.setUserId("userId");
        entity.setData("data");
        entity.setCreatedOn(new Date());
        dao.persist(entity);

        Date newCreatedOn = CREATED_ON;
        entity.setCreatedOn(newCreatedOn);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();
        ClientLog loaded = dao.load(entity.getId());
        assertFalse(newCreatedOn.equals(loaded.getCreatedOn()));
    }

    /**
     * UserId and created on are unique.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void uniqueName() {
        Date createdOn = new Date();
        String userId = "userId";
        entity.setName("name");
        entity.setUserId(userId);
        entity.setData("data");
        entity.setCreatedOn(createdOn);
        dao.persist(entity);

        entity = new ClientLog();
        entity.setUserId(userId);
        entity.setCreatedOn(createdOn);
        entity.setName("new name");
        entity.setData("new data");
        dao.persist(entity);
    }

}
