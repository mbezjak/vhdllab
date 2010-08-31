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

import static hr.fer.zemris.vhdllab.entity.stub.OwnedEntityStub.USER_ID;
import static hr.fer.zemris.vhdllab.entity.stub.PreferencesFileStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.ClientLogStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

public class ClientLogTest extends ValueObjectTestSupport {

    private ClientLog entity;

    @Before
    public void initEntity() throws Exception {
        entity = new ClientLogStub();
    }

    @Test
    public void basics() {
        ClientLog another = new ClientLog();
        assertNull("data is set.", another.getData());

        another.setData(DATA);
        assertNotNull("data is null.", another.getData());
        another.setData(null);
        assertNull("data not cleared.", another.getData());

        another.setCreatedOn(new Date());
        assertNotNull("created on is null.", another.getCreatedOn());
        another.setCreatedOn(null);
        assertNull("created on not cleared.", another.getCreatedOn());
    }

    @Test
    public void constructorData() {
        ClientLog another = new ClientLog(DATA);
        assertEquals("data not same.", DATA, another.getData());
        assertNull(another.getUserId());
        assertNotNull(another.getName());
        assertNotNull(another.getCreatedOn());
    }

    @Test
    public void constructorUserIdData() {
        ClientLog another = new ClientLog(USER_ID, DATA);
        assertEquals("data not same.", DATA, another.getData());
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertNotNull(another.getName());
        assertNotNull(another.getCreatedOn());
    }

    @Test
    public void copyConstructor() {
        ClientLog another = new ClientLog(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("UserId not same.", entity.getUserId(), another
                .getUserId());
        assertEquals("data not same.", entity.getData(), another.getData());
        assertEquals("created on not same.", entity.getCreatedOn(), another
                .getCreatedOn());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
