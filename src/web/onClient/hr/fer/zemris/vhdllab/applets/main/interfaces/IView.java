package hr.fer.zemris.vhdllab.applets.main.interfaces;

/**
 * Interface that defines methods that every View must have. A View is a
 * component that is (most likely) situated on a far south of main applet
 * window, just above status bar. It is used to display useful information to a
 * user. View is read only (meaning user can not change view's contents) and its
 * contents will never be saved.
 * 
 * @author Miro Bezjak
 */
public interface IView {

	/**
	 * Sets a project container so that this view may communicate with other
	 * components in main applet or gain any information that project container
	 * offers.
	 * 
	 * @param container
	 *            a system container
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer
	 */
	void setSystemContainer(ISystemContainer container);

	/**
	 * Sets data to display in this view. If some data was already displayed
	 * then it should be replaced by this. If data is <code>null</code> than
	 * this method will throw no exception and will delete its contents.
	 * 
	 * @param data
	 *            a data to set.
	 * @throws IllegalArgumentException
	 *             if data provided through this method is unknown to
	 *             implementation of this IView interface. For example, if
	 *             implementation expects String but received an integer.
	 */
	void setData(Object data);

	/**
	 * Appends data to an end of this view. If data is <code>null</code> than
	 * this metod will throw no exception and will simply do nothing.
	 * 
	 * @param data
	 *            a data to append.
	 * @throws IllegalArgumentException
	 *             if data provided through this method is unknown to
	 *             implementation of this IView interface. For example, if
	 *             implementation expects String but received an integer.
	 */
	void appendData(Object data);

}
