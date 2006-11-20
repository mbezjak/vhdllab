package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractPropField;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NumField;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NumProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.PortProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.TextProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class PrivremeniProbniSklop extends AbstractSchemaComponent {
	private Ptr<Object> pMaliTekst = new Ptr<Object>();
	private Ptr<Object> pMojInt = new Ptr<Object>();
	
	public PrivremeniProbniSklop() {
		pComponentName.val = "Privremeni probni sklop";
		pMojInt.val = 2;
		pMaliTekst.val = "Tek tolko da nest pise.";
		portlist.add(new SchemaPort());
		portlist.add(new SchemaPort());
	}
	
	

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
		cplist.add(new TextProperty("Tekstic", pMaliTekst));
		cplist.add(new NumProperty("Brojcek", pMojInt));
		cplist.add(new PortProperty("Broj portova", new Ptr<Object>(this)));
		int i = 0;
		for (AbstractSchemaPort port : portlist) {
			cplist.add(new TextProperty("Ime porta br. " + i, port.getNamePtr()));
			i++;
		}
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.SchemaAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		adapter.drawRect(0, 0, 100, 100);
	}
}
