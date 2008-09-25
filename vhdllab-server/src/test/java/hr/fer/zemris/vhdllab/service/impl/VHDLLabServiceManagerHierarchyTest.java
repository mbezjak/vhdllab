package hr.fer.zemris.vhdllab.service.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.api.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.api.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.dao.impl.EntityManagerUtil;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.ProjectManager;
import hr.fer.zemris.vhdllab.service.ServiceContainer;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * A test case for {@link VHDLLabServiceManager}'s extractHierarchy method.
 * 
 * @author Miro Bezjak
 */
public class VHDLLabServiceManagerHierarchyTest {

    private static final Caseless USER_ID = new Caseless("user.id");

    private static ServiceContainer container;
    private static ProjectManager projectMan;
    private static ServiceManager man;
    private static Project project;

    @BeforeClass
    public static void initOnce() throws Exception {
        container = ServiceContainer.instance();
        projectMan = container.getProjectManager();
        man = container.getServiceManager();
        EntityManagerUtil.createEntityManagerFactory();
    }

    @Before
    public void initEachTest() throws Exception {
        EntityManagerUtil.currentEntityManager();
        project = new Project(USER_ID, new Caseless("project_name"));
        projectMan.save(project);
    }

    @After
    public void destroyEachTest() throws Exception {
        projectMan.delete(project.getId());
        EntityManagerUtil.closeEntityManager();
    }

    /**
     * File is null.
     */
    @Test(expected = NullPointerException.class)
    public void extractHierarchyNull() throws Exception {
        man.extractHierarchy(null);
    }

    /**
     * Empty hierarchy.
     */
    @Test
    public void extractHierarchyEmpty() throws Exception {
        Hierarchy hierarchy = man.extractHierarchy(project);
        Set<HierarchyNode> nodes = Collections.emptySet();
        Hierarchy expected = new Hierarchy(project.getName(), nodes);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * One predefined file in a hierarchy.
     */
    @Test
    public void extractHierarchyTopLevel() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(1);
        Caseless name = new Caseless("comp_and");
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        nodes.add(new HierarchyNode(name, type, null));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * One file using another in a hierarchy.
     */
    @Test
    public void extractHierarchyComplex() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(2);
        Caseless name = new Caseless("comp_or"); // uses vl_or - predefined file
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, node));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * One file using another and one file is standalone in a hierarchy.
     */
    @Test
    public void extractHierarchyComplex2() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(2);
        Caseless name = new Caseless("comp_or");
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, node));

        name = new Caseless("comp_and"); // standalone component
        type = FileType.SOURCE;
        prepairFile(name, type);
        nodes.add(new HierarchyNode(name, type, null));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * 2-2 hierarchy (n-m => n uses m components).
     */
    @Test
    public void extractHierarchyComplex3() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(2);
        Caseless name = new Caseless("comp_or");
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, node));

        name = new Caseless("complex_source");
        type = FileType.SOURCE;
        prepairFile(name, type);
        node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("comp_and");
        type = FileType.SOURCE;
        prepairFile(name, type);
        nodes.add(new HierarchyNode(name, type, node));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * 1-1, 1-1 hierarchy (n-m => n uses m components). Both top level component
     * uses the same simple component.
     */
    @Test
    public void extractHierarchyComplex4() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(2);
        Caseless name = new Caseless("comp_or");
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, node));

        name = new Caseless("comp_or2");
        type = FileType.SOURCE;
        prepairFile(name, type);
        node = new HierarchyNode(name, type, null);
        nodes.add(node);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, node));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * 1-2-2 hierarchy (n-m => n uses m components).
     */
    @Test
    public void extractHierarchyComplex5() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(2);
        Caseless name = new Caseless("ultra_complex_source");
        FileType type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode node = new HierarchyNode(name, type, null);
        nodes.add(node);

        name = new Caseless("comp_or");
        type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode n1 = new HierarchyNode(name, type, node);
        nodes.add(n1);
        name = new Caseless("vl_or");
        type = FileType.PREDEFINED;
        nodes.add(new HierarchyNode(name, type, n1));

        name = new Caseless("complex_source");
        type = FileType.SOURCE;
        prepairFile(name, type);
        HierarchyNode n2 = new HierarchyNode(name, type, node);
        nodes.add(n2);
        name = new Caseless("comp_and");
        type = FileType.SOURCE;
        prepairFile(name, type);
        nodes.add(new HierarchyNode(name, type, n2));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * One dependency can't be resolved. Hierarchy is constructed without
     * affected dependency.
     */
    @Test
    public void extractHierarchyFaultyDependency() throws Exception {
        Set<HierarchyNode> nodes = new HashSet<HierarchyNode>(1);
        Caseless name = new Caseless("comp_and_tb");
        FileType type = FileType.TESTBENCH;
        prepairFaultyFile(name, type);
        nodes.add(new HierarchyNode(name, type, null));
        Hierarchy expected = new Hierarchy(project.getName(), nodes);

        Hierarchy hierarchy = man.extractHierarchy(project);
        assertEquals("hierarchy names not same.", expected, hierarchy);
        Set<HierarchyNode> actualNodes = getNodes(hierarchy);
        assertEquals("hierarchy nodes not same.", nodes, actualNodes);
        checkNodeDependencies(nodes, actualNodes);
    }

    /**
     * Persists specified file but without a content thereby creating faulty
     * file.
     */
    private void prepairFaultyFile(Caseless name, FileType type)
            throws ServiceException {
        File f = new File(type, name, "faulty content");
        project.addFile(f);
        container.getProjectManager().save(project);
    }

    /**
     * Persists specified file.
     */
    private static void prepairFile(Caseless name, FileType type)
            throws ServiceException {
        List<NameAndContent> contents = FileContentProvider.getContent(type);
        for (NameAndContent nc : contents) {
            if (nc.getName().equals(name)) {
                File f = new File(type, nc.getName(), nc.getContent());
                project.addFile(f);
                container.getProjectManager().save(project);
            }
        }
    }

    /**
     * Returns all hierarchy nodes defined in specified hierarchy.
     */
    @SuppressWarnings("unchecked")
    private Set<HierarchyNode> getNodes(Hierarchy h) throws Exception {
        Field field = h.getClass().getDeclaredField("nodes");
        field.setAccessible(true);
        Map<String, HierarchyNode> nodes = (Map<String, HierarchyNode>) field
                .get(h);
        return new HashSet(nodes.values());
    }

    /**
     * Checks that two nodes contains the same dependencies.
     */
    private void checkNodeDependencies(Set<HierarchyNode> expected,
            Set<HierarchyNode> actual) {
        Iterator<HierarchyNode> expectedIterator = expected.iterator();
        while (expectedIterator.hasNext()) {
            HierarchyNode expectedNode = expectedIterator.next();
            Iterator<HierarchyNode> actualIterator = actual.iterator();
            while (actualIterator.hasNext()) {
                HierarchyNode actualNode = actualIterator.next();
                if (expectedNode.equals(actualNode)) {
                    assertEquals("dependencies not same.", expectedNode
                            .getDependencies(), actualNode.getDependencies());
                }
            }
        }
    }

}
