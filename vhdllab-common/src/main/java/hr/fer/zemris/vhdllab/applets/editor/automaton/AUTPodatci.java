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
package hr.fer.zemris.vhdllab.applets.editor.automaton;


public class AUTPodatci {

    public static final int CONST_SIR = 300;
    public static final int CONST_VIS = 300;
    public String ime;
    public String tip;
    public String interfac;
    public String pocetnoStanje = "";
    public String reset;
    public String clock;
    public int sirinaUlaza;
    public int sirinaIzlaza;
    public int sirina;
    public int visina;

    public AUTPodatci(String ime, String tip, String interfac, String pocSt,
            String rs, String cl, String s, String v) {
        super();
        this.ime = ime;
        this.tip = tip;
        this.interfac = interfac;
        this.pocetnoStanje = (pocSt == null ? "" : pocSt);
        clock = cl;
        reset = rs;
        parseInterfac(interfac);
        sirina = Integer.parseInt(s);
        visina = Integer.parseInt(v);
    }

    private void parseInterfac(String interfac2) {
        EntityParser ep = new EntityParser(interfac2);
        interfac = interfac2;
        sirinaIzlaza = ep.getOutputWidth();
        sirinaUlaza = ep.getInputWidth();
    }

}
