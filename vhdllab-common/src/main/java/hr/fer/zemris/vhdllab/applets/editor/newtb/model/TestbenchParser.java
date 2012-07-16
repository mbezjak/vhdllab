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
package hr.fer.zemris.vhdllab.applets.editor.newtb.model;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.VectorDirection;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchParserException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.EditableSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ScalarSignal;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.SignalChange;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.VectorSignal;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

/**
 * 
 * @author Davor Jurisic
 * 
 */
public class TestbenchParser {

    private TestbenchParser() {
    }

    public static Testbench parseXml(String xml) throws UniformTestbenchParserException {
        try {   
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder db;

            db = factory.newDocumentBuilder();
            InputSource in = new InputSource();
            in.setCharacterStream(new StringReader(xml.replace("\n", "")));
            Document xmldoc = db.parse(in);
            return parse(xmldoc);
        } catch (Exception e) {
            throw new UniformTestbenchParserException(e.getMessage());
        }
    }

    private static Testbench parse(Document xmldoc) throws UniformTestbenchParserException {
        
        Element root = xmldoc.getDocumentElement();
        Node testBenchInfoNode = root.getElementsByTagName("TestBenchInfo").item(0);
        Node initTimingInfoNode = root.getElementsByTagName("InitTimingInfo").item(0);
        Node signalsNode = root.getElementsByTagName("Signals").item(0);
        
        if(testBenchInfoNode == null)
            throw new UniformTestbenchParserException(generateErrorMsg("TestBenchInfo", "element"));
        if(initTimingInfoNode == null)
            throw new UniformTestbenchParserException(generateErrorMsg("InitTimingInfo", "element"));
        if(signalsNode == null)
            throw new UniformTestbenchParserException(generateErrorMsg("Signals", "element"));
        
        String sourceName = null;
        String simulationLength = null;
        for(int i = 0; i < testBenchInfoNode.getChildNodes().getLength(); i++) {
            Node n = testBenchInfoNode.getChildNodes().item(i);
            if(n.getNodeName().equals("SourceName")) sourceName = n.getTextContent();
            if(n.getNodeName().equals("SimulationLength")) simulationLength = n.getTextContent();
        }
        if(sourceName == null)
            throw new UniformTestbenchParserException(generateErrorMsg("SourceName", "element"));
        if(simulationLength == null)
            throw new UniformTestbenchParserException(generateErrorMsg("SimulationLength", "element"));
        
        String testbenchType = null;
        String testBenchLength = null;
        String timeScale = null;
        Node singleClockInfoNode = null;
        Node combinatorialInfoNode = null;
        for(int i = 0; i < initTimingInfoNode.getChildNodes().getLength(); i++) {
            Node n = initTimingInfoNode.getChildNodes().item(i);
            if(n.getNodeName().equals("TestBenchType")) testbenchType = n.getAttributes().getNamedItem("type").getNodeValue();
            if(n.getNodeName().equals("TestBenchLength")) testBenchLength = n.getTextContent();
            if(n.getNodeName().equals("TimeScale")) timeScale = n.getTextContent();
            if(n.getNodeName().equals("SingleClockInfo")) singleClockInfoNode = n;
            if(n.getNodeName().equals("CombinatorialInfo")) combinatorialInfoNode = n;
        }
        if(testbenchType == null)
            throw new UniformTestbenchParserException(generateErrorMsg("type", "atribut"));
        if(testBenchLength == null)
            throw new UniformTestbenchParserException(generateErrorMsg("TestBenchLength", "element"));
        if(timeScale == null)
            throw new UniformTestbenchParserException(generateErrorMsg("TimeScale", "element"));
        
        String clockSignalName = null;
        String changeStateEdge = null;
        String clockTimeHigh = null;
        String clockTimeLow = null;
        String inputSetupTime = null;
        
        String checkOutputsTime = null;
        String assignInputsTime = null;
        
        if(testbenchType.equals("single") && singleClockInfoNode != null) {
            clockSignalName = singleClockInfoNode.getAttributes().getNamedItem("clockSignalName").getNodeValue();
            changeStateEdge = singleClockInfoNode.getAttributes().getNamedItem("changeStateEdge").getNodeValue();
            clockTimeHigh = singleClockInfoNode.getAttributes().getNamedItem("clockTimeHigh").getNodeValue();
            clockTimeLow = singleClockInfoNode.getAttributes().getNamedItem("clockTimeLow").getNodeValue();
            inputSetupTime = singleClockInfoNode.getAttributes().getNamedItem("inputSetupTime").getNodeValue();
            
            if(clockSignalName == null)
                throw new UniformTestbenchParserException(generateErrorMsg("clockSignalName", "atribut"));
            if(changeStateEdge == null)
                throw new UniformTestbenchParserException(generateErrorMsg("changeStateEdge", "atribut"));
            if(clockTimeHigh == null)
                throw new UniformTestbenchParserException(generateErrorMsg("clockTimeHigh", "atribut"));
            if(clockTimeLow == null)
                throw new UniformTestbenchParserException(generateErrorMsg("clockTimeLow", "atribut"));
            if(inputSetupTime == null)
                throw new UniformTestbenchParserException(generateErrorMsg("inputSetupTime", "atribut"));
        }
        else if (testbenchType.equals("combinatorial") && combinatorialInfoNode != null) {
            checkOutputsTime = combinatorialInfoNode.getAttributes().getNamedItem("checkOutputsTime").getNodeValue();
            assignInputsTime = combinatorialInfoNode.getAttributes().getNamedItem("assignInputsTime").getNodeValue();
            
            if(checkOutputsTime == null)
                throw new UniformTestbenchParserException(generateErrorMsg("checkOutputsTime", "atribut"));
            if(assignInputsTime == null)
                throw new UniformTestbenchParserException(generateErrorMsg("assignInputsTime", "atribut"));
        }
        else {
            throw new UniformTestbenchParserException(generateErrorMsg("SingleClockInfo ili CombinatorialInfo", "element"));
        }
        
        Testbench tb = null;
        TimeScale ts = TimeScale.valueOf(timeScale);
        
        try {
            if(testbenchType.equals("single")) {
                SingleClockTestbench.Properties p = new SingleClockTestbench.Properties();
                p.changeStateEdge = ChangeStateEdge.valueOf(changeStateEdge);
                p.clockSignalName = clockSignalName;
                p.clockTimeHigh = Long.parseLong(clockTimeHigh) * TimeScale.getMultiplier(ts);
                p.clockTimeLow = Long.parseLong(clockTimeLow) * TimeScale.getMultiplier(ts);
                p.inputSetupTime = Long.parseLong(inputSetupTime) * TimeScale.getMultiplier(ts);
                
                tb = new SingleClockTestbench(
                        sourceName,
                        Long.parseLong(testBenchLength) * TimeScale.getMultiplier(ts),
                        TimeScale.valueOf(timeScale),
                        p
                    );
                tb.setSimulationLength(Long.parseLong(simulationLength) * TimeScale.getMultiplier(ts));
                addSignals(signalsNode, tb);
            }
            else if(testbenchType.equals("combinatorial")) {
                CombinatorialTestbench.Properties p = new CombinatorialTestbench.Properties();
                p.assignInputsTime = Long.parseLong(assignInputsTime) * TimeScale.getMultiplier(ts);
                p.checkOutputsTime = Long.parseLong(checkOutputsTime) * TimeScale.getMultiplier(ts);
                
                tb = new CombinatorialTestbench(
                        sourceName,
                        Long.parseLong(testBenchLength) * TimeScale.getMultiplier(ts),
                        TimeScale.valueOf(timeScale),
                        p
                    );
                tb.setSimulationLength(Long.parseLong(simulationLength) * TimeScale.getMultiplier(ts));
                addSignals(signalsNode, tb);
            }
            else {
                throw new UniformTestbenchParserException("");
            }
        } catch(Exception e) {
            throw new UniformTestbenchParserException("Greska kod instanciranja novog testbencha.");
        }
        
        return tb;
    }
    
