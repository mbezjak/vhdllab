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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomVectorPattern extends VectorPattern {

	private long pulseLen;
	private short dim;
	
	public RandomVectorPattern(int cycles, long pulseLen, short dimension) throws UniformPatternException {
		super(cycles);
		
		if(pulseLen <= 0)
			throw new UniformPatternException(Messages.randomPos);
		this.pulseLen = pulseLen;
		this.dim = dimension;
	}

	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException, UniformPatternException {
		List<SignalChange> ret = new ArrayList<SignalChange>();
		BigInteger maxGen = BigInteger.valueOf(2l).pow(dim);
		int maxGenInt = maxGen.bitLength() + 1 < 32 ? maxGen.intValue() : Integer.MAX_VALUE;
		Random r = new Random();
		for( int i = 0; i < cycles && start + i * pulseLen <= end && start + i * pulseLen >= start; i++)
			ret.add(new SignalChange(
					dim, 
					getWithTrailZeroes(r.nextInt(maxGenInt), dim),
					start + i*pulseLen));
		return ret;			
	}

}
