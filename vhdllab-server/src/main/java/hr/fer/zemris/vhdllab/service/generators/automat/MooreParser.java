package hr.fer.zemris.vhdllab.service.generators.automat;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

//TODO ako je samo std_logic da ne pise a(0) OK

public class MooreParser implements IAutomatVHDLGenerator {
	
	private LinkedList<Stanje> stanja;
	private HashSet<Prijelaz> prijelazi;
	private AUTPodatci podatci;
	
	private LinkedList<Signal> ulazniSignali=new LinkedList<Signal>();
	private LinkedList<Signal> izlazniSignali=new LinkedList<Signal>();
	
	private String data="";
	
	public MooreParser(LinkedList<Stanje> stanja, AUTPodatci podatci, HashSet<Prijelaz> prijelazi) {
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
		buffer=createBlok2(buffer);
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
		.append("state_present <= state_next;\n\tEND IF;\n")
		.append("END PROCESS;\n");
		return buffer;
	}

	private StringBuffer createBlok2(StringBuffer buffer) {
		buffer.append("PROCESS(state_present)\nBEGIN\n\t")
		.append("CASE state_present IS");
		for(Stanje st:stanja){
			buffer.append("\n\tWHEN ST_" ).append(st.ime).append(" =>");
			buffer=generirajIzlaze(st,buffer);
		}
		buffer.append("\n\tWHEN OTHERS => "); 
		buffer=generirajIzlaze(null,buffer);
		buffer.append("\n\tEND CASE;\nEND PROCESS;\n");
		return buffer;
	}

	private StringBuffer generirajIzlaze(Stanje st, StringBuffer buffer) {
		String izlaz="";
		if(st==null){
			StringBuffer b1=new StringBuffer();
			for(int i=0;i<podatci.sirinaIzlaza;i++)b1.append("0");
			izlaz=b1.toString();
		}else izlaz=st.izlaz;
		
		buffer.append("\n\t\t");
		int pozicija=0;
		for(Signal sig:izlazniSignali){
			String navodnici=(sig.getTip()==Signal.STD_LOGIC_VECTOR?"\"":"'");
			buffer.append(sig.getImeSignala()).append(" <= ").append(navodnici)
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
			}
			if(!test)buffer.append("ELSE ");
			buffer.append("state_next<=ST_").append(st.els).append(";");
			if(!test)buffer.append("\n\t\tEND IF;");
		}
		buffer.append("\n\t\tWHEN OTHERS => state_next <= state_present;\n\tEND CASE;\nEND PROCESS;\n");
		return buffer;
	}

	private StringBuffer generirajPrijelaz(StringBuffer buffer, Prijelaz pr) {
		buffer=createCondition(buffer,pr.pobudaIzlaz);
		buffer.append(" THEN state_next<=ST_").append(pr.u).append(";\n\t\t");
		return buffer;
	}

	private StringBuffer createCondition(StringBuffer buffer, TreeSet<String> pobudaIzlaz) {
		for(String pom:pobudaIzlaz){
			buffer.append("(");
			int broj=0;
			for(Signal sig:ulazniSignali){
				buffer=createCplxCondition(buffer,sig,pom.substring(broj,broj+sig.getSirinaSignala()));
				broj+=sig.getSirinaSignala();
			}
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.deleteCharAt(buffer.length()-1);
			buffer.append(")").append(" OR ");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
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
		
		/*if(!pom.matches(".*[-].*")) {
			pombuf.append(sig.getImeSignala()).append("=").append(navodnici).append(pom)
				.append(navodnici).append(" AND ");
		} else{
			int state=0;
			int start=0;
			for(int i=0;i<pom.length();i++){
				if(state==0) if(pom.charAt(i)=='0'||pom.charAt(i)=='1'){
					start=i;
					state=1;
				}
				if(state==1)
					if(pom.charAt(i)=='-'){
						buffer.append(sig.getImeSignala());
						buffer.append("(").append(start).append(" TO ").append(i-1).append(")");
						buffer.append("=").append(navodnici).append(pom.substring(start,i))
					    .append(navodnici).append(" AND ");
						state=0;
					}
			}
			if (state==1){
				buffer.append(sig.getImeSignala());
				buffer.append("(").append(start).append(" TO ").append(pom.length()-1).append(")");
				buffer.append("=").append(navodnici).append(pom.substring(start,pom.length()))
				.append(navodnici).append(" AND ");
			}
		}
		*/
		return pombuf;
	}

	private StringBuffer createType(StringBuffer buffer) {
		//buffer.append("TYPE stateType IS (");
		//for(Stanje st:stanja)buffer.append("ST_").append(st.ime).append(", ");
		//buffer.deleteCharAt(buffer.length()-1);
		//buffer.deleteCharAt(buffer.length()-1);
		//buffer.append(");\nSIGNAL state_present, state_next:stateType;\n");
		
		Signal state = createStateSignal();
		StringBuffer bufferTemp = new StringBuffer();
		if(state.getTip()==Signal.STD_LOGIC){
			bufferTemp.append(" std_logic");
		}else{
			bufferTemp.append(" std_logic_vector(").append(state.getFrom())
			.append(state.getSmijer()==Signal.C_TO?" TO ":" DOWNTO ").append(state.getTo()).append(")");
		}
		String stateType = bufferTemp.toString();
		
		for(Stanje st:stanja){
			buffer.append("\n  CONSTANT ST_").append(st.ime).append(": ").append(stateType).append(" := ")
			.append(decToBinString(state.getTip(), stanja.indexOf(st), state.getFrom()+1)).append(";");
		}
		
		buffer.append("\n");
		buffer.append("\n  SIGNAL state_present, state_next: ").append(stateType).append(";");
		
		buffer.append("\n");
		
		return buffer;
	}
	
	private String decToBinString(int tip, int indexOf, int size) {
		String s = "";
		
		if(tip == Signal.STD_LOGIC){
			return indexOf == 1?"'1'":"'0'";
		}else{
			StringBuffer b = new StringBuffer("\"");
			for(int i = 0; i < size;i++){
				b.append(indexOf%2);
				indexOf/=2;
			}
			s = b.append("\"").reverse().toString();
		}
		
		return s;
	}

	private Signal createStateSignal() {
		int length = stanja.size();
		String tip = "std_logic";
		int from = 0;
		int to = 0;

		if(length>2){
			tip = "std_logic_vector";
			from = (int) Math.floor(Math.log(length-1)/Math.log(2));
			to = 0;
		}
		
		Signal s = new Signal(from,to,"signal",tip);
		return s;
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
