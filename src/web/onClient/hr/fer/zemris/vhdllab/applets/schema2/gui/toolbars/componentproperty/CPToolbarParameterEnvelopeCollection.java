package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

/**
 * Razred koji se brine o parametrima, njihovim prikazom u tabeli te njihovom
 * promjenom
 * 
 * @author Garfield
 * 
 */
public class CPToolbarParameterEnvelopeCollection {

	/**
	 * Komponenta ciji se parametri obradjuju
	 */
	private ISchemaComponent component = null;

	/**
	 * Lista parametara spremnih za prikaz u JTable
	 */
	private List<ParameterEnvelope> parameters = null;

	/**
	 * Row model za JTableX
	 */
	private RowEditorModel rowModel = null;

	/**
	 * Konstruktor
	 * 
	 * @param component
	 *            instanca komponente
	 */
	public CPToolbarParameterEnvelopeCollection(ISchemaComponent component) {
		this.component = component;

		buildParameters();
		buildRowEditorModel();
	}

	private void buildRowEditorModel() {
		rowModel = new RowEditorModel();

		for (int row = 0; row < parameters.size(); row++) {
			rowModel.addEditorForRow(row, parameters.get(row).rowEditor);
		}
	}

	private void buildParameters() {
		parameters = new ArrayList<ParameterEnvelope>();
		IParameterCollection collection = component.getParameters();
		Iterator<IParameter> it = collection.iterator();

		while (it.hasNext()) {
			parameters.add(new ParameterEnvelope(it.next()));
		}
	}

	/**
	 * Dohvaca row editor za JTableX
	 * 
	 * @return RowEditorModel
	 */
	public RowEditorModel getRowEditorModel() {
		return rowModel;
	}

	/**
	 * Dohvaca vrijednost parametra
	 * 
	 * @param row
	 *            redak (odnosi se na sami parametar)
	 * @param column
	 *            stupac (odnosi se ili na ime samog parametra, column==0, ili
	 *            na vrijednost parametra, column==1)
	 * @return vrijednost
	 * @throws IllegalArgumentException
	 *             u slucaju da se zeli pristupiti nepostojecem stupcu ili retku
	 *             tabele
	 */
	public String getValueAt(int row, int column) {
		if (column == 0) {
			return parameters.get(row).getParameterName();
		} else if (column == 1) {
			return parameters.get(row).getParameterValue();
		}

		throw new IllegalArgumentException("Pogresno konfigurirana tabela!");
	}

	/**
	 * Promjena vrijednosti odredjenog parametra
	 * 
	 * @param row
	 *            redak u tabeli
	 * @param column
	 *            stupac u tabeli (uvijek mora biti 1)
	 * @param value
	 *            nova vrijednost parametra
	 * @return true ako se parametar uspjesno promjenio, false inace
	 */
	public boolean setValueAt(int row, int column, Object value) {
		if (CPToolbar.DEBUG_MODE) {
			System.out.println("CPToolbarModel: setValueAt detected on row="
					+ row + ", columnt=" + column + ", newValue="
					+ value.toString());
		}

		Caseless objectName = component.getName();
		String parameterName = getValueAt(row, 0);
		Object newValue = null;

		return false;
	}

	/**
	 * Broj stupaca
	 * 
	 * @return uvijek vraca broj 2
	 */
	public int getNumberOfColumns() {
		return 2;
	}

	/**
	 * Broj redaka
	 * 
	 * @return vraca broj redaka u ovisnosti o broju parametara
	 */
	public int getNumberOfRows() {
		return parameters.size();
	}

	private Object getNewValue(Object value, int row) {
		EParamTypes pType;

		return null;
	}

	/**
	 * Generira sve potrebne dijelove za prikaz i upravljanje jednim parametrom
	 * 
	 * @author Garfield
	 * 
	 */
	private class ParameterEnvelope {

		/**
		 * Parametar za koji se izgradjuje ParameterEnvelope
		 */
		private IParameter parameter = null;

		/**
		 * Ako je isEnumerate true, onda je ovo !=null
		 */
		private JComboBox possibleValues = null;

		/**
		 * Ako je parametar pobrojan, ovo je true, u protivnom je false
		 */
		private boolean isEnumerate = false;

		/**
		 * Defaultni editor u JTableu za ovaj tip parametra
		 */
		private DefaultCellEditor rowEditor = null;

		public ParameterEnvelope(IParameter parameter) {
			this.parameter = parameter;
			buildRest();
		}

		private void buildRest() {
			IParameterConstraint constraint = parameter.getConstraint();
			EParamTypes pType = parameter.getType();
			Set<Object> constraintValues = constraint.getPossibleValues();

			if (CPToolbar.DEBUG_MODE) {
				System.out.println("ParameterEnvelope: parameterName="
						+ parameter.getName() + ", parameterType=" + pType
						+ ", constraintValues=" + constraintValues);
			}

			if (constraintValues != null) {
				buildComboBox(constraintValues);
				isEnumerate = true;
			} else {

				if (pType == EParamTypes.BOOLEAN) {
					buildComboBoxForBoolean(pType);
					isEnumerate = true;
				} else if (pType == EParamTypes.CASELESS) {

				} else if (pType == EParamTypes.TEXT) {

				} else if (pType == EParamTypes.INTEGER) {

				} else if (pType == EParamTypes.DECIMAL) {

				}
			}
		}

		private void buildComboBox(Set<Object> constraintValues) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: buildingComboBox for Object type");
			}

			possibleValues = new JComboBox();

			for (Object o : constraintValues) {
				possibleValues.addItem(o.toString());
				if (CPToolbar.DEBUG_MODE) {
					System.out
							.println("CPToolbarParameterEnvelopeCollection: comboBoxValue:"
									+ o.toString());
				}
			}

			rowEditor = new DefaultCellEditor(possibleValues);
		}

		private void buildComboBoxForBoolean(EParamTypes pType) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: buildingComboBox for Boolean type");
			}
			possibleValues = new JComboBox();
			possibleValues.addItem("true");
			possibleValues.addItem("false");

			rowEditor = new DefaultCellEditor(possibleValues);
		}

		/**
		 * Ime parametra
		 * 
		 * @return ime
		 */
		public String getParameterName() {
			return parameter.getName();
		}

		/**
		 * Dali je parametar pobrojen ili neogranicen
		 * 
		 * @return true ako je pobrojen, false inace
		 */
		public boolean isEnumerate() {
			return this.isEnumerate;
		}

		/**
		 * Dohvaca vrijednost parametra
		 * 
		 * @return vrijednost parametra
		 */
		public String getParameterValue() {
			return parameter.getValue().toString();
		}

		/**
		 * Dohvaca tip parametra
		 * 
		 * @return tip
		 */
		public EParamTypes getParameterType() {
			return parameter.getType();
		}

		/**
		 * Vraca cell editor za ovaj tip parametra
		 * 
		 * @return cell editor
		 */
		public AbstractCellEditor getRowEditor() {
			return rowEditor;
		}
	}
}
