package hr.fer.zemris.vhdllab.dao.impl;

import static org.junit.Assert.assertEquals;
import hr.fer.zemris.vhdllab.server.FileTypes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * A test case for {@link LibrariesConfParser}.
 * 
 * @author Miro Bezjak
 */
public class LibrariesConfParserTest {

	private static final File confFile;
	static {
		try {
			confFile = File.createTempFile("vhdllab", "lib-conf");
			confFile.deleteOnExit();
		} catch (IOException e) {
			throw new IllegalStateException("Could not create temp file", e);
		}
	}

	@BeforeClass
	public static void initTestCase() throws Exception {
		BufferedWriter writer = new BufferedWriter(new FileWriter(confFile,
				false));
		writer.append("<libraries>\n\t");
		writer.append("<library name=\"predefined\" extension=\"vhdl\" ");
		writer.append("mappedTo=\"").append(FileTypes.VHDL_SOURCE);
		writer.append("\" />\n\t");
		writer.append("<library name=\"preferences\" extension=\"txt\" ");
		writer.append("mappedTo=\"").append(FileTypes.PREFERENCES_GLOBAL);
		writer.append("\" />\n");
		writer.append("</libraries>\n");
		writer.close();
	}
	
	/**
	 * confFile is null
	 */
	@Test(expected = NullPointerException.class)
	public void parse() throws Exception {
		LibrariesConfParser.parse(null);
	}

	/**
	 * confFile is null
	 */
	@Test(expected = IllegalArgumentException.class)
	public void parse2() throws Exception {
		LibrariesConfParser.parse(confFile.getParentFile());
	}
	
	/**
	 * Test parse.
	 */
	@Test
	public void parse3() throws Exception {
		LibraryConf l;
		LibrariesConf expectedConf = new LibrariesConf();
		l = new LibraryConf("predefined", "vhdl", FileTypes.VHDL_SOURCE);
		expectedConf.addLibraryConf(l);
		l = new LibraryConf("preferences", "txt", FileTypes.PREFERENCES_GLOBAL);
		expectedConf.addLibraryConf(l);

		LibrariesConf conf = LibrariesConfParser.parse(confFile);
		assertEquals("configurations not equal.", expectedConf, conf);
	}

	@Ignore("must be tested by a user and this has already been tested")
	@Test
	public void asString() throws Exception {
		LibrariesConf conf = LibrariesConfParser.parse(confFile);
		System.out.println(conf);
	}

}
