package hr.fer.zemris.vhdllab.service.filetype.testbench;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.Testbench;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.TestbenchParser;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.util.HashSet;
import java.util.Set;

public class TestbenchDependencyExtractor implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        Testbench tb = null;

        String source = file.getData();
        try {
            tb = TestbenchParser.parseXml(source);
        } catch (UniformTestbenchParserException e) {
            throw new DependencyExtractionException(e);
        }

        Set<Caseless> dependencies = new HashSet<Caseless>(1);
        dependencies.add(new Caseless(tb.getSourceName()));

        return dependencies;
    }

}
