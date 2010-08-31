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
import static hr.fer.zemris.vhdllab.entity.stub.PreferencesFileStub.DATA;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.stub.PreferencesFileStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class PreferencesFileTest extends ValueObjectTestSupport {

    private PreferencesFile entity;

    @Before
    public void initEntity() throws Exception {
        entity = new PreferencesFileStub();
    }

    @Test
    public void basics() {
        PreferencesFile another = new PreferencesFile();
        assertNull("data is set.", another.getData());

        another.setData(DATA);
        assertNotNull("data is null.", another.getData());
        another.setData(null);
        assertNull("data not cleared.", another.getData());
    }

    @Test
    public void constructorNameData() {
        PreferencesFile another = new PreferencesFile(NAME, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertNull(another.getUserId());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void constructorUserIdNameData() {
        PreferencesFile another = new PreferencesFile(USER_ID, NAME, DATA);
        assertEquals("name not same.", NAME, another.getName());
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("data not same.", DATA, another.getData());
    }

    @Test
    public void copyConstructor() {
        PreferencesFile another = new PreferencesFile(entity);
        assertEquals("name not same.", entity.getName(), another.getName());
        assertEquals("UserId not same.", entity.getUserId(), another
                .getUserId());
        assertEquals("data not same.", entity.getData(), another.getData());
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
