package hr.fer.zemris.vhdllab.service.impl;

import static hr.fer.zemris.vhdllab.api.StatusCodes.INTERNAL_SERVER_ERROR;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.FileManager;
import hr.fer.zemris.vhdllab.service.LibraryFileManager;
import hr.fer.zemris.vhdllab.service.LibraryManager;
import hr.fer.zemris.vhdllab.service.ProjectManager;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.service.UserFileManager;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import org.apache.log4j.Logger;

/**
 * A default implementation of {@link ServiceManager}.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class VHDLLabServiceManager implements ServiceManager {

	/**
	 * A log instance.
	 */
	private static final Logger log = Logger
			.getLogger(VHDLLabServiceManager.class);

	/**
	 * A default length of a various reports in characters.
	 * 
	 * e.g. see {@link #reportGeneration(String, VHDLGenerationResult, long)}
	 */
	private static final int REPORT_LENGTH = 1000;

	/**
	 * A project manager.
	 */
	private final ProjectManager projectManager;
	/**
	 * A file manager.
	 */
	private final FileManager fileManager;
	/**
	 * A library manager.
	 */
	private final LibraryManager libraryManager;
	/**
	 * A library file manager.
	 */
	private final LibraryFileManager libraryFileManager;
	/**
	 * A user file manager.
	 */
	private final UserFileManager userFileManager;

	/**
	 * Sole constructor.
	 * 
	 * @param projectManager
	 *            a project manager
	 * @param fileManager
	 *            a file manager
	 * @param libraryManager
	 *            a library manager
	 * @param libraryFileManager
	 *            a library file manager
	 * @param userFileManager
	 *            a user file manager
	 * @throws NullPointerException
	 *             if either parameter is <code>null</code>
	 */
	public VHDLLabServiceManager(ProjectManager projectManager,
			FileManager fileManager, LibraryManager libraryManager,
			LibraryFileManager libraryFileManager,
			UserFileManager userFileManager) {
		if (projectManager == null) {
			throw new NullPointerException("Project manager cant be null");
		}
		if (fileManager == null) {
			throw new NullPointerException("File manager cant be null");
		}
		if (libraryManager == null) {
			throw new NullPointerException("Library manager cant be null");
		}
		if (libraryFileManager == null) {
			throw new NullPointerException("Library file manager cant be null");
		}
		if (userFileManager == null) {
			throw new NullPointerException("User file manager cant be null");
		}
		this.projectManager = projectManager;
		this.fileManager = fileManager;
		this.libraryManager = libraryManager;
		this.libraryFileManager = libraryFileManager;
		this.userFileManager = userFileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getProjectManager()
	 */
	@Override
	public ProjectManager getProjectManager() {
		return projectManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getFileManager()
	 */
	@Override
	public FileManager getFileManager() {
		return fileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getLibraryManager()
	 */
	@Override
	public LibraryManager getLibraryManager() {
		return libraryManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getLibraryFileManager()
	 */
	@Override
	public LibraryFileManager getLibraryFileManager() {
		return libraryFileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#getUserFileManager()
	 */
	@Override
	public UserFileManager getUserFileManager() {
		return userFileManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.service.ServiceManager#generateVHDL(hr.fer.zemris.vhdllab.entities.File)
	 */
	@Override
	public VHDLGenerationResult generateVHDL(File file) throws ServiceException {
		String className = getFileTypeMapping(file.getType()).getGenerator();
		if (className == null) {
			String message = "No generator defined for file type: "
					+ file.getType();
			log.error(message);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
		VHDLGenerator generator = instantiateClass(className);
		VHDLGenerationResult result;
		long start = System.currentTimeMillis();
		try {
			result = generator.generateVHDL(file);
		} catch (RuntimeException e) {
			String message = className + " threw exception during generation!";
			log.error(message, e);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
		long end = System.currentTimeMillis();
		if (result == null) {
			String message = "Generator " + className
					+ " returned null result!";
			log.error(message);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
		reportGeneration(className, result, end - start);
		return result;
	}

	/**
	 * Returns a file type mapping configuration for specified file type. Return
	 * value will never be <code>null</code>.
	 * 
	 * @param fileType
	 *            a file type for whom to return file type mapping
	 * @return a file type mapping configuration for specified file type
	 * @throws ServiceException
	 *             if <code>fileType</code> is not defined in configuration
	 *             file
	 */
	private FileTypeMapping getFileTypeMapping(String fileType)
			throws ServiceException {
		ServerConf conf = ServerConfParser.getConfiguration();
		FileTypeMapping mapping = conf.getFileTypeMapping(fileType);
		if (mapping == null) {
			String message = "File type '" + fileType
					+ "' is not defined in configuration file!";
			log.error(message);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
		return mapping;
	}

	/**
	 * Instantiates specified class by invoking default constructor. Return
	 * value will never be <code>null</code>.
	 * 
	 * @param className
	 *            a name of a class to instantiate
	 * @return an instantiated object of specified class
	 * @throws ServiceException
	 *             if exceptional condition occurs
	 */
	@SuppressWarnings("unchecked")
	private <T> T instantiateClass(String className) throws ServiceException {
		Object object = null;
		try {
			object = Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			String message = className + " couldn't be instantiated!";
			log.error(message, e);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		} catch (IllegalAccessException e) {
			String message = className
					+ " doesn't have public default constructor!";
			log.error(message, e);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		} catch (ClassNotFoundException e) {
			String message = "Class " + className + " doesn't exist!";
			log.error(message, e);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
		try {
			return (T) object;
		} catch (ClassCastException e) {
			String message = "Inappropriate class type!";
			log.error(message, e);
			throw new ServiceException(INTERNAL_SERVER_ERROR, message);
		}
	}

	/**
	 * If debugging is enabled then this method will log a VHDL generation.
	 * 
	 * @param className
	 *            a generator class name
	 * @param result
	 *            a VHDL generation result
	 * @param length
	 *            a time in milliseconds that generation took
	 */
	private void reportGeneration(String className,
			VHDLGenerationResult result, long length) {
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder(REPORT_LENGTH);
			sb.append("Generator ").append(className);
			sb.append(" finished generation in ");
			sb.append(length).append("ms:\n").append(result);
			sb.append("\nGenerated VHDL code:\n").append(result.getVHDL());
			sb.append("\n-----------------------------------");
			log.debug(sb.toString());
		}
	}

}
