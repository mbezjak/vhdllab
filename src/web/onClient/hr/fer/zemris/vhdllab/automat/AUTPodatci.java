package hr.fer.zemris.vhdllab.automat;

public class AUTPodatci {
	
	public String ime;
	public String tip;
	public String bbu;
	public String bbi;
	public String pocetnoStanje;
	
	public AUTPodatci(String ime,String tip,String bbu,String bbi,String pocSt){
		super();
		this.ime=ime;
		this.tip=tip;
		this.bbu=bbu;
		this.bbi=bbi;
		this.pocetnoStanje=pocSt;
	}
}
