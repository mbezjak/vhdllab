package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.selectcomponent;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ECanvasState;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;

import java.awt.BorderLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class TabbedCTAddToolbar extends JPanel implements
		PropertyChangeListener, ListSelectionListener {
	private static final long serialVersionUID = 2973973513115634524L;

	/**
	 * Debug/retail mode
	 */
	public static final boolean DEBUG_MODE = true;

	/**
	 * Schema controller
	 */
	private ISchemaController schemaController = null;

	/**
	 * Local controller za komunikaciju medju toolbarima
	 */
	private ILocalGuiController localController;

	/**
	 * Prvi ListBox
	 */
	private JList firstListBox = null;

	public TabbedCTAddToolbar(ISchemaController schemaController,
			ILocalGuiController localController) {
		super();

		if (schemaController == null) {
			throw new NullPointerException("Schema controller cannot be null!");
		}

		if (localController == null) {
			throw new NullPointerException(
					"Local GUI controller cannot be null!");
		}
		this.schemaController = schemaController;
		this.localController = localController;

		initToolbar();
	}

	/**
	 * 
	 */
	private void initToolbar() {
		setLayout(new BorderLayout());

		placePanel();
	}

	/**
	 * Stavlja stvoreni JTabbedPanel na toolbar
	 */
	private void placePanel() {
		JTabbedPane tabbedPane = createTabbedPane();
		faceLiftTabbedPane(tabbedPane);

		add(tabbedPane, BorderLayout.CENTER);
	}

	/**
	 * Uredjuje izgled JTabbedPane-a
	 * 
	 * @param tabbedPane
	 */
	private void faceLiftTabbedPane(JTabbedPane tabbedPane) {
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
	}

	/**
	 * Stvara JTabbedPane na temelju postojecih komponenti
	 * 
	 * @return JTabbedPane komponenti koje su razvrstane po kategorijama
	 */
	private JTabbedPane createTabbedPane() {
		// kategorija<->listboxmodel
		Map<String, DefaultListModel> categoryModel = createListModelByCategory();
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		for (Map.Entry<String, DefaultListModel> categoty : categoryModel
				.entrySet()) {
			JPanel pane = crateListBoxPane(categoty.getValue());

			tabbedPane.addTab(categoty.getKey(), pane);
		}

		return tabbedPane;
	}

	private JPanel crateListBoxPane(DefaultListModel value) {
		JPanel pane = new JPanel(new BorderLayout());
		JList listBox = new JList(value);

		faceLiftListBox(listBox);
		listBox.addListSelectionListener(this);
		pane.add(listBox, BorderLayout.CENTER);

		if (firstListBox == null)
			firstListBox = listBox;

		return pane;
	}

	private void faceLiftListBox(JList listBox) {
		listBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		listBox.setLayoutOrientation(JList.VERTICAL);
	}

	private void refreshToolbar() {
		firstListBox = null;
		removeAll();
		placePanel();
	}

	/**
	 * @return
	 */
	private Map<String, DefaultListModel> createListModelByCategory() {
		Map<String, DefaultListModel> categoryModelMap = new LinkedHashMap<String, DefaultListModel>();

		ISchemaInfo si = schemaController.getSchemaInfo();
		Map<Caseless, ISchemaComponent> prototypes = si.getPrototypes();

		// prolazi kroz sve prototipove i razvrstava ih u ispravnu kategoriju
		for (Map.Entry<Caseless, ISchemaComponent> component : prototypes
				.entrySet()) {
			String categoryName = component.getValue().getCategoryName();

			// ako u categotyModelMap vec postoji kljuc s imenom kategorije.....
			if (categoryModelMap.containsKey(categoryName)) {
				DefaultListModel model = categoryModelMap.get(categoryName);
				model.addElement(component.getKey().toString());
			} else {
				DefaultListModel model = new DefaultListModel();
				model.addElement(component.getKey().toString());

				categoryModelMap.put(categoryName, model);
			}
		}

		return categoryModelMap;
	}

	private void setFirstComponent() {
		if (firstListBox.getModel().getSize() > 0) {
			localController.setComponentToAdd(new Caseless(firstListBox
					.getModel().getElementAt(0).toString()));
			firstListBox.setSelectedIndex(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {

		if (evt.getPropertyName().equals(
				CanvasToolbarLocalGUIController.PROPERTY_CHANGE_STATE)) {
			if (localController.getComponentToAdd() == null) {
				if (ComponentToAddToolbar.DEBUG_MODE) {
					System.out
							.println("ComponentToAddToolbar: settingFirstComponent");
				}
				setFirstComponent();
				return;
			}
		} else if (evt.getPropertyName().equals(
				EPropertyChange.PROTOTYPES_CHANGE.toString())) {
			refreshToolbar();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		JList listBox = (JList) e.getSource();

		localController.setComponentToAdd(new Caseless(listBox
				.getSelectedValue().toString()));
		localController.setState(ECanvasState.ADD_COMPONENT_STATE);
	}

}
