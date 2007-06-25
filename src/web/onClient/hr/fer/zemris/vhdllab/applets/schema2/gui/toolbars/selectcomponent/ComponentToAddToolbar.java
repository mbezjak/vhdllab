package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;

public class ComponentToAddToolbar extends JPanel implements
		PropertyChangeListener {

	private static final long serialVersionUID = -4549154212847889157L;

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
		ISchemaComponentCollection componentCollection = null;
		Set<Caseless> componentNames = null;
		DefaultListModel listModel = new DefaultListModel();

		componentCollection = schemaController.getSchemaInfo().getComponents();
		componentNames = componentCollection.getComponentNames();
		System.out.println("Number of registered components:"
				+ componentNames.size());
		for (Caseless name : componentNames) {
			listModel.addElement(name.toString());
		}

		return listModel;
	}

	public void RefreshList() {
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
		// ako ce ikada u buducnosti trebati komunikacija sa lokalnim
		// kontrolerom i u ovom smjeru
	}

}
