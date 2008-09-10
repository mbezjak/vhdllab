package hr.fer.zemris.vhdllab.service.generators.automat;

import java.awt.Color;

public class Stanje {
	
	public String ime;
	public String izlaz;
	public String els;
	public int ox;
	public int oy;
	public Color boja=null;
	public String eIz;
	
	public Stanje(String ime,String izlaz,String ox,String oy,String el,String eIz){
		super();
		this.ime=ime;
		this.izlaz=izlaz;
		this.ox=Integer.parseInt(ox);
		this.oy=Integer.parseInt(oy);
		this.boja=Color.BLACK;
		this.els=el;
		this.eIz=eIz;
	}
	public Stanje() {
		super();
		this.ime="";
		this.els="";
		izlaz="";
		eIz="";
		this.boja=Color.RED;
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
	
	@Override
	public String toString() {
		String s=null;
		if(this.izlaz==null)s=new StringBuffer().append(this.ime).toString();
		else s=new StringBuffer().append(this.ime).append("/").append(this.izlaz).toString();
		return s;
	}
	
}
