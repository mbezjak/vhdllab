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

import static hr.fer.zemris.vhdllab.entity.stub.NamedEntityStub.NAME;
import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID;
import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID_2;
import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID_UPPERCASE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class OwnedEntityTest extends ValueObjectTestSupport {

    private OwnedEntity entity;

    @Before
    public void initEntity() throws Exception {
        entity = new OwnedEntityStub();
    }

    @Test
    public void basics() {
        OwnedEntity another = new OwnedEntity();
        assertNull("userId is set.", another.getUserId());

        another.setUserId(USER_ID);
        assertNotNull("userId is null.", another.getUserId());
        another.setUserId(null);
        assertNull("userId not cleared.", another.getUserId());
    }

    @Test
    public void constructorName() {
        OwnedEntity another = new OwnedEntity(NAME);
        assertEquals("name not same.", NAME, another.getName());
        assertNull("userId not null", another.getUserId());
    }

    @Test
    public void constructorUserIdAndName() {
        OwnedEntity another = new OwnedEntity(USER_ID, NAME);
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("name not same.", NAME, another.getName());
    }

    @Test
    public void copyConstructor() {
        OwnedEntity another = new OwnedEntity(entity);
        assertEquals("userId not same.", entity.getUserId(), another
                .getUserId());
    }

    @Test
    public void hashCodeAndEquals() {
        basicEqualsTest(entity);

        OwnedEntity another = new OwnedEntity(entity);
        assertEqualsAndHashCode(entity, another);

        another.setUserId(USER_ID_2);
        assertNotEqualsAndHashCode(entity, another);

        another.setUserId(USER_ID_UPPERCASE);
        assertEqualsAndHashCode(entity, another);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
