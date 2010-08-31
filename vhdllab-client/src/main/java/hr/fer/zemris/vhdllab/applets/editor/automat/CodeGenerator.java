/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.automat;

import hr.fer.zemris.vhdllab.applets.editor.automaton.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Prijelaz;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Stanje;

import java.util.HashSet;
import java.util.LinkedList;

public class CodeGenerator {
	/**
	 * Metoda koja generira interni kod za automat
	 * @return interni kod automata
	 */
	public String generateInternalCode(AUTPodatci podatci, HashSet<Prijelaz> prijelazi, LinkedList<Stanje> stanja){
		String code="<?xml version=\"1.0\"?>";
		code=new StringBuffer().append(code).append("\n<Automat>").toString();
		code=generateHead(podatci,code);
		code=generateStanja(stanja,podatci,code);
		code=generatePrijelazi(prijelazi,podatci,code);
		code=new StringBuffer().append(code).append("</Automat>").toString();
		return code;
	}
	private String generatePrijelazi(HashSet<Prijelaz> prijelazi, AUTPodatci podatci, String code) {
		StringBuffer buffer=new StringBuffer().append(code);
		for(Prijelaz pr:prijelazi){
			for(String pom:pr.pobudaIzlaz){
				buffer.append("<Prijelaz>\n<Iz>").append(pr.iz).append("</Iz>\n<U>").append(pr.u)
				.append("</U>\n<Pobuda>");
				if(podatci.tip.equals("Moore"))buffer.append(pom).append("</Pobuda>\n");
				else{
					String[] pom2=pom.split("/");
					buffer.append(pom2[0]).append("</Pobuda>\n<Izlaz>").append(pom2[1]).append("</Izlaz>\n");
				}
				buffer.append("</Prijelaz>\n");
			}
			
		}
		return buffer.toString();
	}
	private String generateStanja(LinkedList<Stanje> stanja, AUTPodatci podatci, String code) {
		StringBuffer buffer=new StringBuffer().append(code);
		for(Stanje st:stanja){
			buffer.append("<Stanje>\n<Ime>").append(st.ime).append("</Ime>\n");
			if(podatci.tip.equals("Moore"))buffer.append("<Izlaz>")
			.append(st.izlaz).append("</Izlaz>\n").toString();
			buffer.append("<Ox>").append(st.ox).append("</Ox>\n<Oy>").append(st.oy).append("</Oy>\n")
			.append("<Else>").append(st.els).append("</Else>\n");
			if(podatci.tip.equalsIgnoreCase("Mealy"))
				buffer.append("<ElseIzlaz>").append(st.eIz)
				.append("</ElseIzlaz>\n");
			buffer.append("</Stanje>\n");
		}
		return buffer.toString();
		
	}
	private String generateHead(AUTPodatci podatci, String code) {
		return new StringBuffer().append(code).append("\n<Podatci_Sklopa>\n<Ime>").append(podatci.ime)
		.append("</Ime>\n<Tip>").append(podatci.tip).append("</Tip>\n<Interfac>").append(podatci.interfac)
		.append("</Interfac>\n<Pocetno_Stanje>").append(podatci.pocetnoStanje).append("</Pocetno_Stanje>\n")
		.append("<Reset>").append(podatci.reset).append("</Reset>\n<Clock>").append(podatci.clock).append("</Clock>\n<Sirina>")
		.append(podatci.sirina).append("</Sirina>\n<Visina>").append(podatci.visina)
		.append("</Visina>\n</Podatci_Sklopa>\n")
		.toString();		
	}
}
