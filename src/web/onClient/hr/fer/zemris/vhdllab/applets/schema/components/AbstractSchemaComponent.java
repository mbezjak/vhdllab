package hr.fer.zemris.vhdllab.applets.schema.components;

import hr.fer.zemris.vhdllab.applets.schema.components.properties.GenericProperty;
import hr.fer.zemris.vhdllab.applets.schema.components.properties.NoEditProperty;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;

import java.awt.Color;
import java.awt.Point;
import java.io.StringReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

import javax.swing.JTextField;

import org.apache.commons.digester.Digester;

/**
 * Smisao ove klase jest ponuditi funkcionalnost 
 * zajednicku svim sklopovima.
 * Primjerice, svaki sklop ima svojstvo Name. Ova komponenta to
 * svojstvo stavlja na listu svojstava. Svaka 
 * naslijedena komponenta potom zove: 
 * super.addToPropertyList()
 * prije dodavanja vlastitih svojstava kao sto su broj
 * ulaza, izlaza, itd.
 * @author Axel
 *
 */
public abstract class AbstractSchemaComponent implements ISchemaComponent {	
	protected LinkedList<AbstractSchemaPort> portlist;
	protected Ptr<Object> pComponentName;
	protected int componentDelay;
	public String getComponentName() {
		return (String)pComponentName.val;
	}
	public void setComponentName(String name) {
		pComponentName = new Ptr<Object>();
		pComponentName.val = name;
	}
	
	
	
	
	
	protected static HashSet<String> instanceNameSet;
	protected static long counter;
	static {
		instanceNameSet = new HashSet();
	}
	public static void clearInstanceNameSet() {
		if (instanceNameSet != null) instanceNameSet.clear();
	}
	
	protected Ptr<Object> pComponentInstanceName;
	public String getComponentInstanceName() {
		return (String)pComponentInstanceName.val;
	}
	public void setComponentInstanceName(String name) {
		if (name.equals("") || (name.charAt(0) >= '0' && name.charAt(0) <= '9')) name = '_' + name;
		name = name.replaceAll("[^a-zA-Z0-9_]", "_");
		if (name == pComponentInstanceName.val) return;
		System.out.println("Postavljam ime na: " + name);
		if (isInstanceNameInvalid(name)) {
			name = generateNewInstanceName(name);
		} 
		instanceNameSet.remove(this.pComponentInstanceName.val);
		this.pComponentInstanceName.val = name;
		instanceNameSet.add(name);
	}
	static protected boolean isInstanceNameInvalid(String name) {
		if (instanceNameSet.contains(name)) return true;
		if (name.length() == 0) return true;
		return false;
	}
	static protected String generateNewInstanceName(String input) {
		counter++;
		if (input.length() > 5) {
			input = input.substring(0, input.length() - 2) + counter;
		} else {
			input = "Sklop_" + counter;
		}
		return input;
	}
	
	
	
	
	
	
	protected AbstractSchemaPort.PortOrientation smjer;
	
	private boolean isDrawingPorts;
	private boolean isDrawingFrame;
	private boolean isDrawingName;
	
