package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.enums.EParamTypes;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterConstraint;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;

import java.util.Set;

import javax.swing.JComboBox;

public class CPToolbarParameterEnvelopeCollection {

	/**
	 * Komponenta ciji se parametri obradjuju
	 */
	private ISchemaComponent component = null;

	public CPToolbarParameterEnvelopeCollection(ISchemaComponent component) {
		this.component = component;
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

		public ParameterEnvelope(IParameter parameter) {
			this.parameter = parameter;
			buildRest();
		}

		private void buildRest() {
			if (CPToolbar.DEBUG_MODE) {
				System.out.println("ParameterEnvelope: parameterName="
						+ parameter.getName());
			}

			IParameterConstraint constraint = parameter.getConstraint();
			Set<Object> constraintValues = constraint.getPossibleValues();
			if (constraintValues != null) {
				buildComboBox(constraintValues);
			}
		}

		private void buildComboBox(Set<Object> constraintValues) {
			possibleValues = new JComboBox();

			for (Object o : constraintValues) {
				possibleValues.addItem(o.toString());
			}
		}

		private void buildComboBox(EParamTypes pType) {

		}
	}
}
