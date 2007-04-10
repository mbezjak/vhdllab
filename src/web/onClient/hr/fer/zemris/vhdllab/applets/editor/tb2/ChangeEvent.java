package hr.fer.zemris.vhdllab.applets.editor.tb2;

/**
 *This class represents a change event for a signal. It consists of an integer representing change 
 *event occurance a string representing signals value after the change and an integer representing 
 *messure units. 
 * @author ddelac
 *
 */
public class ChangeEvent implements Comparable {
	/**
	 * constants for units:
	 * TODO:Add others
	 */
	public static final int NANOSEC=1;
	
	
	private int units;
	private int moment;
	private String value;
	
	public ChangeEvent(int moment, int units, String value) {
		super();
		this.moment=moment;
		this.units=units;
		this.value=value;
	}
	
	//*******************************GETTERS & SETTERS***************************
	public int getMoment() {
		return moment;
	}
	public int getUnits() {
		return units;
	}
	public String getValue() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		ChangeEvent ev=null;
		
		if(o instanceof ChangeEvent) ev=(ChangeEvent) o;
		return this.moment==ev.getMoment()*ev.getUnits()/this.units;
	}
	
	public int compareTo(Object o) {
		ChangeEvent ev=null;
		
		if(o instanceof ChangeEvent) ev=(ChangeEvent) o;
		
		return (this.moment*this.units-ev.getMoment()*ev.getUnits())*(-1);
	}
}
