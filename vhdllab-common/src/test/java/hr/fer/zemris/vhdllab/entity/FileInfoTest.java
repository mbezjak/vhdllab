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

import static hr.fer.zemris.vhdllab.entity.stub.FileInfoStub.DATA;
import static hr.fer.zemris.vhdllab.entity.stub.FileInfoStub.TYPE;
import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.FileInfoStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class FileInfoTest extends ValueObjectTestSupport {

    private FileInfo entity;

    @Before
    public void initEntity() throws Exception {
        entity = new FileInfoStub();
    }

    @Test
    public void basics() {
        FileInfo another = new FileInfo();
        assertNull("type is set.", another.getType());
        assertNull("data is set.", another.getData());

        another.setType(TYPE);
        assertNotNull("type is null.", another.getType());
        another.setType(null);
        assertNull("type not cleared.", another.getType());

        another.setData(DATA);
        assertNotNull("data is null.", another.getData());
        another.setData(null);
        assertNull("data not cleared.", another.getData());
    }

    @Test
    public void constructor() {
        FileInfo another = new FileInfo(NAME, TYPE, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("type not same.", TYPE, another.getType());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void copyConstructor() {
        FileInfo another = new FileInfo(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("type not same.", entity.getType(), another.getType());
        assertEquals("data not same.", entity.getData(), another.getData());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
