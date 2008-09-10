package hr.fer.zemris.vhdllab.service.generators.automat;

import java.util.HashSet;
import java.util.TreeSet;

public class Prijelaz {
	public String iz;
	public String u;
	public TreeSet<String> pobudaIzlaz;
	
	public Prijelaz(String iz,String u,String pobuda,String izlaz){
		super();
		this.iz=iz;
		this.u=u;
		pobudaIzlaz=new TreeSet<String>();
		String pomocni=pobuda;
		if(izlaz!=null)pomocni=new StringBuffer().append(pomocni).append("/").append(izlaz).toString();
		pobudaIzlaz.add(pomocni);
	}
	public Prijelaz() {
		super();
		pobudaIzlaz=new TreeSet<String>();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean pov=true;
		Prijelaz prj=(Prijelaz)obj;
		if (obj==null) pov=false;
		else if(!(this.iz.equals(prj.iz)&&this.u.equals(prj.u)))pov=false;
		return pov;
	}
	
	@Override
	public String toString() {
		String pomocni=null;
		for(String s:pobudaIzlaz){
			if(pomocni==null)pomocni=s;
			else pomocni=new StringBuffer().append(pomocni).append(", ").append(s).toString();
		}
		return pomocni;
	}
	
	public void dodajPodatak(String pobuda, String izlaz){
		String pomocni=pobuda;
		if(izlaz!=null)pomocni=new StringBuffer().append(pomocni).append("/").append(izlaz).toString();
		pobudaIzlaz.add(pomocni);
	}
	
	public void dodajPodatak(Prijelaz pr,HashSet<Prijelaz>prijelazi){
		if(!equals2(pr,prijelazi))
		this.pobudaIzlaz.addAll(pr.pobudaIzlaz);
	}
	
	/**
	 * funkcija usporeduje dali se smije dodati prijelaz ob u listu prijelaza konacnog automata
	 * obj smije imati samo jedan clan u TreeSetu pobudaIzlaz...
	 * @param obj
	 * @param prijelazi
	 * @return
	 */
	public boolean equals2(Object obj,HashSet<Prijelaz>prijelazi) {
		boolean pov=false;
		if (obj==null) return false;
		Prijelaz prj=(Prijelaz)obj;
		String pom=prj.pobudaIzlaz.first();
		if (obj==null) pov=false;		
		else{
			int length=getLength(prj);
			for(Prijelaz pr:prijelazi){
				if(pr.iz.equals(prj.iz))
				for(String pod:pr.pobudaIzlaz){
					boolean test2=true;
					for(int i=0;i<length;i++)if(!(pom.charAt(i)==pod.charAt(i)||pom.charAt(i)=='-'||pod.charAt(i)=='-')){
						test2=false;
					}
					if(test2){
						pov=true;
						break;
					}
				}
			}
		}
		return pov;
	}
	
	/**
	 * vraca velicinu pobude
	 * @param prj
	 * @return
	 */
	private static int getLength(Prijelaz prj) {
		String str=prj.pobudaIzlaz.first();
		String[] str2=str.split("/");
		return str2[0].trim().length();
	}
	


}
