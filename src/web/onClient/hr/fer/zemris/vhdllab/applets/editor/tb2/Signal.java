package hr.fer.zemris.vhdllab.applets.editor.tb2;

import java.util.Set;
import java.util.TreeSet;


/**
 * This class represents one signal in a list of signals. A signal has several characteristics:
 * name, rangeFrom, rangeTo, type and signal data. Signal data is a set of signal change events
 * used to draw a signal on a canvas.
 * @author ddelac
 *
 */
public class Signal {
	private String name;
	
	/**
	 *Type values: 	0 for std_logic
	 *				1 for std_logic_vector 
	 */
	private int type;
	
	private int rangeFrom;
	private int rangeTo;
	
	/**
	 * A flag telling if this signal should be displayed expanded
	 */
	private boolean expanded;
	
	/**
	 * Implemented as a TreeSet and used to store all Change events for this signal in sorted order
	 * by the moment of occurance.
	 */
	private Set<ChangeEvent> data;
	
	
	public Signal(String name, int type, int rangeFrom, int rangeTo) {
		super();
		
		data=new TreeSet<ChangeEvent>();
		
		this.name=name;
		this.type=type;
		this.rangeFrom=rangeFrom;
		this.rangeTo=rangeTo;
	}
	
	public void addChangeEvent(ChangeEvent ev){
		data.add(ev);
		balanceData();
	};
	
	
	private void balanceData() {
		// TODO balance the data to remove unused change events!!!
		
	}

	//**********************GETTERS & SETTERS*******************************
	public Set<ChangeEvent> getData() {
		return data;
	}
	public void setData(Set<ChangeEvent> data) {
		this.data = data;
	}
	public String getName() {
		return name;
	}
	public int getRangeFrom() {
		return rangeFrom;
	}
	public int getRangeTo() {
		return rangeTo;
	}
	public int getType() {
		return type;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}
}
