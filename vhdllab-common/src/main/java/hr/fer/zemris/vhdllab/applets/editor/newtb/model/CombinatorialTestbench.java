package hr.fer.zemris.vhdllab.applets.editor.newtb.model;

import hr.fer.zemris.vhdllab.applets.editor.newtb.enums.TimeScale;
import hr.fer.zemris.vhdllab.applets.editor.newtb.exceptions.UniformTestbenchException;

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
public class CombinatorialTestbench extends Testbench {
	
	public static class Properties
	{
		public long checkOutputsTime;
		public long assignInputsTime;
	}
	
	protected long checkOutputsTime;
	protected long assignInputsTime;

	public CombinatorialTestbench(String sourceName, long testBenchLength,
			TimeScale timeScale, Properties testbenchProperties) throws UniformTestbenchException {
		super(sourceName, testBenchLength, timeScale);
		
		if(testbenchProperties.assignInputsTime <= 0)
			throw new UniformTestbenchException("assignInputsTime mora biti veci ili jednak 1.");
		if(testbenchProperties.checkOutputsTime <= 0)
			throw new UniformTestbenchException("checkOutputsTime mora biti veci ili jednak 1.");
		
		this.assignInputsTime = testbenchProperties.assignInputsTime;
		this.checkOutputsTime = testbenchProperties.checkOutputsTime;
	}

	public long getCheckOutputsTime() {
		return checkOutputsTime;
	}

	public long getAssignInputsTime() {
		return assignInputsTime;
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
			return writer.toString();
		} catch (ParserConfigurationException ign1) {
		} catch (TransformerException ign2) {}

		return null;
	}

	@Override
	protected Element getInitTimingInfoElement(Document xmldoc) {
		Element initTimingInfoElement = xmldoc.createElementNS(null, "InitTimingInfo");
		Element testbenchTypeElement = xmldoc.createElementNS(null, "TestBenchType");
		Element testBenchLengthElement = xmldoc.createElementNS(null, "TestBenchLength");
		Element timeScaleElement = xmldoc.createElementNS(null, "TimeScale");
		Element combinatorialInfoElement = xmldoc.createElementNS(null, "CombinatorialInfo");
		initTimingInfoElement.appendChild(testbenchTypeElement);
		initTimingInfoElement.appendChild(testBenchLengthElement);
		initTimingInfoElement.appendChild(timeScaleElement);
		initTimingInfoElement.appendChild(combinatorialInfoElement);
		
		testbenchTypeElement.setAttribute("type", "combinatorial");
		testBenchLengthElement.appendChild(xmldoc.createTextNode(String.valueOf(this.testBenchLength / TimeScale.getMultiplier(this.timeScale))));
		timeScaleElement.appendChild(xmldoc.createTextNode(this.timeScale.name()));
		combinatorialInfoElement.setAttribute("checkOutputsTime", String.valueOf(this.checkOutputsTime / TimeScale.getMultiplier(this.timeScale)));
		combinatorialInfoElement.setAttribute("assignInputsTime", String.valueOf(this.assignInputsTime / TimeScale.getMultiplier(this.timeScale)));
		
		return initTimingInfoElement;
	}

	@Override
	public long getPeriodLength() {
		return this.assignInputsTime + this.checkOutputsTime;
	}
}
