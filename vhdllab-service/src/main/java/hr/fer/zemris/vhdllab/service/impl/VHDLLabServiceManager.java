package hr.fer.zemris.vhdllab.service.impl;

import static hr.fer.zemris.vhdllab.api.StatusCodes.INTERNAL_SERVER_ERROR;
import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.server.conf.FileTypeMapping;
import hr.fer.zemris.vhdllab.server.conf.ServerConf;
import hr.fer.zemris.vhdllab.server.conf.ServerConfParser;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.ServiceManager;
import hr.fer.zemris.vhdllab.service.VHDLGenerator;

import java.util.HashMap;
import java.util.Map;

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

    private final Map<String, VHDLGenerator> generators;

    /**
     * Default constructor.
     * 
     * @throws IllegalStateException
     *             if classes associated with file types can't be instantiated
     *             (e.g. specified generator class name could not be found)
     */
    public VHDLLabServiceManager() {
        generators = new HashMap<String, VHDLGenerator>();
        ServerConf conf = ServerConfParser.getConfiguration();
        for (String type : conf.getFileTypes()) {
            FileTypeMapping m = conf.getFileTypeMapping(type);
            String className = m.getGenerator();
            if (className != null) {
                VHDLGenerator gen = instantiateClass(className);
                generators.put(type, gen);
                if (log.isDebugEnabled()) {
                    log.debug("Instantiated generator " + className
                            + " for file type: " + type);
                }
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("No defined generator for file type: " + type);
                }
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.service.ServiceManager#generateVHDL(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public VHDLGenerationResult generateVHDL(File file) throws ServiceException {
        VHDLGenerator generator = getGenerator(file.getType());
        String className = generator.getClass().getCanonicalName();
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
     * Returns a VHDL generator for specified file type. Return value will neven
     * be <code>null</code>.
     * 
     * @param type
     *            a file type for whom to return generator
     * @return a VHDL generator for specified file type
     * @throws ServiceException
     *             if no generator for specified file type could be found
     */
    private VHDLGenerator getGenerator(String type) throws ServiceException {
        VHDLGenerator gen = generators.get(type);
        if (gen == null) {
            throw new ServiceException(INTERNAL_SERVER_ERROR,
                    "No generator for type: " + type);
        }
        return gen;
    }

    /**
     * Instantiates specified class by invoking default constructor. Return
     * value will never be <code>null</code>.
     * 
     * @param className
     *            a name of a class to instantiate
     * @return an instantiated object of specified class
     * @throws IllegalStateException
     *             if exceptional condition occurs
     */
    @SuppressWarnings("unchecked")
    private <T> T instantiateClass(String className) {
        Object object = null;
        try {
            object = Class.forName(className).newInstance();
        } catch (InstantiationException e) {
            String message = className + " couldn't be instantiated!";
            log.error(message, e);
            throw new IllegalStateException(message);
        } catch (IllegalAccessException e) {
            String message = className
                    + " doesn't have public default constructor!";
            log.error(message, e);
            throw new IllegalStateException(message);
        } catch (ClassNotFoundException e) {
            String message = "Class " + className + " doesn't exist!";
            log.error(message, e);
            throw new IllegalStateException(message);
        }
        try {
            return (T) object;
        } catch (ClassCastException e) {
            String message = "Inappropriate class type!";
            log.error(message, e);
            throw new IllegalStateException(message);
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
