package hr.fer.zemris.vhdllab.server.conf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hr.fer.zemris.vhdllab.api.FileTypes;

import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link ServerConfParser}.
 *
 * @author Miro Bezjak
 */
public class ServerConfParserTest {

    /**
     * Test getConfiguration.
     */
    @Test
    public void getConfiguration() throws Exception {
        FileTypeMapping m;
        ServerConf expectedConf = new ServerConf();
        Properties p = new Properties();
        p.setProperty("compiler.executable", "value not part of equals");
        p.setProperty("compiler.tmpDir", "value not part of equals");
        p.setProperty("simulator.executable", "value not part of equals");
        p.setProperty("simulator.tmpDir", "value not part of equals");
        expectedConf.setProperties(p);
        m = new FileTypeMapping(FileTypes.VHDL_SOURCE);
        m.addFunctionality(FunctionalityType.GENERATOR.toString(),
                "hr.fer.zemris.vhdllab.service.generators.SourceGenerator");
        m.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                "hr.fer.zemris.vhdllab.service.extractors.SourceExtractor");
        m.addFunctionality(FunctionalityType.DEPENDENCY.toString(),
                "hr.fer.zemris.vhdllab.service.dependencies.SourceDependency");
        m.addFunctionality(FunctionalityType.COMPILER.toString(),
                "hr.fer.zemris.vhdllab.service.compiler.GhdlCompiler");
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.VHDL_TESTBENCH);
        m.addFunctionality(FunctionalityType.GENERATOR.toString(),
                "hr.fer.zemris.vhdllab.service.generators.TestbenchVHDLGenerator");
        m.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                "hr.fer.zemris.vhdllab.service.extractors.TestbenchExtractor");
        m.addFunctionality(FunctionalityType.DEPENDENCY.toString(),
                "hr.fer.zemris.vhdllab.service.dependencies.TestbenchDependencyExtractor");
        m.addFunctionality(FunctionalityType.COMPILER.toString(),
                "hr.fer.zemris.vhdllab.service.compiler.GhdlCompiler");
        m.addFunctionality(FunctionalityType.SIMULATOR.toString(),
                "hr.fer.zemris.vhdllab.service.simulator.GHDLSimulator");
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.VHDL_SCHEMA);
        m.addFunctionality(FunctionalityType.GENERATOR.toString(),
                "hr.fer.zemris.vhdllab.service.generators.SchemaVHDLGenerator");
        m.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                "hr.fer.zemris.vhdllab.service.extractors.SchemaCircuitInterfaceExtractor");
        m.addFunctionality(FunctionalityType.DEPENDENCY.toString(),
                "hr.fer.zemris.vhdllab.service.dependencies.SchemaDependency");
        m.addFunctionality(FunctionalityType.COMPILER.toString(),
                "hr.fer.zemris.vhdllab.service.compiler.GhdlCompiler");
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.VHDL_AUTOMATON);
        m.addFunctionality(FunctionalityType.GENERATOR.toString(),
                "hr.fer.zemris.vhdllab.service.generators.automat.AutVHDLGenerator");
        m.addFunctionality(FunctionalityType.EXTRACTOR.toString(),
                "hr.fer.zemris.vhdllab.service.extractors.AutCircuitInterfaceExtractor");
        m.addFunctionality(FunctionalityType.DEPENDENCY.toString(),
                "hr.fer.zemris.vhdllab.service.dependencies.AUTDependancy");
        m.addFunctionality(FunctionalityType.COMPILER.toString(),
                "hr.fer.zemris.vhdllab.service.compiler.GhdlCompiler");
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.VHDL_SIMULATION);
        m.addFunctionality(FunctionalityType.DEPENDENCY.toString(),
                "hr.fer.zemris.vhdllab.service.dependencies.SimulationDependancy");
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.VHDL_PREDEFINED);
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.PREFERENCES_USER);
        expectedConf.addFileTypeMapping(m);
        m = new FileTypeMapping(FileTypes.ERROR);
        expectedConf.addFileTypeMapping(m);

        ServerConf conf = ServerConfParser.getConfiguration();
        System.out.println(expectedConf);
        System.out.println(conf);
        assertEquals("configurations not equal.", expectedConf, conf);
    }

    /**
     * File type is case insensitive
     */
    @Test
    public void getFileTypeMapping() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        FileTypeMapping mapping = conf.getFileTypeMapping(FileTypes.VHDL_SOURCE
                .toUpperCase());
        assertNotNull("file type is case sensitive.", mapping);
    }

    @Ignore("must be tested by a user and this has already been tested")
    @Test
    public void asString() throws Exception {
        ServerConf conf = ServerConfParser.getConfiguration();
        System.out.println(conf);
    }

}
