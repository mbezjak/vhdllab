package hr.fer.zemris.vhdllab.applets.editor.newtb.model;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.SignalChangeListener;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.TestbenchListener;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.EditableSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.Signal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Davor Jurisic
 *
 */
public abstract class Testbench implements Iterable<Signal>, SignalChangeListener, TestbenchModel {
	
	protected String sourceName;
	protected long testBenchLength;
	protected TimeScale timeScale;
	protected Map<String, Signal> signalMap;
	protected long simulationLength;
	
	private List<TestbenchListener> listeners = null;
	private Set<Signal> suspendedChangedSignals = null;
	private boolean signalChangedEventsSuspended;
	
	public Testbench(String sourceName, long testBenchLength, TimeScale timeScale) throws UniformTestbenchException {
		if(sourceName == null || sourceName.length() == 0)
			throw new UniformTestbenchException("SourceName ne smije biti null ili duljine 0.");
		if(testBenchLength <= 0)
			throw new UniformTestbenchException("testBenchLength mora biti veci ili jednak 1.");
		if(timeScale == null)
			throw new UniformTestbenchException("timeScale ne smije biti null.");
		
		this.sourceName = sourceName;
		this.testBenchLength = testBenchLength;
		this.timeScale = timeScale;
		this.simulationLength = 0;
		// changed to LinkedHashMap by Miro to preserve ordering
		this.signalMap = new LinkedHashMap<String, Signal>();
		
		this.listeners = new ArrayList<TestbenchListener>();
		this.suspendedChangedSignals = new HashSet<Signal>();
		this.signalChangedEventsSuspended = false;
	}
	
	public abstract long getPeriodLength(); 
	
	public List<Signal> getSignals() {
		return new ArrayList<Signal>(this.signalMap.values());
	}
	
	public Signal getSignal(String signalName) throws UniformTestbenchException {
		if(signalName == null) {
			throw new UniformTestbenchException("signalName ne smije biti null.");
		}
		return this.signalMap.get(signalName);
	}
	
	public void addSignal(EditableSignal signal) throws UniformTestbenchException {
		if(signal == null) {
			throw new UniformTestbenchException("signal ne smije biti null.");
		}
		if(!this.signalMap.containsKey(signal.getName())) {
			this.signalMap.put(signal.getName(), signal);
			signal.setSignalChangeListener(this);
			this.fireSignalAddedEvent(signal.getName());
		}
	}
	
	public long getTestBenchLength() {
		return testBenchLength;
	}

	public void setTestBenchLength(long testBenchLength) throws UniformTestbenchException {
		if(testBenchLength <= 0)
			throw new UniformTestbenchException("testBenchLength mora biti veci ili jednak 1.");
		long oldTestBenchLength = this.testBenchLength;
		this.testBenchLength = testBenchLength;
		
		if(oldTestBenchLength != this.testBenchLength) {
			this.fireTestBenchLengthChangedEvent();
		}
	}

	public TimeScale getTimeScale() {
		return timeScale;
	}

	public long getSimulationLength() {
		return simulationLength;
	}

	public void setSimulationLength(long simulationLength) throws UniformTestbenchException {
		if(simulationLength < 0)
			throw new UniformTestbenchException("simulationLength mora biti veci ili jednak 0.");
		long oldSimulationLength = this.simulationLength;
		this.simulationLength = simulationLength;
		
		if(oldSimulationLength != this.simulationLength) {
			this.fireSimulationLengthChangedEvent();
		}
	}

	public String getSourceName() {
		return sourceName;
	}
	
