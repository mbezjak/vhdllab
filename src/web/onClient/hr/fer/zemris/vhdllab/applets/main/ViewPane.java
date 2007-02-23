package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;

import java.awt.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JTabbedPane;

public class ViewPane extends JTabbedPane {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -8311136559571388001L;

	/**
     * Creates an empty <code>ViewPane</code> with a default
     * tab placement of <code>ViewPane.TOP</code>.
     */
	public ViewPane() {
		super();
	}

	/**
     * Creates an empty <code>ViewPane</code> with the specified tab placement
     * of either: <code>ViewPane.TOP</code>, <code>ViewPane.BOTTOM</code>,
     * <code>ViewPane.LEFT</code>, or <code>ViewPane.RIGHT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     */
	public ViewPane(int tabPlacement) {
		super(tabPlacement);
	}

	/**
     * Creates an empty <code>ViewPane</code> with the specified tab placement
     * and tab layout policy.  Tab placement may be either: 
     * <code>ViewPane.TOP</code>, <code>ViewPane.BOTTOM</code>,
     * <code>ViewPane.LEFT</code>, or <code>ViewPane.RIGHT</code>.
     * Tab layout policy may be either: <code>ViewPane.WRAP_TAB_LAYOUT</code>
     * or <code>ViewPane.SCROLL_TAB_LAYOUT</code>.
     *
     * @param tabPlacement the placement for the tabs relative to the content
     * @param tabLayoutPolicy the policy for laying out tabs when all tabs will not fit on one run
     * @throws IllegalArgumentException if tab placement or tab layout policy are not
     *            one of the above supported values
     */
	public ViewPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}
	
	private Map<String, Component> views = new HashMap<String, Component>();
	public Component add(String title, Component c, String type) {
		views.put(type, c);
		return add(title, c);
	}
	
	public IView getSelectedView() {
		return (IView) getSelectedComponent();
	}

	public IView getViewAt(int index) {
		return (IView) getComponentAt(index);
	}

	public int indexOfView(IView view) {
		for(int i = 0; i < getTabCount(); i++) {
			IView viewComponent = getViewAt(i);
			if(view == viewComponent) {
				return i;
			}
		}
		return -1;
	}
	
	public int indexOfView(String type) {
		Component c = views.get(type);
		return indexOfView((IView) c);
	}


	public boolean isViewOpened(IView view) {
		if(view == null) return false;
		return indexOfView(view) != -1;
	}

	public List<IView> getAllOpenedViews() {
		List<IView> openedViews = new ArrayList<IView>(getTabCount());
		for(int i = 0; i < getTabCount(); i++) {
			IView view = getViewAt(i);
			openedViews.add(view);
		}
		return openedViews;
	}

	public void closeView(IView view) {
		int index = indexOfView(view);
		remove(index);
	}

}