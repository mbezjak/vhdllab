package hr.fer.zemris.vhdllab.server.conf;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import hr.fer.zemris.vhdllab.api.FileTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link ServerConfParser}.
 * 
 * @author Miro Bezjak
 */
public class LibrariesConfParserTest {

	private static final File confFile;
	static {
		try {
			confFile = File.createTempFile("vhdllab", "server-conf");
			confFile.deleteOnExit();
		} catch (IOException e) {
			throw new IllegalStateException("Could not create temp file", e);
		}
	}

	@BeforeClass
	public static void initTestCase() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(confFile,
				false));
		writer.append("<server>\n\t");
		writer.append("<fileTypeMapping type=\"").append(FileTypes.VHDL_SOURCE);
		writer.append("\" />\n\t");
		writer.append("<fileTypeMapping type=\"").append(FileTypes.VHDL_SCHEMA);
		writer.append("\" />\n");
		writer.append("</server>\n");
		writer.close();
	}

	/**
	 * Test getConfiguration.
	 */
	@Test
	public void getConfiguration() throws Exception {
		FileTypeMapping m;
		ServerConf expectedConf = new ServerConf();
		m = new FileTypeMapping(FileTypes.VHDL_SOURCE);
		expectedConf.addFileTypeMapping(m);
		m = new FileTypeMapping(FileTypes.VHDL_TESTBENCH);
		expectedConf.addFileTypeMapping(m);
		m = new FileTypeMapping(FileTypes.VHDL_SCHEMA);
		expectedConf.addFileTypeMapping(m);
		m = new FileTypeMapping(FileTypes.VHDL_AUTOMATON);
		expectedConf.addFileTypeMapping(m);
		m = new FileTypeMapping(FileTypes.PREFERENCES_USER);
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
