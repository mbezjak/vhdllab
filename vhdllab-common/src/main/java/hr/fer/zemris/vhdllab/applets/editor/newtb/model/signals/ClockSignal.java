/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals;

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.listeners.SignalChangeListener;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class ClockSignal extends Signal {
	
	private long clockTimeLow;
	private long clockTimeHigh;
	private long timeLength;
	
	public ClockSignal(String name, long clockTimeLow, long clockTimeHigh, long timeLength) throws UniformSignalException {
		this(name, clockTimeLow, clockTimeHigh, timeLength, null);
	}
	
	public ClockSignal(String name, long clockTimeLow, long clockTimeHigh, long timeLength, SignalChangeListener listener) throws UniformSignalException {
		super(name, (short)1, listener);
		
		this.createClockSignalChanges(clockTimeLow, clockTimeHigh, timeLength);
	}
	
	public void createClockSignalChanges(long clockTimeLow, long clockTimeHigh, long timeLength)
			throws UniformSignalException {
		if(clockTimeHigh <= 0)
			throw new UniformSignalException("clockTimeHigh mora biti veci ili jednak 1");
		if(clockTimeLow <= 0)
			throw new UniformSignalException("clockTimeLow mora biti veci ili jednak 1");
		if(timeLength <= 0)
			throw new UniformSignalException("timeLength mora biti veci ili jednak 1");
		
		this.changes.clear();
		
		boolean low = true;
		for(long t = 0; t < timeLength; ) {
			if(low) {
				try {
					this.changes.put(Long.valueOf(t), new SignalChange((short)1, "0", Long.valueOf(t)));
				} catch (UniformSignalChangeException e) { }
				t += clockTimeLow;
				low = false;
			}
			else {
				try {
					this.changes.put(Long.valueOf(t), new SignalChange((short)1, "1", Long.valueOf(t)));
				} catch (UniformSignalChangeException e) { }
				t += clockTimeHigh;
				low = true;
			}
		}
		
		this.clockTimeLow = clockTimeLow;
		this.clockTimeHigh = clockTimeHigh;
		this.timeLength = timeLength;
		
		if(this.signalChangeListener != null) {
			this.signalChangeListener.signalChanged(this);
		}
	}
	
	public long getClockTimeLow() {
		return clockTimeLow;
	}

	public long getClockTimeHigh() {
		return clockTimeHigh;
	}

	public long getTimeLength() {
		return timeLength;
	}
}
