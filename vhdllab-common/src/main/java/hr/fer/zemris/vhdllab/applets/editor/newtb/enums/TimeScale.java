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
package hr.fer.zemris.vhdllab.applets.editor.newtb.enums;

public enum TimeScale {
	fs, ps, ns, us, ms, s;
	
	public static long getMultiplier(TimeScale t)
	{
		if(t == TimeScale.fs)
			return 1L;
		if(t == TimeScale.ps)
			return 1000L;
		if(t == TimeScale.ns)
			return 1000000L;
		if(t == TimeScale.us)
			return 1000000000L;
		if(t == TimeScale.ms)
			return 1000000000000000L;
		if(t == TimeScale.s)
			return 1000000000000000L;
		return 0;
	}
}
