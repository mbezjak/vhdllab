package hr.fer.zemris.vhdllab.service.generator.automat;

public class AUTPodatci {
	
	public String ime;
	public String tip;
	public String interfac;
	public String pocetnoStanje="";
	public String reset;
	public String clock;
	public int sirinaUlaza;
	public int sirinaIzlaza;
	public int sirina;
	public int visina;
	
	public AUTPodatci(String ime,String tip,String interfac,String pocSt,String rs,String cl,String s,String v){
		super();
		this.ime=ime;
		this.tip=tip;
		this.interfac=interfac;
		this.pocetnoStanje=(pocSt==null?"":pocSt);
		clock=cl;
		reset=rs;
		parseInterfac(interfac);
		sirina=Integer.parseInt(s);
		visina=Integer.parseInt(v);
	}

	private void parseInterfac(String interfac2) {
		EntityParser ep=new EntityParser(interfac2);
		interfac=interfac2;
		sirinaIzlaza=ep.getOutputWidth();
		sirinaUlaza=ep.getInputWidth();
	}

}