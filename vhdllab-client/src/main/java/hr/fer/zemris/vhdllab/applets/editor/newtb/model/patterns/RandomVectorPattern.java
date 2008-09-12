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
		BigInteger maxGen = BigInteger.ONE.add(BigInteger.ONE).pow(dim);
		int maxGenInt = maxGen.bitCount() < 32 ? maxGen.intValue() : Integer.MAX_VALUE;
		Random r = new Random();
		for( int i = 0; i < cycles && start + i * pulseLen <= end && start + i * pulseLen >= start; i++)
			ret.add(new SignalChange(
					dim, 
					getWithTrailZeroes(r.nextInt(maxGenInt), dim),
					start + i*pulseLen));
		return ret;			
	}

}
