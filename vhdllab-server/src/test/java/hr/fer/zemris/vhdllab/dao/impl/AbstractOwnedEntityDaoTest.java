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

import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.OwnedEntityTable;
import hr.fer.zemris.vhdllab.dao.impl.support.OwnedEntityTableDao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCallback;

public class AbstractOwnedEntityDaoTest extends AbstractDaoSupport {

    private AbstractOwnedEntityDao<?> dao;

    @PostConstruct
    public void initDao() {
        /*
         * It doesn't matter which entity will be used so we take the simplest
         * one but also one that will be useful in find* tests.
         */
        dao = new OwnedEntityTableDao();
        dao.setEntityManagerFactory(entityManagerFactory);
    }

    /**
     * user id is null
     */
    @Test(expected = IllegalArgumentException.class)
    public void findByUserNullUserId() {
        dao.findByUser(null);
    }

    /**
     * non-existing user id
     */
    @Test
    public void findByUserNonExistingUserId() {
        // DB is empty here
        assertTrue(dao.findByUser("non-existing-user-id").isEmpty());
    }

    /**
     * everything ok. one entity in collection
     */
    @Test
    public void findByUser() {
        OwnedEntityTable entity = setupOwnedEntity("user", "name");
        List<OwnedEntityTable> list = Collections.singletonList(entity);
        assertEquals("lists not equal.", list, dao.findByUser("user"));
        assertEquals("user id is not case insensitive.", list, dao
                .findByUser("USER"));
    }

    /**
     * everything ok. two projects in collection
     */
    @Test
    public void findByUser2() {
        List<OwnedEntityTable> list = new ArrayList<OwnedEntityTable>(2);
        list.add(setupOwnedEntity("user", "name1"));
        list.add(setupOwnedEntity("user", "name2"));
        assertEquals("collections not equal.", list, dao.findByUser("user"));
        assertEquals("user id is not case insensitive.", list, dao
                .findByUser("USER"));
    }

    /**
     * everything ok. two collections
     */
    @Test
    public void findByUser3() {
        List<OwnedEntityTable> list1 = Collections
                .singletonList(setupOwnedEntity("user1", "name"));
        List<OwnedEntityTable> list2 = Collections
                .singletonList(setupOwnedEntity("user2", "name"));

        assertEquals("collections not equal.", list1, dao.findByUser("user1"));
        assertEquals("user id is not case insensitive.", list1, dao
                .findByUser("USER1"));
        assertEquals("collections not equal.", list2, dao.findByUser("user2"));
        assertEquals("user id is not case insensitive.", list2, dao
                .findByUser("USER2"));
    }

    private OwnedEntityTable setupOwnedEntity(final String userId,
            final String name) {
        String query = createInsertStatement("OwnedEntityTable",
                "id, version, user_id, name", "null, 0, ?, ?");
        getJdbcTemplate().execute(query, new PreparedStatementCallback() {
            @Override
            public Object doInPreparedStatement(PreparedStatement ps)
                    throws SQLException, DataAccessException {
                ps.setString(1, userId);
                ps.setString(2, name);
                return ps.execute();
            }
        });
        return new OwnedEntityTable(userId, name);
    }

}
