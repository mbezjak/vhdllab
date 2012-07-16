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

/**
 * 
 * @author Davor Jurisic
 *
 */
public class SignalChange {
	
	private short signalDimension;
	private long time;
    private String signalValue;
    private Signal parentSignal;
    
    public SignalChange(short signalDimension, String signalValue, long time) throws UniformSignalChangeException {
    	this(signalDimension, signalValue, time, null);
    }
    
	public SignalChange(short signalDimension, String signalValue, long time, Signal parentSignal) throws UniformSignalChangeException {
		if(!this.signalValueValid(signalValue)) {
			throw new UniformSignalChangeException("Vrijednost signala moze poprimiti samo binarne znamenke.");
		}
		if(signalValue.length() != signalDimension)
			throw new UniformSignalChangeException("Dimezija signala razlikuje se od dozvoljene dimenzije.");
		if(time < 0)
			throw new UniformSignalChangeException("Vrijeme ne smije biti negativno.");
		if(signalDimension < 1)
			throw new UniformSignalChangeException("Dimenzija signala mora biti veca ili jednaka 1.");
		
		this.signalDimension = signalDimension;
		this.signalValue = signalValue;
		this.time = time;
		this.parentSignal = parentSignal;
	}
	
	private boolean signalValueValid(String signalValue)
	{
		for(int i = 0; i < signalValue.length(); i++) {
			char c = signalValue.charAt(i); 
			if(c != '1' && c != '0') return false;
		}
		return true;
	}

	public long getTime() {
		return time;
	}

	public String getSignalValue() {
		return signalValue;
	}

	public void setSignalValue(String signalValue) throws UniformSignalChangeException {
		if(signalValue.length() != this.signalDimension)
			throw new UniformSignalChangeException("Dimezija signala razlikuje se od dozvoljene dimenzije.");
		if(!this.signalValueValid(signalValue)) {
			throw new UniformSignalChangeException("Vrijednost signala moze poprimiti samo binarne znamenke.");
		}
		this.signalValue = signalValue;
		
		if(this.parentSignal != null && this.parentSignal.signalChangeListener != null) {
			this.parentSignal.signalChangeListener.signalChanged(this.parentSignal);
		}
	}

	public short getSignalDimension() {
		return signalDimension;
	}
	
	@Override
	public String toString()
	{
		return "Dimenzija: " + signalDimension + ", Time: " + time + ", Value: " + signalValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + signalDimension;
		result = prime * result
				+ ((signalValue == null) ? 0 : signalValue.hashCode());
		result = prime * result + (int) (time ^ (time >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SignalChange other = (SignalChange) obj;
		if (signalDimension != other.signalDimension)
			return false;
		if (signalValue == null) {
			if (other.signalValue != null)
				return false;
		} else if (!signalValue.equals(other.signalValue))
			return false;
		if (time != other.time)
			return false;
		return true;
	}
}

