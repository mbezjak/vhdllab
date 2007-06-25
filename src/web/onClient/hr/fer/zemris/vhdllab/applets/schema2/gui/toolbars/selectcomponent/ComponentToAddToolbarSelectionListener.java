package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent;

import hr.fer.zemris.vhdllab.applets.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class ComponentToAddToolbarSelectionListener implements
		ListSelectionListener {

	/**
	 * Local controller for intercommunication with canvas
	 */
	private ILocalGuiController localController = null;

	public ComponentToAddToolbarSelectionListener(
			ILocalGuiController localController) {

		this.localController = localController;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		localController.setComponentToAdd(new Caseless(e.toString()));
		System.out.println("List selection listener: " + e.toString());
		localController.setState(ECanvasState.ADD_COMPONENT_STATE);
	}

}
