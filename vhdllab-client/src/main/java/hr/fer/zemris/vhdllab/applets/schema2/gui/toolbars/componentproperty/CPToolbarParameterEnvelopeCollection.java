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
package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.ETimeMetrics;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.TimeFormatException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ILocalGuiController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Time;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.SetParameterCommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands.SetParameterCommand.EParameterHolder;
import hr.fer.zemris.vhdllab.applets.schema2.gui.canvas.CanvasToolbarLocalGUIController;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.SwingComponent.RowEditorModel;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.customTableCellEditors.ObjectCellEditor;
import hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty.customTableCellEditors.TimeCellEditor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.TableCellEditor;

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
	private Caseless componentName = null;

	/**
	 * Lista parametara spremnih za prikaz u JTable
	 */
	private List<ParameterEnvelope> parameters = null;

	/**
	 * Row model za JTableX
	 */
	private RowEditorModel rowModel = null;

	/**
	 * Controller
	 */
	private ISchemaController controller = null;

	/**
	 * Parameter holder
	 */
	private EParameterHolder parameterHolder = null;

	/**
	 * Local gui controller
	 */
	private ILocalGuiController lgc = null;

	/**
	 * Konstruktor
	 * 
	 * @param component
	 *            komponenta
	 * @param controller
	 *            kontroler
	 * @param lgc
	 */
	public CPToolbarParameterEnvelopeCollection(ISchemaComponent component,
			ISchemaController controller, ILocalGuiController lgc) {
		this.controller = controller;
		this.componentName = component.getName();
		this.lgc = lgc;

		parameterHolder = EParameterHolder.component;
		buildParameters(component.getParameters());
		buildRowEditorModel();
	}

	/**
	 * Konstruktor
	 * 
	 * @param wire
	 *            zica
	 * @param controller
	 *            kontroler
	 */
	public CPToolbarParameterEnvelopeCollection(ISchemaWire wire,
			ISchemaController controller, ILocalGuiController lgc) {
		this.controller = controller;
		this.componentName = wire.getName();
		this.lgc = lgc;

		parameterHolder = EParameterHolder.wire;
		buildParameters(wire.getParameters());
		buildRowEditorModel();
	}

	public CPToolbarParameterEnvelopeCollection(ISchemaEntity entity,
			ISchemaController controller, ILocalGuiController lgc) {
		this.controller = controller;
		this.componentName = entity.getName();
		this.lgc = lgc;

		parameterHolder = EParameterHolder.entity;
		buildParameters(entity.getParameters());
		buildRowEditorModel();
	}

	/**
	 * Editori celija u tabeli
	 */
	private void buildRowEditorModel() {
		rowModel = new RowEditorModel();

		for (int row = 0; row < parameters.size(); row++) {
			rowModel.addEditorForRow(row, parameters.get(row).rowEditor);
		}
	}

	private void buildParameters(IParameterCollection collection) {
		parameters = new ArrayList<ParameterEnvelope>();
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
			System.out.println("---");
		}
		String parameterName = getValueAt(row, 0);
		Object newValue = null;

		if (CPToolbar.DEBUG_MODE) {
			System.out
					.println("CPToolbarParameterEnvelopeCollection: setValueAt detected on row="
							+ row
							+ ", column="
							+ column
							+ ", newValue="
							+ value.toString());
			System.out
					.println("CPToolbarParameterEnvelopeCollection: objectName="
							+ componentName.toString()
							+ ", parameterName="
							+ parameterName);
		}
		newValue = getNewValue(value, row);

		// bypass sending command if newValue is null
		if (newValue == null) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: bypassing sending command; reason=invalid or null valued input");
			}
			return false;
		} else if (newValue.equals(getValueAt(row, 1))) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: new value is equal to old value - no action!");
			}
			return false;
		}

		ICommand command = new SetParameterCommand(componentName,
				parameterName, parameterHolder, newValue, controller
						.getSchemaInfo());
		ICommandResponse response = controller.send(command);

		if (response.isSuccessful()) {
			Caseless cName = (Caseless) response.getInfoMap().get(
					SetParameterCommand.KEY_UPDATED_NAME);
			if (!componentName.equals(cName)) {
				int pHolder = CanvasToolbarLocalGUIController.TYPE_NOTHING_SELECTED;

				if (parameterHolder.equals(EParameterHolder.wire)) {
					pHolder = CanvasToolbarLocalGUIController.TYPE_WIRE;
				} else if (parameterHolder.equals(EParameterHolder.component)) {
					pHolder = CanvasToolbarLocalGUIController.TYPE_COMPONENT;
				}
				lgc.setSelectedComponent(cName, pHolder);
			}
		}

		if (CPToolbar.DEBUG_MODE) {
			System.out
					.println("CPToolbarParameterEnvelopeCollection: commandResponseIsSuccessful="
							+ response.isSuccessful());
			if (!response.isSuccessful()) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: commandResponseExplain="
								+ response.getError().getMessage());
			}
		}

		return response.isSuccessful();
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
		EParamTypes pType = parameters.get(row).getParameterType();

		if (pType == EParamTypes.BOOLEAN) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=BOOLEAN");
			}
			return Boolean.valueOf(value.toString());
		} else if (pType == EParamTypes.CASELESS) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=CASELESS");
			}
			return new Caseless(value.toString());
		} else if (pType == EParamTypes.TEXT) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=TEXT");
			}
			return String.valueOf(value.toString());
		} else if (pType == EParamTypes.INTEGER) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=INTEGER");
			}
			int broj = 0;
			try {
				broj = Integer.parseInt(value.toString());
			} catch (NumberFormatException e) {
				return null;
			}
			return broj;
		} else if (pType == EParamTypes.DECIMAL) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=DECIMAL");
			}
			return Double.parseDouble(value.toString());
		} else if (pType == EParamTypes.TIME) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=TIME");
			}
			Time newTime = null;

			try {
				newTime = Time.parseTime(value.toString());
			} catch (TimeFormatException e) {
			}

			return newTime;
		} else if (pType == EParamTypes.OBJECT) {
			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: parameterType=OBJECT");
			}
			return parameters.get(row).getParameterValueForObject(
					(String) value);

		}

		throw new IllegalArgumentException("Nepoznati tip parametra!");
	}

	/**
	 * Generira sve potrebne dijelove za prikaz i upravljanje jednim parametrom
	 * 
	 * @author Garfield
	 * 
	 */
	private static class ParameterEnvelope {

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
		 * Za parametre tipa OBJECT potrebna je struktura koja pamti koji objekt
		 * se nalazi pod kojim stringom. <parameterName>,<paramValue,paramObject>
		 */
		private Map<String, Object> containerForObjectParameters = null;

		/**
		 * Defaultni editor u JTableu za ovaj tip parametra
		 */
		private TableCellEditor rowEditor = null;

		/**
		 * Konstruktor
		 * 
		 * @param parameter
		 *            parametar
		 */
		public ParameterEnvelope(IParameter parameter) {
			this.parameter = parameter;
			containerForObjectParameters = new HashMap<String, Object>();
			buildRest();
		}

		private void buildRest() {
			IParameterConstraint constraint = parameter.getConstraint();
			EParamTypes pType = parameter.getType();
			Set<Object> constraintValues = constraint.getPossibleValues();

			if (CPToolbar.DEBUG_MODE) {
				System.out.println("ParameterEnvelope: parameterName="
						+ parameter.getName() + ", parameterValue="
						+ parameter.getValue() + ", parameterType=" + pType
						+ ", constraintValues=" + constraintValues);
			}
			if (constraintValues != null) {
				buildComboBox(constraintValues, parameter.getName());
				isEnumerate = true;
			} else {

				if (pType == EParamTypes.BOOLEAN) {
					buildComboBoxForBoolean(pType);
					isEnumerate = true;
				} else if (pType == EParamTypes.OBJECT) {
					// TODO Napraviti podrsku za object tipove parametara
					rowEditor = new ObjectCellEditor(parameter.getValue());
				} else if (pType == EParamTypes.TIME) {
					rowEditor = new TimeCellEditor(ETimeMetrics.getAllTimes());
				}
			}
		}

		private void buildComboBox(Set<Object> constraintValues,
				String paramName) {

			if (CPToolbar.DEBUG_MODE) {
				System.out
						.println("CPToolbarParameterEnvelopeCollection: buildingComboBox for Object type");
			}

			possibleValues = new JComboBox();

			for (Object o : constraintValues) {
				possibleValues.addItem(o.toString());
				containerForObjectParameters.put(o.toString(), o);

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
		 * Dohvaca vrijednost OBJECT parametra
		 * 
		 * @param value
		 *            kljuc pod kojim se nalazi OBJECT
		 * @return OBJECT
		 */
		public Object getParameterValueForObject(String value) {

			return containerForObjectParameters.get(value);
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
		public TableCellEditor getRowEditor() {
			return rowEditor;
		}
	}
}
