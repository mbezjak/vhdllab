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

	public ViewPane() {
		super();
	}

	public ViewPane(int tabPlacement) {
		super(tabPlacement);
	}

	public ViewPane(int tabPlacement, int tabLayoutPolicy) {
		super(tabPlacement, tabLayoutPolicy);
	}
	
	private Map<String, Component> views = new HashMap<String, Component>();
	public Component add(String title, Component c, String type) {
		views.put(type, c);
		return add(title, c);
	}

	public IView getViewAt(int index) {
		return (IView) getComponentAt(index);
	}

	public int indexOfView(IView view) {
		for(int i = 0; i < getTabCount(); i++) {
			IView viewComponent = getViewAt(i);
			if(view.hashCode() == viewComponent.hashCode()) {
				return i;
			}
		}
		return -1;
	}
	
	public int indexOfView(String type) {
		Component c = views.get(type);
		return indexOfView((IView) c);
	}


	public boolean viewIsOpened(IView view) {
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
