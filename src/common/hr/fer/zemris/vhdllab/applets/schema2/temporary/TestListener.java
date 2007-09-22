package hr.fer.zemris.vhdllab.applets.schema2.temporary;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TestListener implements PropertyChangeListener {
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("Change: " + evt.getPropertyName());
	}
}