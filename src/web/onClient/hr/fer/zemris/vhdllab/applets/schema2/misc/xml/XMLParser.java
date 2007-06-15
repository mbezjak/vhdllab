package hr.fer.zemris.vhdllab.applets.schema2.misc.xml;

import hr.fer.zemris.vhdllab.applets.schema2.exceptions.NotImplementedException;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
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
		InputStream is;
		
		try {
			is = new FileInputStream(filename);
		} catch (FileNotFoundException fnfe) {
			return null;
		}
		
		return parseXML(is);
	}
	
	public final XMLTree parseXMLFromString(String xml) {
		InputStream is = new StringBufferInputStream(xml);
		
		return parseXML(is);
	}
	
	public final XMLTree parseXMLFromStream(InputStream is) {
		return parseXML(is);
	}
	
	
	
	private final XMLTree parseXML(InputStream is) {
		Digester digester = new Digester();
		
		throw new NotImplementedException();
	}
	
	
	

}



















