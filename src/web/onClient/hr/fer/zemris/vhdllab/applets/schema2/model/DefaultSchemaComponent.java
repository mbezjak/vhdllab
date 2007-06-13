package hr.fer.zemris.vhdllab.applets.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.Parameter;
import hr.fer.zemris.vhdllab.applets.editor.schema2.predefined.beans.PredefinedComponent;
import hr.fer.zemris.vhdllab.applets.schema2.enums.EOrientation;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameterCollection;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IVHDLSegmentProvider;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.SchemaPort;
import hr.fer.zemris.vhdllab.applets.schema2.model.drawers.DefaultComponentDrawer;
import hr.fer.zemris.vhdllab.applets.schema2.model.parameters.ParameterFactory;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;



public class DefaultSchemaComponent implements ISchemaComponent {
	private static final int WIDTH_PER_PORT = 30;
	private static final int HEIGHT_PER_PORT = 30;
	
	
	/* private fields */
	private String componentName;
	private String codeFileName;
	private String categoryName;
	private boolean generic;
	private IComponentDrawer drawer;
	private IParameterCollection parameters;
	
	
	/* ctors */
	
	/** 
	 * @param name
	 * Jedinstveno ime ove instance komponente.
	 * @param predefComp
	 * Wrapper za predefinirane komponente.
	 */
	public DefaultSchemaComponent(String name, PredefinedComponent predefComp) {
		// basic properties
		componentName = predefComp.getComponentName();
		codeFileName = predefComp.getCodeFileName();
		categoryName = predefComp.getCategoryName();
		generic = predefComp.isGenericComponent();
		
		// drawer
		initDrawer(predefComp);
		
		// parameters
		parameters = new SchemaParameterCollection();
		Set<Parameter> params = predefComp.getParameters();
		if (params != null) {
			ParameterFactory parfactory = new ParameterFactory();
			IParameter par;
			for (Parameter parwrap : params) {
				par = parfactory.createParameter(parwrap);
				parameters.addParameter(par.getName(), par);
			}
		}
		
		// ports
		//Set<Port> ports = predefComp.getPorts();
	}
	
	
	
	
	/* methods */
	
	private void initDrawer(PredefinedComponent predefComp) {
		try {
			Class cls = Class.forName(predefComp.getDrawerName());
			Class[] partypes = new Class[1];
			partypes[0] = ISchemaComponent.class;
			Constructor<IComponentDrawer> ct = cls.getConstructor(partypes);
			drawer = ct.newInstance();
		} catch (ClassNotFoundException cnfe) {
			drawer = new DefaultComponentDrawer(this);
		} catch (NoSuchMethodException nsme) {
			drawer = new DefaultComponentDrawer(this);
		} catch (IllegalArgumentException e) {
			drawer = new DefaultComponentDrawer(this);
		} catch (InstantiationException e) {
			drawer = new DefaultComponentDrawer(this);
		} catch (IllegalAccessException e) {
			drawer = new DefaultComponentDrawer(this);
		} catch (InvocationTargetException e) {
			drawer = new DefaultComponentDrawer(this);
		}
	}







	public ISchemaComponent copyCtor() {
		// TODO Auto-generated method stub
		throw new NotImplementedException();
	}
	
	public String getCategoryName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public CircuitInterface getCircuitInterface() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public EOrientation getComponentOrientation() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IComponentDrawer getDrawer() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getHeight() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Caseless getName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IParameterCollection getParameters() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<SchemaPort> getPorts() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public SchemaPort getSchemaPort(int xoffset, int yoffset, int dist) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public SchemaPort getSchemaPort(int index) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Caseless getTypeName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public IVHDLSegmentProvider getVHDLSegmentProvider() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int getWidth() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int portCount() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void setComponentOrientation(EOrientation orient) {
		// TODO Auto-generated method stub
		
	}
	
	public void setName(Caseless name) {
		// TODO Auto-generated method stub
		
	}
	
	public void deserialize(String code) {
		// TODO Auto-generated method stub
		
	}
	
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
