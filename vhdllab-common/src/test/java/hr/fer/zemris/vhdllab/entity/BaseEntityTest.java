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

import static hr.fer.zemris.vhdllab.entity.stub.BaseEntityStub.ID;
import static hr.fer.zemris.vhdllab.entity.stub.BaseEntityStub.VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.stub.BaseEntityStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class BaseEntityTest extends ValueObjectTestSupport {

    private BaseEntity entity;

    @Before
    public void initEntity() {
        entity = new BaseEntityStub();
    }

    @Test
    public void basics() {
        BaseEntity another = new BaseEntity();
        assertNull("id is set.", another.getId());
        assertNull("version is set.", another.getVersion());

        another.setId(ID);
        assertNotNull("id is null.", another.getId());
        another.setId(null);
        assertNull("id not cleared.", another.getId());

        another.setVersion(VERSION);
        assertNotNull("version is null.", another.getVersion());
        another.setVersion(null);
        assertNull("version not cleared.", another.getVersion());
    }

    @Test
    public void copyConstructor() {
        BaseEntity another = new BaseEntity(entity);
        assertEquals("id not same.", entity.getId(), another.getId());
        assertEquals("version not same.", entity.getVersion(), another
                .getVersion());
    }

    @Test(expected = NullPointerException.class)
    public void copyConstructorNullArgument() {
        new BaseEntity(null);
    }

    @Test
    public void isNew() {
        assertFalse("entity is new.", entity.isNew());
        entity.setVersion(null);
        assertFalse("entity is new.", entity.isNew());
        entity.setId(null);
        assertTrue("entity not new.", entity.isNew());
        entity.setVersion(VERSION);
        assertTrue("entity not new.", entity.isNew());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
