package hr.fer.zemris.vhdllab.service.dependencies;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * A test case for {@link PredefinedDependency}.
 *
 * @author Miro Bezjak
 */
public class PredefinedDependencyTest {

    /**
     * everything ok.
     */
    @Test
    public void execute() throws Exception {
        Project project = new Project("user.id", "project_name");
        String type = FileTypes.VHDL_PREDEFINED;
        List<NameAndContent> list = FileContentProvider.getContent(type);
        assumeTrue(list.size() >= 1);
        NameAndContent nc = list.get(0);
        File file = new File(project, nc.getName(), type, nc.getContent());
        DependencyExtractor dep = new PredefinedDependency();
        assertEquals("predefined file has dependencies.", Collections
                .emptyList(), dep.execute(file));
    }

}
