package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;
import hr.fer.zemris.vhdllab.applets.schema.components.SchemaPort;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class PortProperty extends AbstractComponentProperty {
	private PortField portfld;
	
	public PortProperty(String nm, Ptr<Object> p) {
		super(nm, p);
		portfld = new PortField(p);
	}
	
	class PortField extends AbstractPropField {
		Ptr<Object> pSklop;
		JTextField tfield;
		
		public PortField(Ptr<Object> pSklop) {
			this.pSklop = pSklop;
			tfield = new JTextField("" + ((AbstractSchemaComponent)pSklop.val).getNumberOfPorts());
			//System.out.println(((AbstractSchemaComponent)pSklop.val).getNumberOfPorts());
			tfield.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					updateProperty();
				}
			});
		}

		/* (non-Javadoc)
		 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractPropField#getComponent()
		 */
		@Override
		public Component getComponent() {
			return tfield;
		}

		/* (non-Javadoc)
		 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractPropField#updateProperty()
		 */
		@Override
		public void updateProperty() {
			Integer oldval = ((AbstractSchemaComponent)pSklop.val).getNumberOfPorts();
			Integer newval = null;
			try {
				newval = Integer.parseInt(tfield.getText());
			} catch (Exception e) {
				tfield.setText(oldval.toString());
				return;
			}
			((AbstractSchemaComponent)pSklop.val).clearPortList();
			for (int i = 0; i < newval; i++) {
				((AbstractSchemaComponent)pSklop.val).addPort(new SchemaPort());
			}
		}
		
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.properties.NumProperty#getPropField()
	 */
	@Override
	public AbstractPropField getPropField() {
		return portfld;
	}
}