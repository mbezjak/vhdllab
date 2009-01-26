package hr.fer.zemris.vhdllab.applets.main.interfaces;

import hr.fer.zemris.vhdllab.applets.main.component.projectexplorer.IProjectExplorer;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;
import hr.fer.zemris.vhdllab.platform.manager.editor.Editor;

import java.util.List;

/**
 * A system container is a core of vhdllab and contains important methods about
 * the system. It is designed to be a mediator between a component module (an
 * editor for example) and a system or another module. Since modules can't
 * communicate directly they can use some of system container's methods to
 * establish communication. System container is also a way of getting required
 * resource from a server (that is actually provided in {@link IResourceManager}
 * interface but system container providers resource manager interface).
 * 
 * @author Miro Bezjak
 */
public interface ISystemContainer {

    /* COMPILE RESOURCE METHODS */
    
    IProjectExplorer getProjectExplorer();

    /**
     * Shows a dialog to a user so he can select a resource that he wants
     * compiled. A user can cancel a dialog and in that case this method will
     * return <code>false</code>.
     * 
     * @return <code>true</code> if a resource has been compiled; false
     *         otherwise
     * @see #compileLastHistoryResult()
     * @see #compile(FileIdentifier)
     * @see #compile(Caseless, Caseless)
     */
    boolean compileWithDialog();

    /**
     * Compiles a last recently compiled resource. This method will return
     * <code>true</code> if resource has been compiled or <code>false</code>
     * otherwise. If no resource has been compiled then a dialog will present to
     * a user to select a resource to compile (like in
     * {@link #compileWithDialog()} method).
     * 
     * @return <code>true</code> if resource has been compiled;
     *         <code>false</code> otherwise
     * @see #compileWithDialog()
     * @see #compile(FileIdentifier)
     * @see #compile(Caseless, Caseless)
     */
    boolean compileLastHistoryResult();

    /**
     * Compiles specified resource. If <code>file</code> is <code>null</code>
     * this method will not throw an exception and will simply return
     * <code>false</code>.
     * <p>
     * Note that some resource is not compilable (simulation for example)
     * therefor this method will not compile such resource. To check if a
     * resource is compilable invoke
     * {@link IResourceManager#isCompilable(Caseless, Caseless)} method.
     * </p>
     * 
     * @param file
     *            a resource to compile
     * @return <code>true</code> if resource has been compiled;
     *         <code>false</code> otherwise
     * @see #compileWithDialog()
     * @see #compileLastHistoryResult()
     * @see #compile(Caseless, Caseless)
     */
    boolean compile(FileIdentifier file);

    /**
     * Compiles a specified resource. If any error occurs this method will
     * simply return <code>false</code>.
     * <p>
     * If either parameter is <code>null</code> this method will not throw an
     * exception and will simply return <code>false</code>.
     * </p>
     * 
     * @param projectName
     *            a resource's project name
     * @param fileName
     *            a resource's file name
     * @return <code>true</code> if resource has been compiled;
     *         <code>false</code> otherwise
     * @see #compileWithDialog()
     * @see #compileLastHistoryResult()
     * @see #compile(FileIdentifier)
     */
    boolean compile(Caseless projectName, Caseless fileName);

    /* SIMULATE RESOURCE METHODS */

    /**
     * Shows a dialog to a user so he can select a resource that he wants
     * simulated. A user can cancel a dialog and in that case this method will
     * return <code>false</code>.
     * 
     * @return <code>true</code> if a resource has been simulated; false
     *         otherwise
     * @see #simulateLastHistoryResult()
     * @see #simulate(FileIdentifier)
     * @see #simulate(Caseless, Caseless)
     */
    boolean simulateWithDialog();

    /**
     * Simulates a last recently simulated resource. This method will return
     * <code>true</code> if resource has been simulated or <code>false</code>
     * otherwise. If no resource has been simulated then a dialog will present
     * to a user to select a resource to simulate (like in
     * {@link #simulateWithDialog()} method).
     * 
     * @return <code>true</code> if resource has been simulated;
     *         <code>false</code> otherwise
     * @see #simulateWithDialog()
     * @see #simulate(FileIdentifier)
     * @see #simulate(Caseless, Caseless)
     */
    boolean simulateLastHistoryResult();

    /**
     * Simulates specified resource. If <code>file</code> is <code>null</code>
     * this method will not throw an exception and will simply return
     * <code>false</code>.
     * <p>
     * Note that some resource is not simulatable (vhdl source for example)
     * therefor this method will not simulate such resource. To check if a
     * resource is simulatable invoke
     * {@link IResourceManager#isSimulatable(Caseless, Caseless)} method.
     * </p>
     * 
     * @param file
     *            a resource to simulate
     * @return <code>true</code> if resource has been simulated;
     *         <code>false</code> otherwise
     * @see #simulateWithDialog()
     * @see #simulateLastHistoryResult()
     * @see #simulate(Caseless, Caseless)
     */
    boolean simulate(FileIdentifier file);

    /**
     * Simulates a specified resource. If any error occurs this method will
     * simply return <code>false</code>.
     * <p>
     * If either parameter is <code>null</code> this method will not throw an
     * exception and will simply return <code>false</code>.
     * </p>
     * 
     * @param projectName
     *            a resource's project name
     * @param fileName
     *            a resource's file name
     * @return <code>true</code> if resource has been simulated;
     *         <code>false</code> otherwise
     * @see #simulateWithDialog()
     * @see #simulateLastHistoryResult()
     * @see #simulate(FileIdentifier)
     */
    boolean simulate(Caseless projectName, Caseless fileName);

    /* RESOURCE MANIPULATION METHODS */

    /**
     * Opens a dialog for creating new project. This method will return
     * <code>true</code> if specified project was successfully created or
     * <code>false</code> otherwise.
     */
    void createNewProjectInstance();

    /**
     * Opens a dialog for creating new resource. This method will return
     * <code>true</code> if specified resource was successfully created or
     * <code>false</code> otherwise.
     * 
     * @param type
     *            a type of a file to create
     * @return <code>true</code> if specified resource was successfully created;
     *         <code>false</code> otherwise
     * @throws NullPointerException
     *             if <code>type</code> is <code>null</code>
     */
    boolean createNewFileInstance(FileType type);

    /**
     * Returns a currently selected project in a project explorer or
     * <code>null</code> if no project is selected.
     * 
     * @return a currently selected project in a project explorer or
     *         <code>null</code> if no project is selected
     */
    Caseless getSelectedProject();

    /**
     * Returns a currently selected file in a project explorer or
     * <code>null</code> if no file is selected.
     * 
     * @return a currently selected file in a project explorer or
     *         <code>null</code> if no file is selected
     */
    FileIdentifier getSelectedFile();

    /* MANAGER GETTER METHODS */

    /**
     * Returns a resource manager for manipulating resources. Return value will
     * never be <code>null</code>.
     * 
     * @return a resource manager
     */
    IResourceManager getResourceManager();

    List<Editor> showSaveDialog(String title, String message,
            List<Editor> editorsToBeSaved);

}