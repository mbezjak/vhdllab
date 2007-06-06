package hr.fer.zemris.vhdllab.applets.schema2.enums;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;




public enum EPropertyChange {
	ANY_CHANGE() {
		@Override
		public void assignListenerToSupport(PropertyChangeListener listener, PropertyChangeSupport support) {
			support.addPropertyChangeListener(listener);
		}
	},
	CANVAS_CHANGE() {
		@Override
		public String toString() {
			return "CANVAS_CHANGE";
		}
	},
	COMPONENT_PROPERTY_CHANGE() {
		@Override
		public String toString() {
			return "COMPONENT_PROPERTY_CHANGE";
		}	
	},
	NO_CHANGE() {
		@Override
		public void assignListenerToSupport(PropertyChangeListener listener, PropertyChangeSupport support) {
			// notify nobody
		}
	};
	
	/**
	 * Ovisno o tipu svojstva cija se promjena ocekuje,
	 * PropertyChange postavlja listener na support.
	 * 
	 * @param listener
	 * @param support
	 */
	public void assignListenerToSupport(PropertyChangeListener listener, PropertyChangeSupport support) {
		support.addPropertyChangeListener(this.toString(), listener);			
	}
	
	/**
	 * Ovisno o tipu svojstva cija se promjena desila,
	 * pobuduje odgovarajuci event nad supportom.
	 * 
	 * @param support
	 */
	public void firePropertyChanges(PropertyChangeSupport support, Object oldval, Object newval) {
		support.firePropertyChange(this.toString(), oldval, newval);
	}
}




