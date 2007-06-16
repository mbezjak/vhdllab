package hr.fer.zemris.vhdllab.applets.schema2.misc.xml;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringBufferInputStream;
import java.io.StringReader;

import org.apache.commons.digester.Digester;







public class XMLParser {
	/* static fields */

	/* private fields */

	
	
	/* ctors */
	
	public XMLParser() {
	}

	
	
	
	/* methods */
	
	public final XMLTree parseXMLFromFile(String filename) {
		InputStreamReader isr;
		
		try {
			isr = new FileReader(filename);
		} catch (FileNotFoundException fnfe) {
			return null;
		}
		
		return parseXML(isr);
	}
	
	public final XMLTree parseXMLFromString(String xml) {
		Reader reader = new StringReader(xml);
		
		return parseXML(reader);
	}
	
	public final XMLTree parseXMLFromStream(InputStreamReader isr) {
		return parseXML(isr);
	}
	
	
	
	private final XMLTree parseXML(Reader reader) {
		Digester digester = new Digester();
		
		throw new NotImplementedException();
	}
	
	
	

}



















