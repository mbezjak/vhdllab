package hr.fer.zemris.vhdllab.automat;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.HashSet;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
public class AUTParser {
		
	public AUTPodatci podatci;
	public HashSet<Stanje> stanja=new HashSet<Stanje>();
	public HashSet<Prijelaz> prijelazi=new HashSet<Prijelaz>();
	
	public AUTParser(){
		super();
	}
	
	public void AUTParse(String podatci) throws IOException, SAXException{
		Digester digester=new Digester();
		
		digester.push(this);
		
		digester.addCallMethod("Automat/Podatci_Sklopa","dodajPodatke",5);
		digester.addCallParam("Automat/Podatci_Sklopa/Ime",0);
		digester.addCallParam("Automat/Podatci_Sklopa/Tip",1);
		digester.addCallParam("Automat/Podatci_Sklopa/Broj_Bitova_Ulaz",2);
		digester.addCallParam("Automat/Podatci_Sklopa/Broj_Bitova_Izlaz",3);
		digester.addCallParam("Automat/Podatci_Sklopa/Pocetno_Stanje",4);
		
		digester.addCallMethod("Automat/Stanje","dodajStanje",4);
		digester.addCallParam("Automat/Stanje/Ime",0);
		digester.addCallParam("Automat/Stanje/Izlaz",1);
		digester.addCallParam("Automat/Stanje/Ox",2);
		digester.addCallParam("Automat/Stanje/Oy",3);
		
		digester.addCallMethod("Automat/Prijelaz","dodajPrijelaz",4);
		digester.addCallParam("Automat/Prijelaz/Iz",0);
		digester.addCallParam("Automat/Prijelaz/U",1);
		digester.addCallParam("Automat/Prijelaz/Pobuda",2);
		digester.addCallParam("Automat/Prijelaz/Izlaz",3);
		
		digester.parse(new StringReader(podatci));
	}
	
	public void dodajPodatke(String ime,String tip,String bbu,String bbi,String pocetnoStanje){
		podatci=new AUTPodatci(ime,tip,bbu,bbi,pocetnoStanje);
	}
	public void dodajStanje(String ime, String izlaz, String ox,String oy){
		Stanje st=new Stanje(ime,izlaz,ox,oy);
		stanja.add(st);
	}
	public void dodajPrijelaz(String iz,String u,String pobuda,String izlaz){
		Prijelaz pr=new Prijelaz(iz,u,pobuda,izlaz);
		prijelazi.add(pr);
	}
}
