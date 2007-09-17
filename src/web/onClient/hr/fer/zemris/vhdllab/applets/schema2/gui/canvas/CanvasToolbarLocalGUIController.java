package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Razred CanvasToolbarLocalGUIController služi kao lokalni controller za
 * SchemaCanvas i SchemaToolbar u kontekstu lokalnog MVC-a za GUI scheme.
 * 
 * @author ddelac
 * 
 */
public class CanvasToolbarLocalGUIController implements ILocalGuiController {

	/**
	 * Bilo koja promjena
	 */
	public static final String PROPERTY_CHANGE_COMPONENT_TO_ADD = "ChangeComponentToAdd";

	/**
	 * Promjena oznacene komponente (ComponentPropertyToolbar)
	 */
	public static final String PROPERTY_CHANGE_SELECTION = "ChangeSelection";

	/**
	 * Promjena stanja
	 */
	public static final String PROPERTY_CHANGE_STATE = "ChangeState";

	public static final int TYPE_WIRE = 1;
	public static final int TYPE_COMPONENT = 0;
	public static final int TYPE_NOTHING_SELECTED = 2;

	private PropertyChangeSupport support = null;

	private ECanvasState state = ECanvasState.MOVE_STATE;
	private Caseless componentToAdd = null;
	private Caseless selectedComponent = new Caseless("");
	private int selectedType = 0;

	// TODO makni kasnije
	private boolean gridON = true;

	// #########

	public CanvasToolbarLocalGUIController() {
		support = new PropertyChangeSupport(this);
	}

	public void addListener(PropertyChangeListener listener) {
		support.addPropertyChangeListener(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#addListener(java.beans.PropertyChangeListener)
	 */
	public void addListener(String properyName, PropertyChangeListener listener) {
		support.addPropertyChangeListener(properyName, listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#getComponentToAdd()
	 */
	public Caseless getComponentToAdd() {
		return componentToAdd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#setComponentToAdd(hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless)
	 */
	public void setComponentToAdd(Caseless componentToAdd) {
		Caseless temp = this.componentToAdd;
		this.componentToAdd = componentToAdd;
		support.firePropertyChange(PROPERTY_CHANGE_COMPONENT_TO_ADD, temp,
				componentToAdd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#getSelectedComponent()
	 */
	public Caseless getSelectedComponent() {
		return selectedComponent;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#setSelectedComponent(hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless)
	 */
	public void setSelectedComponent(Caseless selectedComponent, int compOrWire) {
		Caseless temp = this.selectedComponent;
		this.selectedType = compOrWire;
		this.selectedComponent = selectedComponent;
		support.firePropertyChange(PROPERTY_CHANGE_SELECTION, temp,
				selectedComponent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#getState()
	 */
	public ECanvasState getState() {
		return state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.ILocalGuiController#setState(java.lang.Integer)
	 */
	public void setState(ECanvasState state) {
		ECanvasState temp = this.state;
		this.state = state;
		support.firePropertyChange(PROPERTY_CHANGE_STATE, temp, state);
	}

	@Override
	public String toString() {
		return "State:" + state + "|ComponentToAdd:" + componentToAdd
				+ "|SelectedComponent:" + selectedComponent;
	}

	public int getSelectedType() {
		return selectedType;
	}

	public boolean isGridON() {
		return gridON;
	}

	public void setGridON(boolean gridON) {
		this.gridON = gridON;
		support.firePropertyChange("GRID", null, null);
	}
}