	public void clearPortList() {
		portlist.clear();
	}
	public void addPort(AbstractSchemaPort port) {
		portlist.add(port);
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#getNumberOfPorts()
	 */
	public int getNumberOfPorts() {
		return portlist.size();
	}
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.components.ISchemaComponent#getSchemaPort(int)
	 */
	public AbstractSchemaPort getSchemaPort(int portIndex) {
		try {
			return portlist.get(portIndex);
		} catch (Exception e) {
			return null;
		}
	}
	
	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.schema.ISchemaComponent#getPropertyList()
	 */
	public ComponentPropertyList getPropertyList() {
		ComponentPropertyList cplist = new ComponentPropertyList();
		addPropertiesToComponentPropertyList(cplist);
		return cplist;
	}
	
	/**
	 * Konstruktor.
	 *
	 */
	public AbstractSchemaComponent(String instanceName) {
		portlist = new LinkedList<AbstractSchemaPort>();
		pComponentInstanceName = new Ptr<Object>();
		setComponentInstanceName(instanceName);
		smjer = AbstractSchemaPort.PortOrientation.WEST;
		isDrawingPorts = true;
		isDrawingFrame = true;
		isDrawingName = true;
		componentDelay = 10;
	}
	
	

	/**
	 * Adds properties to the component property list.
	 * Inherited classes override this method, but call this
	 * method from their ancestors to get a complete property
	 * list.
	 * @param cplist Property list.
	 */
	protected void addPropertiesToComponentPropertyList(ComponentPropertyList cplist) {
//		Ptr<Object> pcn = new Ptr<Object>();
//		pcn.val = componentName;
//		Ptr<Object> pcin = new Ptr<Object>();
//		pcin.val = componentInstanceName;
		cplist.add(new NoEditProperty("Ime sklopa", pComponentName));
		cplist.add(new GenericProperty("Ime instance sklopa", new Ptr<Object>(this)) {
			@Override
			public void onUpdate(JTextField tf) {
				AbstractSchemaComponent comp = (AbstractSchemaComponent) getSklopPtr().val;
				comp.setComponentInstanceName(tf.getText());
				tf.setText(comp.getComponentInstanceName());
			}

			@Override
			public void onLoad(JTextField tf) {
				AbstractSchemaComponent comp = (AbstractSchemaComponent) getSklopPtr().val;
				tf.setText(comp.getComponentInstanceName());
			}
		});
		// o tom potom
//		cplist.add(new GenericComboProperty("Orijentacija", new Ptr<Object>(this)) {
//			@Override
//			public void onUpdate(JComboBox cbox) {
//				AbstractSchemaPort.PortOrientation po = AbstractSchemaPort.PortOrientation.NORTH;
//				int i = cbox.getSelectedIndex();
//				if (i == 3) po = AbstractSchemaPort.PortOrientation.WEST;
//				if (i == 2) po = AbstractSchemaPort.PortOrientation.EAST;
//				if (i == 1) po = AbstractSchemaPort.PortOrientation.SOUTH;
//				cbox.removeAllItems();
//				if (i == -1) {
//					po = ((AbstractSchemaComponent)getSklopPtr().val).getSmjer();
//					i = smToInt(po);
//				}
//				cbox.insertItemAt("W", 0);
//				cbox.insertItemAt("E", 0);
//				cbox.insertItemAt("S", 0);
//				cbox.insertItemAt("N", 0);
//				cbox.setSelectedIndex(i);
//				((AbstractSchemaComponent)getSklopPtr().val).setSmjer(po);
//			}
//		});
		cplist.add(new GenericProperty("Kasnjenje sklopa", new Ptr<Object>(this)) {
			@Override
			public void onUpdate(JTextField tf) {
				Integer i = null;
				try {
					i = Integer.parseInt(tf.getText());
				}
				catch (Exception e) {
					i = ((AbstractSchemaComponent)getSklopPtr().val).getComponentDelay();
				}
				if (i < 0) {
					i = ((AbstractSchemaComponent)getSklopPtr().val).getComponentDelay();
					tf.setText(i.toString());
					return;
				}
				((AbstractSchemaComponent)getSklopPtr().val).setComponentDelay(i);				
			}

			@Override
			public void onLoad(JTextField tf) {
				tf.setText(((AbstractSchemaComponent)getSklopPtr().val).getComponentDelay() + "");	
			}
		});
	}
	
	/**
	 * Za brzo dobivanje obratnog usmjerenja.
	 */
	protected AbstractSchemaPort.PortOrientation reverseOrient(AbstractSchemaPort.PortOrientation or) {
		if (or == AbstractSchemaPort.PortOrientation.WEST) or = AbstractSchemaPort.PortOrientation.EAST;
		if (or == AbstractSchemaPort.PortOrientation.EAST) or = AbstractSchemaPort.PortOrientation.WEST;
		if (or == AbstractSchemaPort.PortOrientation.SOUTH) or = AbstractSchemaPort.PortOrientation.NORTH;
		if (or == AbstractSchemaPort.PortOrientation.NORTH) or = AbstractSchemaPort.PortOrientation.SOUTH;
		return or;
	}
	
	/**
	 * Za brzo izracunavanje indeksa smjera.
	 */
	static protected int smToInt(AbstractSchemaPort.PortOrientation or) {
		if (or == AbstractSchemaPort.PortOrientation.WEST) return 3;
		if (or == AbstractSchemaPort.PortOrientation.EAST) return 2;
		if (or == AbstractSchemaPort.PortOrientation.SOUTH) return 1;
		if (or == AbstractSchemaPort.PortOrientation.NORTH) return 0;
		return 0;
	}
	
	/**
	 * Za brzo izracunavanje smjera iz indeksa.
	 */
	static protected AbstractSchemaPort.PortOrientation intToSm(int i) {
		AbstractSchemaPort.PortOrientation or = AbstractSchemaPort.PortOrientation.NORTH;
		if (i == 2) or = AbstractSchemaPort.PortOrientation.EAST;
		if (i == 3) or = AbstractSchemaPort.PortOrientation.WEST;
		if (i == 0) or = AbstractSchemaPort.PortOrientation.NORTH;
		if (i == 1) or = AbstractSchemaPort.PortOrientation.SOUTH;
		return or;
	}
	
	
	/**
	 * @return Returns the smjer.
	 */
	public AbstractSchemaPort.PortOrientation getSmjer() {
		return smjer;
	}
	/**
	 * @param smjer The smjer to set.
	 */
	public void setSmjer(AbstractSchemaPort.PortOrientation smjer) {
		System.out.println("Postavljam smjer na: " + smjer);
		this.smjer = smjer;
	}
	public void setSmjerWithInt(String smj) {
		System.out.println("Postavljam smjer na: " + smj);
		int indSmj = Integer.parseInt(smj);
		this.smjer = intToSm(indSmj);
	}

	public static long getCounter() {
		return counter;
	}
	public static void setCounter(long counter) {
		AbstractSchemaComponent.counter = counter;
	}
	
	/**
	 * Updateanje koordinata portova.
	 */
	protected abstract void updatePortCoordinates();
	
	/**
	 * Virtualni konstruktor.
	 */
	public abstract AbstractSchemaComponent vCtr();
	
	/**
	 * Ovu verziju draw mora zvati metoda draw iz naslijedene klase.
	 * Inace se nece iscrtati tockice koje oznacavaju same portove.
	 */
	public void draw(SchemaDrawingAdapter adapter) {
		if (isDrawingFrame) adapter.drawRect(0, 0, getComponentWidthSpecific(), getComponentHeightSpecific(), Color.BLUE);
		drawSpecific(adapter);
		if (isDrawingPorts) for (AbstractSchemaPort port : portlist) {
			if (port.getTipPorta() == "_boxing") continue;
			Point p = port.getCoordinate();
			adapter.drawCursorPoint(p.x, p.y, null);
		}
		if (isDrawingName) adapter.drawString((String) pComponentInstanceName.val, 0, 1);
	}
	
	public abstract void drawSpecific(SchemaDrawingAdapter adapter);
	
	public abstract boolean isToBeSerialized();
	
	public abstract boolean isToBeMapped();
	
	public abstract boolean hasComponentBlock();
	
	public abstract String getEntityBlock();
	
	public abstract String getMapping(Map<Integer, String> signalList);
	
	public abstract String getAdditionalSignals();
	
	/**
	 * 
	 * @param serial
	 * @return
	 */
	public void drawEssential(SchemaDrawingAdapter adapter) {
		isDrawingPorts = false;
		isDrawingFrame = false;
		isDrawingName = false;
		draw(adapter);
		isDrawingPorts = true;
		isDrawingFrame = true;
		isDrawingName = true;
	}
	
	protected abstract boolean deserializeComponentSpecific(String serial);
	
	protected abstract String serializeComponentSpecific();
	
	public void postaviOpcenito(String ime, String ori, String del, String componentSpecific) {
		setComponentInstanceName(ime);
		setSmjerWithInt(ori);
		try {
			setComponentDelay(Integer.parseInt(del));
		} catch (Exception e) {
			e.printStackTrace();
		}
		deserializeComponentSpecific(componentSpecific);
	}
	
	public boolean deserializeComponent(String serial) {
		System.out.println(serial);
		
		Digester digester=new Digester();
		
		digester.push(this);
		
		// prvo deserijaliziraj opcenite stvari
		digester.addCallMethod("komponenta", "postaviOpcenito", 4);
		digester.addCallParam("komponenta/imeInstanceKomponente", 0);
		digester.addCallParam("komponenta/orijentacija", 1);
		digester.addCallParam("komponenta/kasnjenje", 2);
		// deserijaliziraj component-specific stvari
		digester.addCallParam("komponenta/componentSpecific", 3);
		
		try {
			digester.parse(new StringReader(serial));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String serializeComponent() {
		// serijaliziraj opcenite stvari
		StringBuilder builder = new StringBuilder("<komponenta>");
		builder.append("<imeInstanceKomponente>").append(pComponentInstanceName.val).append("</imeInstanceKomponente>");
		builder.append("<orijentacija>").append(smToInt(smjer)).append("</orijentacija>");
		builder.append("<kasnjenje>").append(componentDelay).append("</kasnjenje>");
		
		// serijaliziraj component-specific stvari
		builder.append("<componentSpecific>").append(serializeComponentSpecific()).append("</componentSpecific>");
		
		// serijaliziraj portove
		//		builder.append("<portovi>");
		//		int i = 0;
		//		for (AbstractSchemaPort port : portlist) {
		//			builder.append("<port>");
		//			builder.append("<portNumber>").append(i).append("</portNumber>");
		//			builder.append("<portInfo>").append(port.serialize()).append("</portInfo>");
		//			builder.append("</port>");
		//		}
		//		builder.append("</portovi>");
		
		builder.append("</komponenta>");
		
		return builder.toString();
	}
	public boolean isDrawingPorts() {
		return isDrawingPorts;
	}
	public void setDrawingPorts(boolean isDrawingPorts) {
		this.isDrawingPorts = isDrawingPorts;
	}
	public boolean isDrawingFrame() {
		return isDrawingFrame;
	}
	public void setDrawingFrame(boolean isDrawingFrame) {
		this.isDrawingFrame = isDrawingFrame;
	}
	public boolean isDrawingName() {
		return isDrawingName;
	}
	public void setDrawingName(boolean isDrawingName) {
		this.isDrawingName = isDrawingName;
	}
	public int getComponentDelay() {
		return componentDelay;
	}
	public void setComponentDelay(int componentDelay) {
		this.componentDelay = componentDelay;
	}
	
	public int getComponentWidth() {
		return getComponentWidthSpecific();
	}
	
	public int getComponentHeight() {
		return getComponentHeightSpecific();
	}
}




