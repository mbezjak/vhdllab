package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.components.properties.NumProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.TextProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Point;

public class PrivremeniProbniSklop extends AbstractSchemaComponent {
	private String maliTekst;
	private Integer mojInt;
	
	public PrivremeniProbniSklop() {
		mojInt = 15;
		maliTekst = "Tek tolko da nest pise.";
		portlist.add(new SchemaPort());
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.AbstractSchemaComponent#addPropertiesToComponentPropertyList(hr.fer.zemris.vhdllab.applets.schema.components.ComponentPropertyList)
	 */
	@Override
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
		super.addPropertiesToComponentPropertyList(cplist);
		cplist.add(new TextProperty("Tekstic", new Ptr<Object>(maliTekst)));
		cplist.add(new NumProperty("Brojcek", new Ptr<Object>(mojInt)));
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
