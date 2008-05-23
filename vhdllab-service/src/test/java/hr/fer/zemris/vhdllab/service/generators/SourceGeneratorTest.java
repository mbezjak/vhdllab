package hr.fer.zemris.vhdllab.service.generators;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeTrue;
import hr.fer.zemris.vhdllab.api.FileTypes;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.entities.Project;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;
import hr.fer.zemris.vhdllab.test.FileContentProvider;
import hr.fer.zemris.vhdllab.test.NameAndContent;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

/**
 * A test case for {@link SourceGenerator}.
 *
 * @author Miro Bezjak
 */
public class SourceGeneratorTest {

    /**
     * everything ok.
     */
    @Test
    public void execute() throws Exception {
        Project project = new Project("user.id", "project_name");
        String type = FileTypes.VHDL_SOURCE;
        List<NameAndContent> list = FileContentProvider.getContent(type);
        assumeTrue(list.size() >= 1);
        NameAndContent nc = list.get(0);
        File f = new File(project, nc.getName(), type, nc.getContent());
        VHDLGenerator gen = new SourceGenerator();
        VHDLGenerationResult result = gen.execute(f);
        assertTrue("result not successful.", result.isSuccessful());
        assertEquals("status not equal.", Integer.valueOf(0), result
                .getStatus());
        assertEquals("messages is not empty.", Collections.emptyList(), result
                .getMessages());
        assertEquals("content not equal.", f.getContent(), result.getVHDL());
    }

}
