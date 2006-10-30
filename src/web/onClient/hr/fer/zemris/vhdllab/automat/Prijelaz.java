package hr.fer.zemris.vhdllab.automat;

public class Prijelaz {
	public String iz;
	public String u;
	public String pobuda;
	public String izlaz;
	
	public Prijelaz(String iz,String u,String pobuda,String izlaz){
		super();
		this.iz=iz;
		this.u=u;
		this.pobuda=pobuda;
		this.izlaz=izlaz;
	}
	public Prijelaz(String pobuda,String izlaz){
		super();
		this.pobuda=pobuda;
		this.izlaz=izlaz;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean pov=true;
		Prijelaz prj=(Prijelaz)obj;
		if (obj==null) pov=false;
		else if(prj.iz!=this.iz||prj.u!=this.u)pov=false;
		return pov;
	}

}