    private static void addSignals(Node signalsNode, Testbench tb) throws Exception {
        String name = null;
        String type = null;
        String dimension = null;
        String direction = null;
        String data = null;
        EditableSignal s = null;
        
        for(int i = 0; i < signalsNode.getChildNodes().getLength(); i++) {
            name = null;
            type = null;
            dimension = null;
            direction = null;
            data = null;
            Node n = signalsNode.getChildNodes().item(i);
            name = n.getAttributes().getNamedItem("name").getNodeValue();
            type = n.getAttributes().getNamedItem("type").getNodeValue();
            if(n.getAttributes().getNamedItem("dimension") != null) {
                dimension = n.getAttributes().getNamedItem("dimension").getNodeValue(); 
            }
            if(n.getAttributes().getNamedItem("direction") != null) {
                direction = n.getAttributes().getNamedItem("direction").getNodeValue(); 
            }
            data = n.getTextContent();
            if(type.equals("scalar")) {
                s = new ScalarSignal(name);
                s.setSignalChangeValue(getChanges(data, (short)1, tb.getTimeScale()));
            }
            else if(type.equals("vector")) {
                s = new VectorSignal(name, Short.parseShort(dimension), VectorDirection.valueOf(direction));
                s.setSignalChangeValue(getChanges(data, Short.parseShort(dimension), tb.getTimeScale()));
            }
            tb.addSignal(s);
        }
    }
    
    private static List<SignalChange> getChanges(String data, short dimension, TimeScale timeScale) throws Exception
    {
        List<SignalChange> ls = new ArrayList<SignalChange>();
        if(data.length() == 0)
            return ls;
        
        data = data.replace(")(", "#");
        data = data.replace("(", "");
        data = data.replace(")", "");
        String[] datas = data.split("#");
        if(datas == null)
            return ls;
        for (String s : datas) {
            String[] tmp = s.split(",");
            ls.add(new SignalChange(dimension, tmp[1], Long.parseLong(tmp[0]) * TimeScale.getMultiplier(timeScale)));
        }
        return ls;
    }
    
    
    private static String generateErrorMsg(String elementName, String elementType) {
        return "Nedostaje " + elementName + " " + elementType;
    }
}
