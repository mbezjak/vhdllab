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
package hr.fer.zemris.vhdllab.service.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

public class ProjectMetadataTest extends ValueObjectTestSupport {

    private Hierarchy hierarchy;
    private Set<File> files;
    private ProjectMetadata metadata;

    @SuppressWarnings("unchecked")
    @Before
    public void initObject() {
        Project project = new Project("userId", "name");
        hierarchy = new Hierarchy(project, Collections.EMPTY_LIST);

        files = new HashSet<File>(2);
        File file = new File("file1", null, "data");
        file.setProject(project);
        files.add(file);
        file = new File("file2", null, "data2");
        file.setProject(project);
        files.add(file);

        metadata = new ProjectMetadata(hierarchy, files);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProject() {
        new ProjectMetadata(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullHierarchy() {
        new ProjectMetadata(null, files);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullFiles() {
        new ProjectMetadata(hierarchy, null);
    }

    @Test
    public void constructorProject() {
        Project project = new Project("userId", "project_name");
        metadata = new ProjectMetadata(project);
        assertEquals(project, metadata.getHierarchy().getProject());
        assertEquals(project, metadata.getProject());
        assertEquals(0, metadata.getHierarchy().getFileCount());
        assertTrue(metadata.getFiles().isEmpty());
    }

    @Test
    public void constructorHierarchyFiles() {
        metadata = new ProjectMetadata(hierarchy, files);
        assertEquals(hierarchy, metadata.getHierarchy());

        assertEquals(2, metadata.getFiles().size());
        files.add(new File());
        assertEquals(2, metadata.getFiles().size());

        File file = (File) CollectionUtils.get(metadata.getFiles(), 0);
        assertNull(file.getProject());

        file = (File) CollectionUtils.get(files, 0);
        file.setName("new_name");
        File another = (File) CollectionUtils.get(metadata.getFiles(), 0);
        assertFalse(file.equals(another));
    }

    @Test
    public void getProject() {
        Project project = metadata.getProject();
        project.setName("new_name");
        assertFalse(project.equals(metadata.getProject()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getFiles() {
        metadata.getFiles().add(new File());
    }

    @Test
    public void getFiles2() {
        File file = (File) CollectionUtils.get(metadata.getFiles(), 0);
        file.setName("new_name");
        File another = (File) CollectionUtils.get(metadata.getFiles(), 0);
        assertTrue(file.equals(another));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addFile() {
        metadata.addFile(null);
    }

    @Test
    public void addFile2() {
        File file = new File("file_name", null, "data");
        file.setProject(hierarchy.getProject());
        metadata.addFile(file);
        assertEquals(3, metadata.getFiles().size());
        assertTrue(metadata.getFiles().contains(file));

        File another = new File(file);
        another.setData("new_data");
        metadata.addFile(another);
        assertEquals(3, metadata.getFiles().size());
        File found = (File) CollectionUtils.find(metadata.getFiles(),
                new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        File f = (File) object;
                        return f.getName().equals("file_name");
                    }
                });
        assertEquals("new_data", found.getData());
    }

    @Test
    public void addFile3() {
        File file = new File("file_name", null, "data");
        metadata.addFile(file);
        File found = (File) CollectionUtils.find(metadata.getFiles(),
                new Predicate() {
                    @Override
                    public boolean evaluate(Object object) {
                        File f = (File) object;
                        return f.getName().equals("file_name");
                    }
                });
        assertEquals(hierarchy.getProject(), found.getProject());
    }

    @Test(expected = IllegalArgumentException.class)
    public void removeFile() {
        metadata.removeFile(null);
    }

    @Test
    public void removeFile2() {
        File file = (File) CollectionUtils.get(metadata.getFiles(), 0);
        metadata.removeFile(file);
        assertEquals(1, metadata.getFiles().size());
        assertFalse(metadata.getFiles().contains(file));
    }

    @Test(expected = IllegalArgumentException.class)
    public void setHierarchy() {
        metadata.setHierarchy(null);
    }

    @Test
    public void afterSerialization() {
        metadata = (ProjectMetadata) SerializationUtils.clone(metadata);
        for (File f : metadata.getFiles()) {
            assertEquals(hierarchy.getProject(), f.getProject());
        }
        File file = (File) CollectionUtils.get(files, 0);
        assertTrue(
                "change in hashCode of a file (added project reference) isn't reflected to the set containing it.",
                metadata.getFiles().contains(file));
    }

    @Test
    public void testToString() {
        toStringPrint(metadata);
    }

}
