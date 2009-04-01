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
