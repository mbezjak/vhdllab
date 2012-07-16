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

import static org.junit.Assume.assumeTrue;
import hr.fer.zemris.vhdllab.dao.EntityDao;
import hr.fer.zemris.vhdllab.dao.impl.support.AbstractDaoSupport;
import hr.fer.zemris.vhdllab.dao.impl.support.FileInfoDao;
import hr.fer.zemris.vhdllab.dao.impl.support.FileInfoTable;
import hr.fer.zemris.vhdllab.entity.FileType;

import javax.annotation.Resource;

import org.apache.commons.lang.RandomStringUtils;
import org.hibernate.validator.InvalidStateException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * Tests for {@link FileInfoDao}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class FileInfoDaoTest extends AbstractDaoSupport {

    @Resource(name = "fileInfoDao")
    private EntityDao<FileInfoTable> dao;
    private FileInfoTable entity;

    @Before
    public void initEachTest() {
        entity = new FileInfoTable();
    }

    /**
     * Type can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void typeIsNull() {
        entity.setName("name");
        entity.setData("data");
        entity.setType(null);
        dao.persist(entity);
    }

    /**
     * Data can't be null.
     */
    @Test(expected = DataIntegrityViolationException.class)
    public void dataIsNull() {
        entity.setName("name");
        entity.setType(FileType.SOURCE);
        entity.setData(null);
        dao.persist(entity);
    }

    /**
     * Data is too long.
     */
    @Test(expected = InvalidStateException.class)
    public void dataTooLong() {
        // assume there is enough memory for this test
        assumeTrue(Runtime.getRuntime().freeMemory() > 17 * 1000 * 1000);
        entity.setName("name");
        entity.setType(FileType.SOURCE);
        entity.setData(RandomStringUtils.randomAlphabetic(16000001));
        dao.persist(entity);
    }

    /**
     * Type can't be updated.
     */
    @Test
    public void typeNotUpdateable() {
        entity.setName("name");
        entity.setData("data");
        entity.setType(FileType.SOURCE);
        dao.persist(entity);
        FileType newType = FileType.AUTOMATON;
        entity.setType(newType);
        dao.merge(entity);
        closeEntityManager(); // flush cache
        createEntityManager();
        FileInfoTable loaded = (FileInfoTable) entityManager.createQuery(
                "select e from FileInfoTable e").getSingleResult();
        assertFalse(newType.equals(loaded.getType()));
    }

}
