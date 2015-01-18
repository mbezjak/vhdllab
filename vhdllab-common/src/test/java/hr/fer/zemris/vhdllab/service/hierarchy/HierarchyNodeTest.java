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
package hr.fer.zemris.vhdllab.service.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import org.junit.Before;
import org.junit.Test;

public class HierarchyNodeTest extends ValueObjectTestSupport {

    private HierarchyNode root;

    @Before
    public void initObject() {
        root = new HierarchyNode(
                new File("file_name", FileType.SOURCE, "data"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullFile() {
        new HierarchyNode(null, root);
    }

    @Test
    public void constructor() {
        File file = new File("another_file", null, "data");
        root = new HierarchyNode(file, null);
        assertEquals(file, root.getFile());
        assertNotSame(file, root.getFile());
        assertNull(root.getFile().getData());
        assertNull(root.getFile().getProject());

        assertTrue(root.getDependencies().isEmpty());
        assertNull(root.getParent());
    }

    @Test
    public void constructor2() {
        File file = new File("another_file", null, null);
        HierarchyNode node = new HierarchyNode(file, root);
        assertEquals(root, node.getParent());
        assertTrue(root.getDependencies().contains(file));
    }

    @Test
    public void hasDependencies() {
        assertFalse(root.hasDependencies());
        root.addDependency(new HierarchyNode(new File(), null));
        assertTrue(root.hasDependencies());
    }

    @Test(expected = NullPointerException.class)
    public void addDependencyNullArgument() {
        root.addDependency(null);
    }

    @Test
    public void addDependencyDuplicate() {
        HierarchyNode node = new HierarchyNode(new File(), null);
        root.addDependency(node);
        root.addDependency(node);

        assertEquals(1, root.getDependencies().size());
    }

    @Test
    public void addDependencyDuplicate2() {
        HierarchyNode node = new HierarchyNode(new File(), null);
        root.addDependency(node);
        root.addDependency(new HierarchyNode(
                new File("middle_file", null, null), null));
        root.addDependency(node);

        assertEquals(2, root.getDependencies().size());
    }

    @Test
    public void addDependencySelf() throws Exception {
        root.addDependency(root);

        assertTrue(root.getDependencies().isEmpty());
    }

    @Test
    public void addDependencyCyclic() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);
        node.addDependency(new HierarchyNode(file, null));

        assertEquals(1, root.getDependencies().size());
        assertTrue(node.getDependencies().isEmpty());
    }

    @Test
    public void addDependencyCyclic2() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);

        HierarchyNode noodDeepInHierarchy = new HierarchyNode(new File(), null);
        node.addDependency(noodDeepInHierarchy);
        noodDeepInHierarchy.addDependency(new HierarchyNode(root.getFile(),
                null));

        assertEquals(1, root.getDependencies().size());
        assertEquals(1, node.getDependencies().size());
    }

    @Test
    public void addDependency() throws Exception {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);
        assertEquals(root, node.getParent());
        assertTrue(root.getDependencies().contains(file));
    }

    /**
     * Returns direct reference.
     */
    @Test
    public void getFile() {
        File file = root.getFile();
        file.setName("name");
        assertEquals("name", root.getFile().getName());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getDependencies() {
        root.getDependencies().add(new File());
    }

    @Test(expected = IllegalArgumentException.class)
    public void containsDependency() {
        root.containsDependency(null);
    }

    @Test
    public void containsDependency2() {
        File file = new File("a_file_name", null, null);
        HierarchyNode node = new HierarchyNode(file, null);
        root.addDependency(node);

        assertTrue(root.containsDependency(file));
        assertFalse(root.containsDependency(new File()));

        File clone = new File(file, new Project("userId", "a_project_name"));
        assertTrue(root.containsDependency(clone));
    }

    @Test
    public void hashCodeAndEquals() throws Exception {
        basicEqualsTest(root);

        HierarchyNode newRoot = new HierarchyNode(new File(), null);
        HierarchyNode another = new HierarchyNode(root.getFile(), newRoot);
        assertEqualsAndHashCode(root, another);

        another = new HierarchyNode(new File("another_file", null, null),
                newRoot);
        assertNotEqualsAndHashCode(root, another);
    }

    @Test
    public void testToString() {
        toStringPrint(root);
        assertEquals("file_name []", root.toString());

        new HierarchyNode(new File("another_file", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [another_file]", root.toString());

        new HierarchyNode(new File("new_file", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [another_file,new_file]", root.toString());

        new HierarchyNode(new File(), root);
        toStringPrint(root);
        assertEquals("file_name [another_file,new_file,null]", root.toString());
    }

    @Test
    public void testToString2() {
        toStringPrint(root);

        new HierarchyNode(new File("left", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [left]", root.toString());

        new HierarchyNode(new File("right", null, null), root);
        toStringPrint(root);
        assertEquals("file_name [left,right]", root.toString());
    }

}