	protected Element getSignalsElement(Document xmldoc) {
		Element signalsElement = xmldoc.createElementNS(null, "Signals");
		Element sElement = null;
		
		for(Signal s : this) {
			if(s.getClass() == VectorSignal.class || s.getClass() == ScalarSignal.class) {
				sElement = xmldoc.createElementNS(null, "Signal");
				sElement.setAttribute("name", s.getName());
				if(s.getDimension() == 1) {
					sElement.setAttribute("type", "scalar");
				}
				else {
					sElement.setAttribute("type", "vector");
					sElement.setAttribute("dimension", String.valueOf(s.getDimension()));
					sElement.setAttribute("direction", ((VectorSignal)s).getDirection().toString());
				}
				StringBuilder sb = new StringBuilder();
				for(SignalChange sc : s) {
					sb.append("(");
					sb.append(String.valueOf(sc.getTime() / TimeScale.getMultiplier(this.timeScale)));
					sb.append(",");
					sb.append(sc.getSignalValue());
					sb.append(")");
				}
				sElement.appendChild(xmldoc.createTextNode(sb.toString()));
				signalsElement.appendChild(sElement);
			}
		}
		return signalsElement;
	}
	
	protected Element getTestBenchInfoElement(Document xmldoc) {
		Element testBenchInfoElement = xmldoc.createElementNS(null, "TestBenchInfo");
		Element sourceNameElement = xmldoc.createElementNS(null, "SourceName");
		Element simulationLengthElement = xmldoc.createElementNS(null, "SimulationLength");
		
		testBenchInfoElement.appendChild(sourceNameElement);
		testBenchInfoElement.appendChild(simulationLengthElement);
		
		sourceNameElement.appendChild(xmldoc.createTextNode(this.sourceName));
		simulationLengthElement.appendChild(xmldoc.createTextNode(String.valueOf(this.simulationLength / TimeScale.getMultiplier(this.timeScale))));
		
		return testBenchInfoElement;
	}
	
	private void increaseTestbenchLength(long newLength) {
		try {
			if(newLength > this.testBenchLength) {
				this.setTestBenchLength(newLength);
			}
		} catch (UniformTestbenchException e) {
			e.printStackTrace();
		}
	}
	
	protected abstract Element getInitTimingInfoElement(Document xmldoc);
	
	public abstract String toXml();
	
	@Override
	public void suspendSignalChangedEvents() {
		this.signalChangedEventsSuspended = true;
	}
	
	@Override
	public void resumeSignalChangedEvents() {
		this.signalChangedEventsSuspended = false;
		for(Signal s : this.suspendedChangedSignals) {
			this.signalChanged(s);
		}
		this.suspendedChangedSignals.clear();
	}
	
	@Override
	public void signalChanged(Signal signal) {
		if(this.signalChangedEventsSuspended) {
			this.suspendedChangedSignals.add(signal);
		}
		else {
			if(this.signalMap.get(signal.getName()) != null) {
				long signalLength = this.signalMap.get(signal.getName()).getSignalLength();
				if(signalLength > this.testBenchLength) {
					this.increaseTestbenchLength(signalLength + this.getPeriodLength());
				}
			}
			this.fireSignalChangedEvent(signal.getName());
		}
	}
	
	@Override
	public Iterator<Signal> iterator() {
		return this.signalMap.values().iterator();
	}
	
	protected void fireSignalChangedEvent(String signalName) {
		for(TestbenchListener l : this.listeners) {
			l.signalChanged(signalName);
		}
	}
	
	protected void fireSignalAddedEvent(String signalName) {
		for(TestbenchListener l : this.listeners) {
			l.signalAdded(signalName);
		}
	}
	
	protected void fireTestBenchLengthChangedEvent() {
		for(TestbenchListener l : this.listeners) {
			l.testBenchLengthChanged();
		}
	}
	
	protected void fireSimulationLengthChangedEvent() {
		for(TestbenchListener l : this.listeners) {
			l.simulationLengthChanged();
		}
	}
	
	@Override
	public void addTestbenchListener(TestbenchListener l) {
		this.listeners.add(l);
	}

	@Override
	public Testbench getTestbench() {
		return this;
	}

	@Override
	public void removeTestbenchListener(TestbenchListener l) {
		this.listeners.remove(l);
	}
}
