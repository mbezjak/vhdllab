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
package hr.fer.zemris.vhdllab.applets.editor.newtb.model.patterns;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.Messages;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * 
 * @author yohney
 *
 */

public class RandomPattern extends Pattern {

	private int randomSeed;
	private long periodLen;
	private long maxPeriodNumber;

	public RandomPattern(int cycles, int randomSeed, long periodLen, long maxPeriodNumber) throws UniformPatternException {
		super(cycles);
		if(periodLen <= 0 || maxPeriodNumber <= 0)
			throw new UniformPatternException(Messages.randomPos);
		this.randomSeed = randomSeed;
		this.periodLen = periodLen;
		this.maxPeriodNumber = maxPeriodNumber;
	}


	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException {
		List<SignalChange> ret = new ArrayList<SignalChange>();
		Random r = new Random((long)randomSeed == 0 ? 1001 : (long)randomSeed);
		int count = cycles;
		long currentTime = start;
		boolean lowSignal = true;
		long length = 0;
		while(count-- > 0)
		{
			if(lowSignal) {
				ret.add(new SignalChange((short)1, "0", currentTime));
				lowSignal = false;
			} else {
				ret.add(new SignalChange((short)1, "1", currentTime));
				lowSignal = true;
			}
			length = 1 + (Math.abs(r.nextLong()) % maxPeriodNumber);
			currentTime += length * periodLen;
			if(currentTime > end || currentTime < 0)
				return ret;
		}
		return ret;
	}
	
	@Override
    public String toString()
	{
		return super.toString() +
			"Random seed: " + randomSeed + "\n " + 
			"Pulse length (max skip length): " + periodLen + " (" + maxPeriodNumber + ").";
	}

}
