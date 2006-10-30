package hr.fer.zemris.vhdllab.applets.automat;

import java.awt.Color;

public class Stanje {
	
	public String ime;
	public String izlaz;
	public int ox;
	public int oy;
	public Color boja=null;
	
	public Stanje(String ime,String izlaz,String ox,String oy){
		super();
		this.ime=ime;
		this.izlaz=izlaz;
		this.ox=Integer.parseInt(ox);
		this.oy=Integer.parseInt(oy);
		this.boja=Color.BLACK;
	}
	@Override
	public boolean equals(Object obj) {
		boolean vrj;
		try{Stanje st=(Stanje) obj;
		vrj=this.ime.equals(st.ime);}
		catch(NullPointerException e){
			vrj=false;
		}
		return vrj;
	}

}
