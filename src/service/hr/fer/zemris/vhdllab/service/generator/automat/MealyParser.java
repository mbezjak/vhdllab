package hr.fer.zemris.vhdllab.service.generator.automat;
//TODO sredi me!!!-autoDrawer srediti za izlaz elseprijelaza!!!!
import java.util.HashSet;
import java.util.LinkedList;

public class MealyParser implements IAutomatVHDLGenerator {

	private LinkedList<Stanje> stanja;
	private HashSet<Prijelaz> prijelazi;
	private AUTPodatci podatci;
	
	private LinkedList<Signal> ulazniSignali=new LinkedList<Signal>();
	private LinkedList<Signal> izlazniSignali=new LinkedList<Signal>();
	
	private String data="";
	
	public MealyParser(LinkedList<Stanje> stanja, AUTPodatci podatci, HashSet<Prijelaz> prijelazi) {
		this.stanja=stanja;
		this.podatci=podatci;
		this.prijelazi=prijelazi;
		unparseEntity();		
		createData();
	}

	private void createData() {
		StringBuffer buffer=new StringBuffer();
		buffer.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL;\n\n");
		
		buffer=addEntity(buffer);
		buffer.append("\nARCHITECTURE Behavioral OF ").append(podatci.ime).append(" IS\n");
		buffer=createType(buffer);
		buffer.append("\nBEGIN\n");
		buffer=createBlok1(buffer);
		buffer=createBlok3(buffer);
		buffer.append("\nEND Behavioral;");
		data=buffer.toString();
	}

	private StringBuffer createBlok3(StringBuffer buffer) {
		buffer.append("PROCESS(clock, reset)\nBEGIN\n\t")
		.append("IF reset='").append(podatci.reset).append("' THEN\n\t\tstate_present <= ST_")
		.append(podatci.pocetnoStanje).append(";\n\t")
		.append("ELSIF ");
		if(podatci.clock.equalsIgnoreCase("falling_edge"))buffer.append("falling_edge(clock)");
		else if(podatci.clock.equalsIgnoreCase("rising_edge"))buffer.append("rising_edge(clock)");
		else buffer.append("clock=").append(podatci.clock);
		buffer.append(" THEN\n\t\t")
		.append("state_present <= state_n;");
		
		buffer = spojiIzlaze(buffer);
		
		buffer.append("\n\tEND IF;\n")
		.append("END PROCESS;\n");
		return buffer;
	}

	private StringBuffer spojiIzlaze(StringBuffer buffer) {
		for(Signal s:izlazniSignali){
			buffer.append("\n\t\t").append(s.getImeSignala()).append("<=").append(s.getImeSignala()).append("_n;");
		}
		return buffer;
	}

