package hr.fer.zemris.vhdllab.dao.impl;

import hr.fer.zemris.vhdllab.dao.DAOException;
import hr.fer.zemris.vhdllab.dao.LibraryDAO;
import hr.fer.zemris.vhdllab.entities.Library;
import hr.fer.zemris.vhdllab.entities.LibraryFile;
import hr.fer.zemris.vhdllab.server.api.StatusCodes;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

/**
 * This is a default implementation of {@link LibraryDAO}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class LibraryDAOImpl implements LibraryDAO {
	/*
	 * NOTE: Implementation uses java.io.File. Not
	 * hr.fer.zemris.vhdllab.entities.File!
	 */

	/**
	 * Empty configuration file set when an error occurs when loading or parsing
	 * libraries configuration file.
	 */
	private static final LibrariesConf ERROR_LIB_CONF = new LibrariesConf();

	/**
	 * A name of a libraries configuration file.
	 */
	private static final String LIB_CONF_NAME = "libraries.xml";

	/**
	 * A logger for this class.
	 */
	private static final Logger log = Logger.getLogger(LibraryDAOImpl.class);

	private final File basedir;

	private LibrariesConf conf;

	public LibraryDAOImpl(File dir) {
		this.basedir = dir;
		if (basedir == null) {
			throw new NullPointerException("Basedir cant be null");
		}
		if (!basedir.isDirectory()) {
			throw new IllegalArgumentException(
					"Basedir must be a directory but was a file: " + basedir);
		}
		if (!basedir.exists()) {
			throw new IllegalArgumentException("Basedir doesn't exist: "
					+ basedir);
		}

		File confFile = makeConfFile();
		if (!confFile.exists()) {
			log.error("Libraries configuration file not present: " + confFile);
			conf = ERROR_LIB_CONF;
		} else {
			try {
				conf = LibrariesConfParser.parse(confFile);
			} catch (IOException e) {
				log.error("Error loading libraries configuration file: "
						+ confFile, e);
				conf = ERROR_LIB_CONF;
			} catch (SAXException e) {
				log.error("Error parsing libraries configuration file: "
						+ confFile, e);
				conf = ERROR_LIB_CONF;
			}
		}
	}

	private File makeConfFile() {
		return mergePathToBaseDir(LIB_CONF_NAME);
	}

	private File mergePathToBaseDir(String relativePath) {
		return mergePaths(basedir, relativePath);
	}

	private File mergePaths(File absoluteFile, String relativePath) {
		String absolutePath = absoluteFile.getAbsolutePath();
		absolutePath += "/";
		absolutePath += relativePath;
		return new File(absolutePath);
	}

	@Override
	public void save(Library entity) throws DAOException {
		throw new UnsupportedOperationException(
				"Persisting libraries is not supported!");
	}

	@Override
	public Library load(Long id) throws DAOException {
		throw new UnsupportedOperationException(
				"Libraries do not have identifiers!");
	}

	@Override
	public boolean exists(Long id) throws DAOException {
		throw new UnsupportedOperationException(
				"Libraries do not have identifiers!");
	}

	@Override
	public void delete(Long id) throws DAOException {
		throw new UnsupportedOperationException(
				"Persisting libraries is not supported!");
	}

	@Override
	public boolean exists(String name) throws DAOException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Library findByName(String name) throws DAOException {
		if (name == null) {
			throw new NullPointerException("Library name cant be null");
		}
		checkForMaliciousPath(name);
		File libDir = mergePathToBaseDir(name);
		if (!libDir.exists()) {
			throw new DAOException(StatusCodes.DAO_DOESNT_EXIST, "Library "
					+ libDir + " doesnt exist");
		}

		Library library = new Library(name);
		injectValueToPrivateField(library, "created", new Date(libDir
				.lastModified()));
		for (File f : libDir.listFiles()) {
			if (f.isFile()) {
				String content = readFile(f);
				String type = conf.getLibraryConf(name).getFileType();
				new LibraryFile(library, f.getName(), type, content);
			}
		}

		return library;
	}

	private String readFile(File file) {
		DataInputStream data = null;
		try {
			data = new DataInputStream(new BufferedInputStream(
					new FileInputStream(file)));
			byte[] bytes = new byte[(int) file.length()];
			data.readFully(bytes);
			return new String(bytes, "UTF-8");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		} finally {
			if (data != null) {
				try {
					data.close();
				} catch (IOException e) {
				}
			}
		}
	}

	private void injectValueToPrivateField(Object object, String fieldName,
			Object value) {
		try {
			Field field = object.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(object, value);
			field.setAccessible(false);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void checkForMaliciousPath(String name) {
		if (name.contains("./") || name.contains("../")) {
			throw new SecurityException("Malicious library name detected: "
					+ name);
		}
	}

	@Override
	public Set<Library> getAll() throws DAOException {
		Set<Library> libraries = new HashSet<Library>(conf.getLibraryCount());
		for (LibraryConf c : conf.getAvailableLibraries()) {
			libraries.add(findByName(c.getName()));
		}
		return libraries;
	}

}
