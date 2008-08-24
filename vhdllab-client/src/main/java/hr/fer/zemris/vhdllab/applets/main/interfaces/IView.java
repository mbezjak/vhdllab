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
	 * Enables to get other interfaces that this view implements. Note that some
	 * implementation will not only look for interfaces that this view directly
	 * implements but also if some of its components implements specified
	 * interface.
	 * <p>
	 * This method can return <code>null</code> if:
	 * <ul>
	 * <li>any error occurs</li>
	 * <li><code>clazz</code> is <code>null</code></li>
	 * <li>this view does not implement specified interface</li>
	 * <li>this view refuses to return an interface (e.g. implementing
	 * interface is private and no outside components can invoke its methods)</li>
	 * </ul>
	 * </p>
	 * 
	 * @param <T>
	 *            an interface that this view implements
	 * @param clazz
	 *            a class of an interface that this view implements
	 * @return a specified interface or <code>null</code> if this view either
	 *         does not implement specified implement or refuses to
	 */
	<T> T asInterface(Class<T> clazz);

}
