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

import static hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub.PROJECT_ID;
import static hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub.PROJECT_ID_2;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.FileHistoryStub;
import hr.fer.zemris.vhdllab.entity.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub;
import hr.fer.zemris.vhdllab.entity.stub.HistoryStub2;
import hr.fer.zemris.vhdllab.entity.stub.ProjectStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class FileHistoryTest extends ValueObjectTestSupport {

    private FileHistory entity;

    @Before
    public void initEntity() throws Exception {
        entity = new FileHistoryStub();
    }

    @Test
    public void basics() {
        FileHistory another = new FileHistory();
        assertNull("project id is set.", another.getProjectId());
        assertNull("history is set.", another.getHistory());

        another.setProjectId(PROJECT_ID);
        assertNotNull("project id is null.", another.getProjectId());
        another.setProjectId(null);
        assertNull("project id not cleared.", another.getProjectId());

        another.setHistory(new HistoryStub());
        assertNotNull("history is null.", another.getHistory());
        another.setHistory(null);
        assertNull("history not cleared.", another.getHistory());
    }

    @Test
    public void constructorFileHistory() throws Exception {
        History history = new HistoryStub();
        File file = (File) CollectionUtils.get(new ProjectStub().getFiles(), 0);
        FileHistory another = new FileHistory(file, history);
        assertEquals("name not same.", file.getName(), another.getName());
        assertEquals("project id not same.", file.getProject().getId(), another
                .getProjectId());
        assertEquals("history not same.", history, another.getHistory());
    }

    @Test
    public void constructorFileInfoProjectIdHistory() throws Exception {
        History history = new HistoryStub();
        FileHistory another = new FileHistory(new FileInfoStub(), PROJECT_ID,
                history);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("project id not same.", PROJECT_ID, another.getProjectId());
        assertEquals("history not same.", history, another.getHistory());
        assertNull("id must not be copied.", another.getId());
        assertNull("version must not be copied.", another.getVersion());
    }

    @Test
    public void copyConstructor() {
        FileHistory another = new FileHistory(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("project id not same.", entity.getProjectId(), another
                .getProjectId());
        assertEquals("history not same.", entity.getHistory(), another
                .getHistory());
    }

    @Test(expected = NullPointerException.class)
    public void copyConstructorNullArgument() {
        new FileHistory(null);
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        FileHistory another = new FileHistory(entity);
        assertEqualsAndHashCode(entity, another);

        another.setProjectId(PROJECT_ID_2);
        assertNotEqualsAndHashCode(entity, another);

        another = new FileHistory(entity);
        another.setHistory(new HistoryStub2());
        assertNotEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
