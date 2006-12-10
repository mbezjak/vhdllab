package hr.fer.zemris.vhdllab.applets.automat.VHDLparser;

public class Signal {
	private int sirinaSignala=0;
	private String imeSignala=null;
	public Signal(int s1, String s2) {
		sirinaSignala=s1;
		imeSignala=s2;
	}
	public String getImeSignala() {
		return imeSignala;
	}
	public int getSirinaSignala() {
		return sirinaSignala;
	}
}
