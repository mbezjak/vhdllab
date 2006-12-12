package hr.fer.zemris.vhdllab.applets.automat.VHDLparser;

public class Signal {
	
	public final static int STD_LOGIC_VECTOR=0;
	public final static int STD_LOGIC=1;
	
	private int sirinaSignala=0;
	private String imeSignala=null;
	private int tip=0;
	public Signal(int s1, String s2,String s3) {
		sirinaSignala=s1;
		imeSignala=s2;
		tip=(s3.toUpperCase().equals("STD_LOGIC")?1:0);
	}
	public String getImeSignala() {
		return imeSignala;
	}
	public int getSirinaSignala() {
		return sirinaSignala;
	}
	
	public int getTip(){
		return tip;
	}
}
