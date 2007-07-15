package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class CPToolbarParameterEnvelopeCollection {

	/**
	 * Komponenta ciji se parametri obradjuju
	 */
	private ISchemaComponent component = null;

	/**
	 * Lista parametara spremnih za prikaz u JTable
	 */
	private List<ParameterEnvelope> parameters = null;

	public CPToolbarParameterEnvelopeCollection(ISchemaComponent component) {
		this.component = component;

		buildParameters();
	}

	private void buildParameters() {
		parameters = new ArrayList<ParameterEnvelope>();
		IParameterCollection collection = component.getParameters();
		Iterator<IParameter> it = collection.iterator();

		while (it.hasNext()) {
			parameters.add(new ParameterEnvelope(it.next()));
		}
	}

	public String getValueAt(int row, int column) {
		if (column == 0) {
			return parameters.get(row).getParameterName();
		} else if (column == 1) {
			return parameters.get(row).getParameterValue();
		}

		throw new IllegalArgumentException("Pogresno konfigurirana tabela!");
	}

	public int getNumberOfColumns() {
		return 2;
	}

	public int getNumberOfRows() {
		return parameters.size();
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
				}
			}
		}

		private void buildComboBox(Set<Object> constraintValues) {
			possibleValues = new JComboBox();

			for (Object o : constraintValues) {
				possibleValues.addItem(o.toString());
			}

			rowEditor = new DefaultCellEditor(possibleValues);
		}

		private void buildComboBoxForBoolean(EParamTypes pType) {
			possibleValues.addItem("true");
			possibleValues.addItem("false");

			rowEditor = new DefaultCellEditor(possibleValues);
		}

		public String getParameterName() {
			return parameter.getName();
		}

		public boolean isEnumerate() {
			return this.isEnumerate;
		}

		public String getParameterValue() {
			return parameter.getValueAsString();
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
