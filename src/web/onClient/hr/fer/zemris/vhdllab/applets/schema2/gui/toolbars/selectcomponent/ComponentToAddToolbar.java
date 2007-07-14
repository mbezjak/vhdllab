package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class ComponentToAddToolbar extends JPanel implements
		PropertyChangeListener {

	private static final long serialVersionUID = -4549154212847889157L;

	public static final boolean DEBUG_MODE = true;

	/**
	 * Schema controller
	 */
	private ISchemaController schemaController = null;

	/**
	 * Listener for change selection in list box (JList)
	 */
	private ComponentToAddToolbarSelectionListener selectionListener = null;

	/**
	 * List box
	 */
	JList list = null;

	/**
	 * Constructor
	 * 
	 * @param schemaController
	 *            schema controller
	 * @param localController
	 *            local GUI controller
	 */
	public ComponentToAddToolbar(ISchemaController schemaController,
			ILocalGuiController localController) {

		if (schemaController == null) {
			throw new NullPointerException("Schema controller cannot be null!");
		}

		if (localController == null) {
			throw new NullPointerException(
					"Local GUI controller cannot be null!");
		}

		this.schemaController = schemaController;
		this.selectionListener = new ComponentToAddToolbarSelectionListener(
				localController);

		initToolbar();
	}

	/**
	 * Init Toolbar GUI
	 */
	private void initToolbar() {
		setLayout(new BorderLayout());

		initList();

		add(new JLabel("Components:"), BorderLayout.NORTH);
		add(list, BorderLayout.CENTER);
	}

	private void initList() {
		AbstractListModel listModel = null;

		listModel = getListBoxModel();
		list = new JList(listModel);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL_WRAP);
		list.addListSelectionListener(selectionListener);
	}

	/**
	 * Creates a new list box model filled wiht available components
	 * 
	 * @return
	 */
	private AbstractListModel getListBoxModel() {
		ISchemaInfo si = schemaController.getSchemaInfo();
		Map<Caseless, ISchemaComponent> prototypes = si.getPrototypes();
		DefaultListModel listModel = new DefaultListModel();

		if (DEBUG_MODE) {
			System.out.println("ComponentToAddToolbar: numberOfPrototypes="
					+ prototypes.size());
		}

		for (Map.Entry<Caseless, ISchemaComponent> elem : prototypes.entrySet()) {
			listModel.addElement(elem.getKey());
		}

		return listModel;
	}

	public void RefreshList() {
		if (DEBUG_MODE) {
			System.out
					.println("ComponentToAddToolbar: propertyChangeEvent-refreshing list");
		}
		remove(list);

		initList();
		add(list, BorderLayout.CENTER);
	}

	// TODO treba napraviti listener u slucaju da se doda neka nova komponenta u
	// kolekciju postojecih komponenti

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent evt) {
		if (DEBUG_MODE) {
			System.out.println("ComponentToAddToolbar: propertyChangeEvent");
			System.out.println("ComponentToAddToolbar: propertyNewValue="
					+ evt.getNewValue().toString());
		}

		RefreshList();
	}

}
