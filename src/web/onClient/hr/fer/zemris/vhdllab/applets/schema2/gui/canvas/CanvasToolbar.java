package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ResourceBundle;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class CanvasToolbar extends JToolBar implements PropertyChangeListener,ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8135365480214217897L;

	private ILocalGuiController localController = null;
	private JToggleButton addWireState=null;
	private JToggleButton selactState=null;
	private JToggleButton deleteState=null;

	private ResourceBundle bundle = null; //TODO kad aleks sredi bundle
	
	public CanvasToolbar(ResourceBundle bundle) {
		this.bundle =bundle;
		initGUI();
	}
	
	private void initGUI() {
		
		//TODO tooltip text!!
		Icon ic=new ImageIcon(getClass().getResource("AddWire.png"));
		addWireState=new JToggleButton(ic);
		addWireState.setActionCommand("ADDWIRE");
		addWireState.setToolTipText("Add wire..");
		addWireState.addActionListener(this);
				
		ic=new ImageIcon(getClass().getResource("SelectMode.png"));
		selactState=new JToggleButton(ic);
		selactState.setActionCommand("SELECT");
		selactState.setToolTipText("Edit...");
		selactState.addActionListener(this);
		
		ic=new ImageIcon(getClass().getResource("DeleteMode.png"));
		deleteState=new JToggleButton(ic);
		deleteState.setActionCommand("DELETE");
		deleteState.setToolTipText("Delete...");
		deleteState.addActionListener(this);
		
		//TODO ovo maknuti kad se srede stvari:

		final JToggleButton b = new JToggleButton("SNAP ON");
		b.setSelected(true);
		b.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(localController.isGridON()){
					b.setText("SNAP OFF");
					b.setSelected(false);
					localController.setGridON(false);
				}else{
					b.setText("SNAP ON");
					b.setSelected(true);
					localController.setGridON(true);
				}
			}
			
		});
		
		final JToggleButton c = new JToggleButton("SMART CONNECT ON");
		c.setSelected(true);
		c.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				if(localController.isSmartConectON()){
					c.setText("SMART CONNECT OFF");
					c.setSelected(false);
					localController.setSmartConectON(false);
				}else{
					c.setText("SMART CONNECT ON");
					c.setSelected(true);
					localController.setSmartConectON(true);
				}
			}
			
		});
		
		//############################
		
		this.add(selactState);
		this.add(addWireState);
		this.add(deleteState);
		
		//#####
		this.add(b);
		this.add(c);
		//#####
		
	}

	public void registerController(ILocalGuiController controller){
		this.localController = controller;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		ECanvasState state = localController.getState();
		if(state.equals(ECanvasState.ADD_WIRE_STATE)){
			addWireState.setSelected(true);
			selactState.setSelected(false);
			deleteState.setSelected(false);
		}else if(state.equals(ECanvasState.MOVE_STATE)){
			addWireState.setSelected(false);
			selactState.setSelected(true);
			deleteState.setSelected(false);
		}else if(state.equals(ECanvasState.DELETE_STATE)){
			addWireState.setSelected(false);
			selactState.setSelected(false);
			deleteState.setSelected(true);
		}else {
			addWireState.setSelected(false);
			selactState.setSelected(false);
			deleteState.setSelected(false);
		}
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("ADDWIRE")){
			localController.setState(ECanvasState.ADD_WIRE_STATE);
		}
		else if(e.getActionCommand().equals("SELECT")){
			localController.setState(ECanvasState.MOVE_STATE);
		}
		else if(e.getActionCommand().equals("DELETE")){
			localController.setState(ECanvasState.DELETE_STATE);
		}
		propertyChange(null);
	}
	
	private void dummyStateChanger() {
		ECanvasState state = localController.getState();
		if(state.equals(ECanvasState.ADD_COMPONENT_STATE))
			localController.setState(ECanvasState.ADD_WIRE_STATE);
		else if(state.equals(ECanvasState.ADD_WIRE_STATE))
			localController.setState(ECanvasState.DELETE_STATE);
		else if(state.equals(ECanvasState.DELETE_STATE))
			localController.setState(ECanvasState.MOVE_STATE);
		else if(state.equals(ECanvasState.MOVE_STATE))
			localController.setState(ECanvasState.ADD_COMPONENT_STATE);
	}
	
}
