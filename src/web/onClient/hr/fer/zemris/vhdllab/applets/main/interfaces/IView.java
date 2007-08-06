package hr.fer.zemris.vhdllab.applets.main.interfaces;

/**
 * Interface that defines methods that every View must have. A View is a
 * component that is (most likely) situated on a far south of main panel window,
 * just above status bar. It is used to display useful information to a user.
 * View is read only (meaning user can not change view's contents) and its
 * contents will never be saved.
 * <p>
 * All views must have an empty public default constructor. All GUI
 * initialization, data initialization, thread creation or any other task that
 * initializes a view must be done in {@link #init()} method! Also a
 * {@link #dispose()} method is used for view to dispose any resources used by a
 * view or stop any background threads, remove any listeners, etc.
 * </p>
 * <p>
 * This is how a view will be initialized: <blockquote> ...<br/> IView view =
 * new ViewImplementation();<br/> view.setSystemContainer(aSystemContainer);<br/>
 * view.init();<br/> ... </blockquote>
 * </p>
 * <p>
 * A view will be disposed by simply invoking a {@link #dispose()} method.
 * </p>
 * 
 * @author Miro Bezjak
 */
public interface IView {

	/**
	 * Sets a project container so that this view may communicate with other
	 * components in main panel or gain any information that project container
	 * offers.
	 * 
	 * @param container
	 *            a system container
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer
	 */
	void setSystemContainer(ISystemContainer container);

	/**
	 * All GUI initialization, data initialization, thread creation or any other
	 * task that initializes a view is done in this method.
	 */
	void init();

	/**
	 * Disposes any resources used by a view or stop any background threads,
	 * remove any listeners, etc.
	 */
	void dispose();

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
	 * this method will throw no exception and will simply do nothing.
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
