package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.components.basics.Sklop_MUX2nNA1;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.AbstractComponentProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.GenericProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NumProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.PortProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.TextProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import javax.swing.JTextField;

public class PrivremeniProbniSklop extends AbstractSchemaComponent {
	private Ptr<Object> pMaliTekst = new Ptr<Object>();
	private Ptr<Object> pMojInt = new Ptr<Object>();
	
	public PrivremeniProbniSklop(String ime) {
		super(ime);
		pComponentName.val = "Privremeni probni sklop";
		pMojInt.val = 2;
		pMaliTekst.val = "Mihalj sam ja, onaj, Herpes!";
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
		AbstractComponentProperty pro = new GenericProperty("Svojstvo vise sile.", new Ptr<Object>(this)) {
			@Override
			public void onUpdate(JTextField tf) {
				((PrivremeniProbniSklop)this.getSklopPtr().val).setComponentName("Zika!");
			}
		};
		cplist.add(pro);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#draw(hr.fer.zemris.vhdllab.applets.schema.SchemaAdapter)
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		adapter.drawRect(0, 0, 100, 100);
	}



	@Override
	protected void updatePortCoordinates() {
		// TODO Auto-generated method stub
		
	}



	public int getComponentWidth() {
		// TODO Auto-generated method stub
		return 0;
	}



	public int getComponentHeight() {
		// TODO Auto-generated method stub
		return 0;
	}



	@Override
	public AbstractSchemaComponent vCtr() {
		return new PrivremeniProbniSklop((String) pComponentInstanceName.val);
	}
}
