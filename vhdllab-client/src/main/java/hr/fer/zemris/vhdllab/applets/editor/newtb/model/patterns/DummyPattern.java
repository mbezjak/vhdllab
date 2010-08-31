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

import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformPatternException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalChangeException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;

import java.util.List;
/**
 * Dummy pattern, used for testing. See patternTest for clarity.
 * @author Ivan Cesar
 *
 */
public class DummyPattern extends Pattern {

	public DummyPattern(int cycles) {
		super(cycles);
	}
	
	public String getWithTrailZeroesDummy(int num, int dim) throws UniformSignalChangeException, UniformPatternException
	{
		return this.getWithTrailZeroes(num, dim);
	}

	@Override
	public List<SignalChange> getChanges(long start, long end)
			throws UniformSignalChangeException {
		return null;
	}

}
