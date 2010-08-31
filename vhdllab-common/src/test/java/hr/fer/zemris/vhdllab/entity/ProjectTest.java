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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.stub.FileStub;
import hr.fer.zemris.vhdllab.entity.stub.FileStub2;
import hr.fer.zemris.vhdllab.entity.stub.ProjectStub;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;

public class ProjectTest extends ValueObjectTestSupport {

    private Project entity;

    @Before
    public void initEntity() throws Exception {
        entity = new ProjectStub();
    }

    @Test
    public void basics() {
        Project another = new Project();
        assertNotNull("files is null.", another.getFiles());
        assertTrue("files not empty.", another.getFiles().isEmpty());

        another.setFiles(null);
        assertNull("files not cleared.", another.getFiles());
    }

    @Test
    public void constructorName() {
        Project another = new Project(NAME);
        assertEquals("name not same.", NAME, another.getName());
        assertNull("userId not null", another.getUserId());
        assertTrue("files not empty.", another.getFiles().isEmpty());
    }

    @Test
    public void constructorUserIdAndName() {
        Project another = new Project(USER_ID, NAME);
        assertEquals("userId not same.", USER_ID, another.getUserId());
        assertEquals("name not same.", NAME, another.getName());
        assertTrue("files not empty.", another.getFiles().isEmpty());
    }

    @Test
    public void copyConstructor() {
        Project another = new Project(entity);
        assertEquals("userId not same.", entity.getUserId(), another
                .getUserId());
        assertTrue("files not empty.", another.getFiles().isEmpty());
    }

    @Test
    public void setFiles() throws Exception {
        entity = new Project();
        assertTrue("files not empty.", entity.getFiles().isEmpty());
        entity.getFiles().add(new FileStub());
        assertFalse("files is empty.", entity.getFiles().isEmpty());

        entity = new Project();
        Set<File> files = new HashSet<File>();
        entity.setFiles(files);
        assertTrue("files not empty.", entity.getFiles().isEmpty());
        files.add(new FileStub());
        assertFalse("files is empty.", entity.getFiles().isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFileNullArgument() {
        entity.addFile(null);
    }

    /**
     * Add a file.
     */
    @Test
    public void addFile() throws Exception {
        Set<File> files = new HashSet<File>(entity.getFiles());
        File anotherFile = new FileStub2();
        entity.addFile(anotherFile);
        files.add(anotherFile);
        assertTrue("file not added.", entity.getFiles().contains(anotherFile));
        assertEquals("file size not same.", files.size(), entity.getFiles()
                .size());
        assertEquals("files not same.", files, entity.getFiles());
    }

    /**
     * Add a file that is already in that project.
     */
    @Test
    public void addFile2() throws Exception {
        Set<File> files = new HashSet<File>(entity.getFiles());
        File file = (File) CollectionUtils.get(entity.getFiles(), 0);
        entity.addFile(file);
        assertEquals("files not same.", files, entity.getFiles());
        assertEquals("files is changed.", 1, entity.getFiles().size());
    }

    /**
     * Add a file that is already in another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void addFile3() throws Exception {
        Project another = new Project(entity);
        File file = (File) CollectionUtils.get(entity.getFiles(), 0);
        another.addFile(file);
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFileNullArgument() {
        entity.addFile(null);
    }

    /**
     * Remove a file.
     */
    @Test
    public void removeFile() throws Exception {
        File file = (File) CollectionUtils.get(entity.getFiles(), 0);
        entity.removeFile(file);
        assertTrue("files not empty.", entity.getFiles().isEmpty());
        assertNull("project isn't set to null.", file.getProject());
    }

    /**
     * Remove a file that doesn't belong to any project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeFile2() throws Exception {
        File anotherFile = new FileStub2();
        entity.removeFile(anotherFile);
    }

    /**
     * Remove a file that belongs to another project.
     */
    @Test(expected = IllegalArgumentException.class)
    public void removeFile3() throws Exception {
        File file = (File) CollectionUtils.get(entity.getFiles(), 0);
        Project another = new Project(entity);
        another.removeFile(file);
    }

    @Test
    public void testToString() {
        toStringPrint(entity);
    }

}
