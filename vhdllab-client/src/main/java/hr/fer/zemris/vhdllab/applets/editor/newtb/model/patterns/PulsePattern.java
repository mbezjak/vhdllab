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

public class PulsePattern extends Pattern {

    int initial;
    long initLen;
    int pulseValue;
    long pulseLen;
    
    public PulsePattern(int cycles, int initial, long initLen, int pulseValue, long pulseLen ) throws UniformPatternException {
        super(cycles);
        if(initLen <= 0 || pulseLen <= 0 || initial + pulseValue != 1 || initial < 0 || initial > 1)
            throw new UniformPatternException(Messages.initLenPos + "\n" + Messages.pulseLenPos);
        this.initial = initial;
        this.initLen = initLen;
        this.pulseValue = pulseValue;
        this.pulseLen = pulseLen;
    }

    @Override
    public List<SignalChange> getChanges(long start, long end) throws UniformSignalChangeException {
        List<SignalChange> ret = new ArrayList<SignalChange>();
        for(long i = 0; i <= cycles; i++)
        {
            if(start + i * (initLen + pulseLen) > end || start + i * (initLen + pulseLen) < start)
                break;
            ret.add(new SignalChange(
                    (short)1,
                    Integer.toString(initial),
                    start + i * (initLen + pulseLen)
                    ));
            if(i == cycles) {
                break;
            }
            if(start + i * (initLen + pulseLen) + initLen > end || start + i * (initLen + pulseLen) + initLen < start)
                break;
            ret.add(new SignalChange(
                    (short)1,
                    Integer.toString(pulseValue),
                    start + i*(initLen + pulseLen) + initLen
                    ));
        }
        return ret;
    }
    @Override
    public String toString()
    {
        return super.toString() +
            "Initial value (length): " + initial + " (" + initLen + ")\n " + 
            "Pulse value (length): " + pulseValue + " (" + pulseLen + ").";
    }

}
