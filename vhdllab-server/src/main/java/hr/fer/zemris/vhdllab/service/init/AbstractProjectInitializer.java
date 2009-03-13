package hr.fer.zemris.vhdllab.service.init;

import hr.fer.zemris.vhdllab.dao.FileDao;
import hr.fer.zemris.vhdllab.dao.ProjectDao;
import hr.fer.zemris.vhdllab.entity.Project;
import hr.fer.zemris.vhdllab.entity.ProjectType;
import hr.fer.zemris.vhdllab.util.IOUtils;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Provides a way to initialize a project.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public abstract class AbstractProjectInitializer implements ProjectInitializer {

    @Autowired
    private ProjectDao projectDao;
    @Autowired
    private FileDao fileDao;

    /**
     * Names of project files to initialize, if any. Files must be located in
     * the same package as implementor of this class.
     */
    private String[] fileNames;

    /**
     * Setter for fileNames. Files with specified names must be located in the
     * same package as class extending this class.
     * 
     * @param fileNames
     *            names of a file to initialize
     */
    public void setFileNames(String[] fileNames) {
        this.fileNames = fileNames;
    }

    /**
     * Initializes specified project and provided files (through
     * {@link #setFileNames(String[])} method), if any.
     * 
     * @param type
     *            type of a project
     */
    protected void initProject(ProjectType type) {
        Project project = persistProject(type);
        if (fileNames != null) {
            for (String fileName : fileNames) {
                persistProjectFile(project, fileName);
            }
        }
    }

    /**
     * Creates a project if it doesn't exist already.
     * 
     * @param type
     *            type of a project
     * @return created or existing specified project
     */
    protected Project persistProject(ProjectType type) {
        Project project = projectDao.findByName(name);
        if (project == null) {
            project = new Project(name);
            projectDao.save(project);
        }
        return project;
    }

    /**
     * Creates a library file if it doesn't exist already.
     * 
     * @param project
     *            a project to add a file to
     * @param name
     *            a name of a file
     */
    private void persistProjectFile(Project project, String name) {
        String data = readFile(name);
        Caseless predefinedName = new Caseless(name);
        LibraryFile predefinedFile = fileDao.findByName(project.getId(),
                predefinedName);
        if (predefinedFile == null) {
            predefinedFile = new LibraryFile(predefinedName, data);
            project.addFile(predefinedFile);
            fileDao.save(predefinedFile);
        }
    }

    /**
     * Reads a content of specified file. File must be located in the same
     * package as instantiated class.
     * 
     * @param name
     *            a name of a file
     * @return content of a specified file
     */
    private String readFile(String name) {
        InputStream is = getClass().getResourceAsStream(name);
        return IOUtils.toString(is);
    }

}
