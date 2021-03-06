/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;

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
	/**
	 * Bilo kakva promjena nad rasporedom ili razmjestajem komponenti, pinova,
	 * ili zica, ili segmenata zice, ali <b>ne</b> promjena koja obuhvaca
	 * <b>iskljucivo</b> promjenu vrijednosti parametara.
	 */
	CANVAS_CHANGE() {
		@Override
		public String toString() {
			return "CANVAS_CHANGE";
		}
	},
	/**
	 * Bilo kakva promjena vrijednosti parametara zice, komponente na shemi
	 * ili modelirane komponente.
	 */
	PROPERTY_CHANGE() {
		@Override
		public String toString() {
			return "COMPONENT_PROPERTY_CHANGE";
		}
	},
	/**
	 * Bilo kakva promjena u kolekciji prototipova.
	 */
	PROTOTYPES_CHANGE() {
		@Override
		public String toString() {
			return "PROTOTYPES_CHANGE";
		}
	},
	/**
	 * Nikakva promjena u modelu.
	 */
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




