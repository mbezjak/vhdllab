package hr.fer.zemris.vhdllab.service.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.service.hierarchy.Hierarchy;
import hr.fer.zemris.vhdllab.service.hierarchy.HierarchyNode;
import hr.fer.zemris.vhdllab.test.ValueObjectTestSupport;

import java.util.ArrayList;

import org.apache.commons.lang.SerializationUtils;
import org.junit.Before;
import org.junit.Test;

public class FileReportTest extends ValueObjectTestSupport {

    private FileReport report;
    private Hierarchy hierarchy;

    @Before
    public void initObject() {
        hierarchy = new Hierarchy(new Project(), new ArrayList<HierarchyNode>());
        report = new FileReport(new File(), hierarchy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullFile() {
        new FileReport(null, hierarchy);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructorNullHierarchy() {
        new FileReport(new File(), null);
    }

    @Test
    public void constructor() {
        File file = new File("name", null, "data");
        report = new FileReport(file, hierarchy);
        assertEquals(hierarchy, report.getHierarchy());
        assertEquals(file, report.getFile());
        assertNotSame(file, report.getFile());
        assertNotNull(report.getFile().getData());
        assertNull(report.getFile().getProject());
    }

    @Test
    public void getFile() {
        File file = report.getFile();
        file.setName("name");
        assertEquals("name", report.getFile().getName());
    }

    @Test
    public void afterSerialization() {
        assertNull(report.getFile().getProject());
        FileReport clone = (FileReport) SerializationUtils.clone(report);
        assertNotNull(clone.getFile().getProject());
    }

    @Test
    public void testToString() {
        toStringPrint(report);
    }

}
