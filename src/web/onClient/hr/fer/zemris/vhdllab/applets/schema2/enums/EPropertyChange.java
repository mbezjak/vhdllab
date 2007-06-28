package hr.fer.zemris.vhdllab.applets.schema2.enums;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;




public enum EPropertyChange {
	
	/**
	 * Bilo koja promjena - u slucaju promjene se obavjestavaju
	 * svi listeneri registrirani na sve EPropertyChange
	 * vrijednosti osim ANY_CHANGE i NO_CHANGE.
	 */
	ANY_CHANGE() {
		@Override
		public void assignListenerToSupport(PropertyChangeListener listener, PropertyChangeSupport support) {
			support.addPropertyChangeListener(ALL_CHANGES_KEY, listener);
		}
		@Override
		public void firePropertyChanges(PropertyChangeSupport support, Object oldval, Object newval) {
			for (EPropertyChange epc : EPropertyChange.values()) {
				if (epc == NO_CHANGE || epc == ANY_CHANGE) continue;
				support.firePropertyChange(epc.toString(), oldval, newval);
			}
			support.firePropertyChange(ALL_CHANGES_KEY, oldval, newval);
		}
		@Override
		public String toString() {
			return "ANY_CHANGE";
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
	PROTOTYPES_CHANGE() {
		@Override
		public String toString() {
			return "PROTOTYPES_CHANGE";
		}
	},
	NO_CHANGE() {
		@Override
		public void assignListenerToSupport(PropertyChangeListener listener, PropertyChangeSupport support) {
			// notify nobody
		}
	};
	
	/**
	 * Za sve listenere koji slusaju sve promjene.
	 */
	private static final String ALL_CHANGES_KEY = "EVERY_CHANGE";
	
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
		support.firePropertyChange(ALL_CHANGES_KEY, oldval, newval);
	}
}




