package hr.fer.zemris.vhdllab.service.hierarchy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

public class HierarchyTest extends ValueObjectTestSupport {

    private HierarchyNode root;
    private HierarchyNode left;
    private HierarchyNode right;
    private HierarchyNode rightDep;
    private Collection<HierarchyNode> collection;
    private Hierarchy hierarchy;

    @Before
    public void initObject() {
        root = new HierarchyNode(new File("root", null, null), null);
        right = new HierarchyNode(new File("right", null, null), root);
        left = new HierarchyNode(new File("left", null, null), root);
        rightDep = new HierarchyNode(new File("right-dep", null, null), right);
        collection = new ArrayList<HierarchyNode>(4);
        collection.add(root);
        collection.add(right);
        collection.add(left);
        collection.add(rightDep);
        hierarchy = new Hierarchy(new Project("userId", "project_name"),
                collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullProject() {
        new Hierarchy(null, collection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullCollection() {
        new Hierarchy(new Project(), null);
    }

    @Test
    public void constructor() {
        Project project = new Project("userId", "name");
        hierarchy = new Hierarchy(project, collection);
        assertEquals(project, hierarchy.getProject());
        assertNotSame(project, hierarchy.getProject());
        assertNull(hierarchy.getProject().getFiles());

        assertNull(hierarchy.getTopLevelNodes());
        assertNull(hierarchy.getBottomLevelNodes());
        assertEquals(4, hierarchy.getAllNodes().size());
    }

    @Test
    public void getProject() {
        Project project = hierarchy.getProject();
        project.setName("another_name");
        assertEquals("another_name", hierarchy.getProject().getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getNodeNullArgument() {
        hierarchy.getNode(null);
    }

    @Test
    public void getNode() {
        assertEquals(root, hierarchy.getNode(root.getFile()));
        assertEquals(left, hierarchy.getNode(left.getFile()));
        assertEquals(right, hierarchy.getNode(right.getFile()));
        assertEquals(rightDep, hierarchy.getNode(rightDep.getFile()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getAllNodes() {
        hierarchy.getAllNodes().add(new HierarchyNode(new File(), null));
    }

    @Test
    public void afterSerialization() {
        Hierarchy clone = (Hierarchy) SerializationUtils.clone(hierarchy);
        assertNotNull(clone.getTopLevelNodes());
        assertNotNull(clone.getBottomLevelNodes());

        Set<HierarchyNode> topLevel = new HashSet<HierarchyNode>(1);
        topLevel.add(root);
        assertEquals(topLevel, clone.getTopLevelNodes());

        Set<HierarchyNode> bottomLevel = new HashSet<HierarchyNode>(2);
        bottomLevel.add(rightDep);
        bottomLevel.add(left);
        assertEquals(bottomLevel, clone.getBottomLevelNodes());
    }

    @Test
    public void getTopLevelNodes() {
        Hierarchy clone = (Hierarchy) SerializationUtils.clone(hierarchy);
        Set<HierarchyNode> topLevelNodes = clone.getTopLevelNodes();
        topLevelNodes.add(new HierarchyNode(new File(), null));
        assertEquals(topLevelNodes.size(), clone.getTopLevelNodes().size());
    }

    @Test
    public void getBottomLevelNodes() {
        Hierarchy clone = (Hierarchy) SerializationUtils.clone(hierarchy);
        Set<HierarchyNode> bottomLevelNodes = clone.getBottomLevelNodes();
        bottomLevelNodes.add(new HierarchyNode(new File(), null));
        assertEquals(bottomLevelNodes.size(), clone.getBottomLevelNodes()
                .size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getDependenciesForNullArgument() {
        hierarchy.getDependenciesFor(null);
    }

    @Test
    public void getDependenciesFor() {
        assertEquals(2, hierarchy.getDependenciesFor(root).size());
        assertEquals(1, hierarchy.getDependenciesFor(right).size());
        assertEquals(0, hierarchy.getDependenciesFor(left).size());
        assertEquals(0, hierarchy.getDependenciesFor(rightDep).size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void getParentsForNullArgument() {
        hierarchy.getParentsFor(null);
    }

    @Test
    public void getParentsFor() {
        assertEquals(0, hierarchy.getParentsFor(root).size());
        assertEquals(1, hierarchy.getParentsFor(right).size());
        assertEquals(1, hierarchy.getParentsFor(left).size());
        assertEquals(1, hierarchy.getParentsFor(rightDep).size());
    }

    @Test
    public void iteratorFlatHierarchy() {
        Iterator<HierarchyNode> iterator = hierarchy.iteratorFlatHierarchy();
        assertEquals(root, iterator.next());
        assertEquals(right, iterator.next());
        assertEquals(left, iterator.next());
        assertEquals(rightDep, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorXUsesYHierarchy() {
        Hierarchy clone = (Hierarchy) SerializationUtils.clone(hierarchy);
        Iterator<HierarchyNode> iterator = clone.iteratorXUsesYHierarchy(null);
        assertEquals(root, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsesYHierarchy(root);
        assertEquals(left, iterator.next());
        assertEquals(right, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsesYHierarchy(right);
        assertEquals(rightDep, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsesYHierarchy(left);
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsesYHierarchy(rightDep);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void iteratorXUsedByYHierarchy() {
        Hierarchy clone = (Hierarchy) SerializationUtils.clone(hierarchy);
        Iterator<HierarchyNode> iterator = clone
                .iteratorXUsedByYHierarchy(null);
        assertEquals(rightDep, iterator.next());
        assertEquals(left, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsedByYHierarchy(rightDep);
        assertEquals(right, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsedByYHierarchy(left);
        assertEquals(root, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsedByYHierarchy(right);
        assertEquals(root, iterator.next());
        assertFalse(iterator.hasNext());

        iterator = clone.iteratorXUsedByYHierarchy(root);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void hashCodeAndEquals() throws Exception {
        basicEqualsTest(hierarchy);

        Hierarchy another = new Hierarchy(hierarchy.getProject(), collection);
        assertEqualsAndHashCode(hierarchy, another);

        another = new Hierarchy(new Project("newUserId", "another_name"),
                collection);
        assertNotEqualsAndHashCode(hierarchy, another);
    }

    @Test
    public void testToString() {
        toStringPrint(hierarchy);
        assertEquals(
                "Hierarchy for user=userId, project=project_name {\nroot [left,right]\nright [right-dep]\nleft []\nright-dep []\n}",
                hierarchy.toString());
    }

}
