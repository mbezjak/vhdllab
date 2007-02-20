package hr.fer.zemris.vhdllab.service.generator.automat;

public class Signal {
	
	public final static int STD_LOGIC_VECTOR=0;
	public final static int STD_LOGIC=1;
	public final static int C_DOWNTO=-1;
	public final static int C_TO=1;
	
	private int sirinaSignala=0;
	private String imeSignala=null;
	private int tip=0;
	private int smijer=-1;
	private int from;
	private int to;
	
	public Signal(int from, int to, String s2,String s3) {
		this.from=from;
		this.to=to;
		sirinaSignala=Math.abs(from-to)+1;
		imeSignala=s2;
		tip=(s3.toUpperCase().equals("STD_LOGIC")?1:0);
		smijer=(from<to?C_TO:C_DOWNTO);
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
	public int getFrom() {
		return from;
	}
	public int getSmijer() {
		return smijer;
	}
	public int getTo() {
		return to;
	}
}
