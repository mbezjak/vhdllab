package hr.fer.zemris.vhdllab.applets.editor.newtb.model;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.ChangeStateEdge;
import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformSignalException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;
import hr.fer.zemris.vhdllab.applets.editor.newtb.model.signals.ClockSignal;

import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * 
 * @author Davor Jurisic
 *
 */
public class SingleClockTestbench extends Testbench {
    
    public static class Properties
    {
        public String clockSignalName;
        public ChangeStateEdge changeStateEdge;
        public long clockTimeHigh;
        public long clockTimeLow;
        public long inputSetupTime;
    }

    protected String clockSignalName;
    protected ChangeStateEdge changeStateEdge;
    protected long clockTimeHigh;
    protected long clockTimeLow;
    protected long inputSetupTime;
    
    public SingleClockTestbench(String sourceName, long testBenchLength, 
            TimeScale timeScale, Properties testbenchProperties) throws UniformTestbenchException {
        super(sourceName, testBenchLength, timeScale);
        if(testbenchProperties == null)
            throw new UniformTestbenchException("testbenchProperties ne smije biti null.");
        if(testbenchProperties.clockSignalName == null || testbenchProperties.clockSignalName.length() == 0)
            throw new UniformTestbenchException("clockSignalName ne smije biti null ili duljine 0.");
        if(testbenchProperties.clockTimeHigh <= 0)
            throw new UniformTestbenchException("clockTimeHigh mora biti veci ili jednak 1.");
        if(testbenchProperties.clockTimeLow <= 0)
            throw new UniformTestbenchException("clockTimeLow mora biti veci ili jednak 1.");
        if(testbenchProperties.inputSetupTime < 0)
            throw new UniformTestbenchException("inputSetupTime mora biti veci ili jednak 0.");
        if(testbenchProperties.changeStateEdge == null)
            throw new UniformTestbenchException("changeStateEdge ne smije biti null.");
        
        this.clockSignalName = testbenchProperties.clockSignalName;
        this.changeStateEdge = testbenchProperties.changeStateEdge;
        this.clockTimeHigh = testbenchProperties.clockTimeHigh;
        this.clockTimeLow = testbenchProperties.clockTimeLow;
        this.inputSetupTime = testbenchProperties.inputSetupTime;
        
        try {
            this.signalMap.put(
                    this.clockSignalName,
                    new ClockSignal(this.clockSignalName, this.clockTimeLow, this.clockTimeHigh, this.testBenchLength, this)
                );
        } catch (UniformSignalException e) {}
    }   
    
    public ClockSignal getClockSignal() {
        return (ClockSignal)this.signalMap.get(this.clockSignalName);
    }

    public long getClockTimeHigh() {
        return clockTimeHigh;
    }

    public long getClockTimeLow() {
        return clockTimeLow;
    }

    public long getInputSetupTime() {
        return inputSetupTime;
    }

    public String getClockSignalName() {
        return clockSignalName;
    }

    public ChangeStateEdge getChangeStateEdge() {
        return changeStateEdge;
    }

    public void setClockTimeHigh(long clockTimeHigh) throws UniformTestbenchException {
        if(clockTimeHigh <= 0)
            throw new UniformTestbenchException("clockTimeHigh mora biti veci ili jednak 1.");
        this.clockTimeHigh = clockTimeHigh;
        try {
            this.getClockSignal().createClockSignalChanges(this.clockTimeLow, this.clockTimeHigh, this.testBenchLength);
        } catch (UniformSignalException e) {}
    }

    public void setClockTimeLow(long clockTimeLow) throws UniformTestbenchException {
        if(clockTimeLow <= 0)
            throw new UniformTestbenchException("clockTimeLow mora biti veci ili jednak 1.");
        this.clockTimeLow = clockTimeLow;
        try {
            this.getClockSignal().createClockSignalChanges(this.clockTimeLow, this.clockTimeHigh, this.testBenchLength);
        } catch (UniformSignalException e) {}
    }

    public void setInputSetupTime(long inputSetupTime) throws UniformTestbenchException {
        if(inputSetupTime < 0)
            throw new UniformTestbenchException("inputSetupTime mora biti veci ili jednak 0.");
        this.inputSetupTime = inputSetupTime;
    }
    
    @Override
    public void setTestBenchLength(long testBenchLength) throws UniformTestbenchException {
        super.setTestBenchLength(testBenchLength);
        try {
            this.getClockSignal().createClockSignalChanges(this.clockTimeLow, this.clockTimeHigh, this.testBenchLength);
        } catch (UniformSignalException e) {}
        
    }
    
    @Override
    public String toXml() {
        try {
            Document xmldoc = null;
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = null;
            builder = factory.newDocumentBuilder();
            DOMImplementation impl = builder.getDOMImplementation();
            xmldoc = impl.createDocument(null, "TestBench", null);

            Element root = xmldoc.getDocumentElement();
            root.appendChild(this.getTestBenchInfoElement(xmldoc));
            root.appendChild(this.getInitTimingInfoElement(xmldoc));
            root.appendChild(this.getSignalsElement(xmldoc));
            
            DOMSource domSource = new DOMSource(xmldoc);
            StringWriter writer = new StringWriter();
            StreamResult result = new StreamResult(writer);
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer transformer = tf.newTransformer();
            transformer.transform(domSource, result);
            String xmlString = writer.toString().replace("><", ">\n<");
            //System.out.println(xmlString);
            return xmlString;
        } catch (ParserConfigurationException ign1) {
        } catch (TransformerException ign2) {}

        return null;
    }

    @Override
    protected Element getInitTimingInfoElement(Document xmldoc) {
        Element initTimingInfoElement = xmldoc.createElement("InitTimingInfo");
        Element testbenchTypeElement = xmldoc.createElement("TestBenchType");
        Element testBenchLengthElement = xmldoc.createElement("TestBenchLength");
        Element timeScaleElement = xmldoc.createElement("TimeScale");
        Element singleClockInfoElement = xmldoc.createElement("SingleClockInfo");
        initTimingInfoElement.appendChild(testbenchTypeElement);
        initTimingInfoElement.appendChild(testBenchLengthElement);
        initTimingInfoElement.appendChild(timeScaleElement);        
        initTimingInfoElement.appendChild(singleClockInfoElement);
        
        testbenchTypeElement.setAttribute("type", "single");
        testBenchLengthElement.appendChild(xmldoc.createTextNode(String.valueOf(this.testBenchLength  / TimeScale.getMultiplier(this.timeScale))));
        timeScaleElement.appendChild(xmldoc.createTextNode(this.timeScale.name()));
        singleClockInfoElement.setAttribute("changeStateEdge", this.changeStateEdge.name());
        singleClockInfoElement.setAttribute("clockSignalName", this.clockSignalName);
        singleClockInfoElement.setAttribute("clockTimeHigh", String.valueOf(this.clockTimeHigh / TimeScale.getMultiplier(this.timeScale)));
        singleClockInfoElement.setAttribute("clockTimeLow", String.valueOf(this.clockTimeLow / TimeScale.getMultiplier(this.timeScale)));
        singleClockInfoElement.setAttribute("inputSetupTime", String.valueOf(this.inputSetupTime / TimeScale.getMultiplier(this.timeScale)));
        
        return initTimingInfoElement;
    }

    @Override
    public long getPeriodLength() {
        return this.clockTimeHigh + this.clockTimeLow;
    }
}
