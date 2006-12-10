package hr.fer.zemris.vhdllab.applets.automat.VHDLparser;

import hr.fer.zemris.vhdllab.applets.automat.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.automat.Prijelaz;
import hr.fer.zemris.vhdllab.applets.automat.Stanje;

import java.util.HashSet;
import java.util.LinkedList;

public class MooreParser implements IAutomatVHDLParser {
	
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
		buffer.append("library IEEE;\nuse IEEE.STD_LOGIC_1164.ALL\n\n");
		
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
		.append("IF reset='1' THEN\n\t\tstate_present <= ")
		.append(podatci.pocetnoStanje).append(";\n\t")
		.append("ELSIF falling_edge(clock) THEN\n\t\t")
		.append("state_present <= state_next;\n\tEND IF;\n")
		.append("END PROCESS;\n");
		return buffer;
	}

	private StringBuffer createBlok2(StringBuffer buffer) {
		buffer.append("PROCESS(state_present)\nBEGIN\n\t")
		.append("CASE state_present IS");
		for(Stanje st:stanja){
			buffer.append("\n\tWHEN " ).append(st.ime).append(" =>");
			buffer=generirajIzlaze(st,buffer);
		}
		buffer.append("\n\tWHEN OTHERS => "); 
		buffer=generirajIzlaze(null,buffer);
		buffer.append("\n\tEND CASE\nEND PROCESS\n");
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
			buffer.append(sig.getImeSignala()).append(" <= \"")
			.append(izlaz.substring(pozicija,pozicija+sig.getSirinaSignala()))
			.append("\";\n\t\t");
			pozicija+=sig.getSirinaSignala();
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		return buffer;
	}

	private StringBuffer createBlok1(StringBuffer buffer) {
		// TODO Auto-generated method stub
		buffer.append("PROCESS(state_present");
		for (Signal ul:ulazniSignali)buffer.append(", ").append(ul.getImeSignala());
		buffer.append(")\nBEGIN\n\tCASE state_present IS");
		for(Stanje st:stanja){
			//TODO ovdije si stao!!
		}
		
		
		return buffer;
	}

	private StringBuffer createType(StringBuffer buffer) {
		buffer.append("TYPE stateType IS (");
		for(Stanje st:stanja)buffer.append(st.ime).append(", ");
		buffer.deleteCharAt(buffer.length()-1);
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(");\nSIGNAL state_present, state_next:stateType;\n");
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
					buffer.append(Integer.parseInt(rijeci[3])).append(" TO ").append(Integer.parseInt(rijeci[4])).append(")");
				else buffer.append(Integer.parseInt(rijeci[3])).append(" DOWNTO ").append(Integer.parseInt(rijeci[4])).append(")");
			}
			buffer.append(";");
		}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(")\nEND ").append(podatci.ime).append(";\n");
		return buffer;
	}

	private void unparseEntity() {
		String[] redovi=podatci.interfac.split("\n");
		podatci.sirinaIzlaza=0;
		podatci.sirinaUlaza=0;
		for(int i=0;i<redovi.length;i++){
			String[] pom=redovi[i].split(" ");
			int br=1;
			if(pom[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				br+=Math.abs(Integer.parseInt(pom[3])-Integer.parseInt(pom[4]));	
			}
			if(pom[1].equals("in"))podatci.sirinaUlaza+=br;
			else if(pom[1].equals("out"))podatci.sirinaIzlaza+=br;
			if(pom[1].toUpperCase().equals("IN")){
				ulazniSignali.add(new Signal(br,pom[0]));
			}else {
				izlazniSignali.add(new Signal(br,pom[0]));
			};
		}
	}

	public String getData() {
		return data;
	}

}
