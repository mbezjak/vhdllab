package hr.fer.zemris.vhdllab.server.conf;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.digester.Digester;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * A parser for server configuration file (server.xml).
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class ServerConfParser {

	/**
	 * A logger instance.
	 */
	private static final Logger log = Logger.getLogger(ServerConfParser.class);

	/**
	 * Cached server configuration.
	 */
	private static ServerConf CONFIGURATION;
	static {
		ClassLoader cl = ServerConfParser.class.getClassLoader();
		InputStream is = cl.getResourceAsStream("server.xml");
		try {
			CONFIGURATION = parse(is);
			if(log.isDebugEnabled()) {
				log.debug(CONFIGURATION);
			}
		} catch (Exception e) {
			CONFIGURATION = null;
			log.fatal("Could not parse server configuration", e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					log.warn("Failed to close server conf file.", e);
				}
			}
		}
	}

	/**
	 * Don't let anyone instantiate this class.
	 */
	private ServerConfParser() {
	}

	/**
	 * Returns a server configuration or <code>null</code> if any error
	 * occurs.
	 *
	 * @return a server configuration
	 */
	public static ServerConf getConfiguration() {
		return CONFIGURATION;
	}

	/**
	 * Parsers a configuration file.
	 *
	 * @param is
	 *            an input stream to a configuration file
	 * @return a parsed configuration
	 * @throws NullPointerException
	 *             if <code>is</code> is <code>null</code>
	 * @throws IOException
	 *             if an input/output error occurs
	 * @throws SAXException
	 *             if a parsing exception occurs
	 */
	private static ServerConf parse(InputStream is) throws IOException,
			SAXException {
		Digester digester = new Digester();
		digester.addObjectCreate("server", ServerConf.class);
		digester.addObjectCreate("server/fileTypeMapping",
				FileTypeMapping.class);
		digester.addSetProperties("server/fileTypeMapping", "type", "type");
		digester.addSetProperties("server/fileTypeMapping/generator", "name", "generator");
		digester.addSetProperties("server/fileTypeMapping/extractor", "name", "extractor");
		digester.addSetNext("server/fileTypeMapping", "addFileTypeMapping");

		return (ServerConf) digester.parse(is);
	}

}
