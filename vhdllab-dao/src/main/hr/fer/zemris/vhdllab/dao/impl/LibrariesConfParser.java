package hr.fer.zemris.vhdllab.dao.impl;

import java.io.File;
import java.io.IOException;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

/**
 * A parser for libraries configuration file (libraries.xml).
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public class LibrariesConfParser {

	/**
	 * Don't let anyone instantiate this class.
	 */
	private LibrariesConfParser() {
	}

	/**
	 * Parsers a configuration file.
	 * 
	 * @param confFile
	 *            a configuration file to parse
	 * @return a parsed configuration
	 * @throws NullPointerException
	 *             if <code>confFile</code> is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if <code>confFile</code> is not a file
	 * @throws IOException
	 *             if an input/output error occurs
	 * @throws SAXException
	 *             if a parsing exception occurs
	 */
	public static LibrariesConf parse(File confFile) throws IOException,
			SAXException {
		if (confFile == null) {
			throw new NullPointerException("Configuration file cant be null");
		}
		if (!confFile.isFile()) {
			throw new IllegalArgumentException(
					"Configuration must be a file but was: " + confFile);
		}
		Digester digester = new Digester();
		digester.addObjectCreate("libraries", LibrariesConf.class);
		digester.addObjectCreate("libraries/library", LibraryConf.class);
		digester.addSetProperties("libraries/library", "name", "name");
		digester.addSetProperties("libraries/library", "extension", "extension");
		digester.addSetProperties("libraries/library", "mappedTo", "fileType");
		digester.addSetNext("libraries/library", "addLibraryConf");

		return (LibrariesConf) digester.parse(confFile);
	}

}
