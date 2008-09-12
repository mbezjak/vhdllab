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
