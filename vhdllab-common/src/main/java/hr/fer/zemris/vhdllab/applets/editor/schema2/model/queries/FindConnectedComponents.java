package hr.fer.zemris.vhdllab.applets.editor.schema2.model.queries;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaWire;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.QueryResult;

import java.util.ArrayList;
import java.util.List;






/**
 * Nalazi komponente spojene na specificiranu zicu.
 * 
 * Vraca listu ISchemaComponent objekata pod kljucem
 * KEY_CONNECTED_COMPONENTS.
 * 
 * @author Axel
 *
 */
public class FindConnectedComponents implements IQuery {
	
	/* static fields */
	private static final List<EPropertyChange> DEPENDING_ON = new ArrayList<EPropertyChange>();
	public static final String KEY_CONNECTED_COMPONENTS = "connected_comps";
	
	static {
		DEPENDING_ON.add(EPropertyChange.CANVAS_CHANGE);
	}

	/* private fields */
	private Caseless wirename;

	/* ctors */
	
	public FindConnectedComponents(Caseless nameOfWire) {
		wirename = nameOfWire;
	}

	/* methods */
	
	@Override
	public List<EPropertyChange> getPropertyDependency() {
		return DEPENDING_ON;
	}

	@Override
	public String getQueryName() {
		return FindConnectedComponents.class.getSimpleName();
	}

	@Override
	public boolean isCacheable() {
		return true;
	}

	@Override
	public IQueryResult performQuery(ISchemaInfo info) {
		// first find wire
		ISchemaWire wire = info.getWires().fetchWire(wirename);
		if (wire == null) return new QueryResult(false);
		
		// now find components it's conneted to
		List<ISchemaComponent> connected = new ArrayList<ISchemaComponent>();
		for (PlacedComponent placed : info.getComponents()) {
			for (SchemaPort sp : placed.comp.getSchemaPorts()) {
				Caseless mappedto = sp.getMapping();
				if (Caseless.isNullOrEmpty(mappedto)) continue;
				if (mappedto.equals(wirename)) connected.add(placed.comp);
			}
		}
		
		return new QueryResult(KEY_CONNECTED_COMPONENTS, connected);
	}
	
}