	private StringBuffer generirajIzlaze(String st, StringBuffer buffer) {
		String izlaz="";
		if(st==null||st.equals("0")){
			StringBuffer b1=new StringBuffer();
			for(int i=0;i<podatci.sirinaIzlaza;i++)b1.append("0");
			izlaz=b1.toString();
		}else izlaz=st;
		
		buffer.append("\n\t\t");
		int pozicija=0;
		for(Signal sig:izlazniSignali){
			String navodnici=(sig.getTip()==Signal.STD_LOGIC_VECTOR?"\"":"'");
			buffer.append(sig.getImeSignala()).append("_n <= ").append(navodnici)
			.append(izlaz.substring(pozicija,pozicija+sig.getSirinaSignala()))
			.append(navodnici).append(";\n\t\t");
			pozicija+=sig.getSirinaSignala();
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		return buffer;
	}

	private StringBuffer createBlok1(StringBuffer buffer) {
		buffer.append("PROCESS(state_present");
		for (Signal ul:ulazniSignali)buffer.append(", ").append(ul.getImeSignala());
		buffer.append(")\nBEGIN\n\tCASE state_present IS");
		for(Stanje st:stanja){
			buffer.append("\n\t\tWHEN ST_").append(st.ime).append("=>\n\t\t");
			boolean test=true;
			for(Prijelaz pr:prijelazi)
				if(pr.iz.equals(st.ime)){
					buffer.append(test?"IF ":"ELSIF ");
					test=false;
					buffer=generirajPrijelaz(buffer,pr);
					
					buffer.deleteCharAt(buffer.length()-1);
					buffer.deleteCharAt(buffer.length()-1);
					buffer.deleteCharAt(buffer.length()-1);
					buffer.deleteCharAt(buffer.length()-1);
					buffer.deleteCharAt(buffer.length()-1);
					buffer.deleteCharAt(buffer.length()-1);
			}
			if(!test)buffer.append("ELSE ");
			buffer.append("state_n<=ST_").append(st.els).append(";");
			buffer=generirajIzlaze(st.eIz,buffer);
			if(!test)buffer.append("\n\t\tEND IF;");
		}
		buffer.append("\n\t\tWHEN OTHERS => state_n <= state_present;\n\tEND CASE;\nEND PROCESS;\n");
		return buffer;
	}

	private StringBuffer generirajPrijelaz(StringBuffer buffer, Prijelaz pr) {
		buffer=createCondition(buffer,pr);
		return buffer;
	}

	private StringBuffer createCondition(StringBuffer buffer, Prijelaz pr) {
		for(String pomocni:pr.pobudaIzlaz){
			String[] pom=pomocni.split("/");
			int broj=0;
			for(Signal sig:ulazniSignali){
				buffer=createCplxCondition(buffer,sig,pom[0].substring(broj,broj+sig.getSirinaSignala()));
				broj+=sig.getSirinaSignala();
			}
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.append(" THEN state_n<=ST_").append(pr.u).append(";");
			buffer=generirajIzlaze(pom[1],buffer);
			buffer.append("\n\t\tELSIF ");
		}
		return buffer;
	}
	
	private StringBuffer createCplxCondition(StringBuffer buffer, Signal sig, String pom) {
		String navodnici=(sig.getTip()==Signal.STD_LOGIC?"'":"\"");
		StringBuffer pombuf=buffer;
		
		for(int i=0;i<pom.length();i++)
			if(pom.charAt(i)!='-'){
				buffer.append(sig.getImeSignala());
				if(sig.getTip()==Signal.STD_LOGIC_VECTOR)
					buffer.append("(").append(sig.getFrom()+sig.getSmijer()*i).append(")");
				buffer.append("=").append(navodnici).append(pom.charAt(i))
				.append(navodnici).append(" AND ");
			}
		return pombuf;
	}

	private StringBuffer createType(StringBuffer buffer) {
		buffer.append("TYPE stateType IS (");
		for(Stanje st:stanja)buffer.append("ST_").append(st.ime).append(", ");
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(");\nSIGNAL state_present, state_n:stateType;\n");
		
		String[] redovi=podatci.interfac.split("\n");
		for(int i=0;i<redovi.length;i++){
			buffer.append("\n");
			String[] rijeci=redovi[i].split(" ");
			buffer.append("SIGNAL ").append(rijeci[0]).append("_n: ").append(rijeci[2]);
			if(rijeci[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				if(Integer.parseInt(rijeci[3])<Integer.parseInt(rijeci[4]))
					buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" TO ").append(Integer.parseInt(rijeci[4])).append(")");
				else buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" DOWNTO ").append(Integer.parseInt(rijeci[4])).append(")");
			}
			buffer.append(";");
		}
		buffer.append("\n");
		
		return buffer;
	}

	private StringBuffer addEntity(StringBuffer buffer) {
		buffer.append("ENTITY ").append(podatci.ime).append(" IS PORT(\n")
		.append("\tclock: IN std_logic;\n\treset: IN std_logic;");
		String[] redovi=podatci.interfac.split("\n");
		for(int i=0;i<redovi.length;i++){
			buffer.append("\n\t");
			String[] rijeci=redovi[i].split(" ");
			buffer.append(rijeci[0]).append(": ").append(rijeci[1].toUpperCase())
			.append(" ").append(rijeci[2]);
			if(rijeci[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				if(Integer.parseInt(rijeci[3])<Integer.parseInt(rijeci[4]))
					buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" TO ").append(Integer.parseInt(rijeci[4])).append(")");
				else buffer.append("(").append(Integer.parseInt(rijeci[3])).append(" DOWNTO ").append(Integer.parseInt(rijeci[4])).append(")");
			}
			buffer.append(";");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(");\nEND ").append(podatci.ime).append(";\n");
		return buffer;
	}

	private void unparseEntity() {
		String[] redovi=podatci.interfac.split("\n");
		podatci.sirinaIzlaza=0;
		podatci.sirinaUlaza=0;
		for(int i=0;i<redovi.length;i++){
			String[] pom=redovi[i].split(" ");
			int br=1;
			int odKud=0;
			int doKud=0;
			if(pom[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				odKud=Integer.parseInt(pom[3]);
				doKud=Integer.parseInt(pom[4]);
				br+=Math.abs(odKud-doKud);	
			}
			if(pom[1].toUpperCase().equals("IN"))podatci.sirinaUlaza+=br;
			else if(pom[1].toUpperCase().equals("OUT"))podatci.sirinaIzlaza+=br;
			if(pom[1].toUpperCase().equals("IN")){
				ulazniSignali.add(new Signal(odKud,doKud,pom[0],pom[2]));
			}else {
				izlazniSignali.add(new Signal(odKud,doKud,pom[0],pom[2]));
			};
		}
	}

	
	public String getData() {
		return data;
	}

}
