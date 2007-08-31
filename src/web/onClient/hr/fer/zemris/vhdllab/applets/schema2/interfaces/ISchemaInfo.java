package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Rect2d;
import hr.fer.zemris.vhdllab.applets.schema2.misc.XYLocation;

import java.util.Map;

import com.sun.org.apache.regexp.internal.recompile;



/**
 * Definira izgled wrappera za
 * podatke relevantne schematicu. 
 * Sluzi za pristup svim podacima
 * unutar sheme - komponentama, zicama,
 * informaciji o broju sklopova, itd.
 * 
 * @author Axel
 *
 */
public interface ISchemaInfo {
	
	/**
	 * Vraca objekt koji sadrzi sve komponente.
	 * 
	 * @return
	 */
	ISchemaComponentCollection getComponents();
	
	
	/**
	 * Postavlja objekt koji sadrzi sve komponente.
	 * 
	 * @param components
	 */
	void setComponents(ISchemaComponentCollection components);
	
	
	/**
	 * Vraca objekt koji sadrzi sve zice (signale).
	 * 
	 * @return
	 */
	ISchemaWireCollection getWires();
	
	
	/**
	 * Postavlja objekt koji sadrzi sve zice.
	 * 
	 * @param wires
	 */
	void setWires(ISchemaWireCollection wires);
	
	
	/**
	 * Vraca objekt koji sadrzi CircuitInterface
	 * modeliranog objekta, i jos neke dodatne
	 * podatke, npr. parametre generic bloka.
	 * 
	 * @return
	 */
	ISchemaEntity getEntity();
	
	/**
	 * Vraca listu prototipova u shemi.
	 * 
	 * @return
	 */
	Map<Caseless, ISchemaComponent> getPrototypes();
	
	/**
	 * Vraca objekt s kojim je moguce instancirati
	 * nove komponente.
	 * 
	 * @return
	 */
	ISchemaPrototypeCollection getPrototyper();
	
	/**
	 * Vraca identifikator koji dosad nije koristen u schemi.
	 * 
	 * @return
	 */
	Caseless getFreeIdentifier();
	
	/**
	 * Vraca identifikator koji dosad nije koristen u schemi.
	 * 
	 * @param offered
	 * Ponudeni identifikator.
	 * @return
	 * Ako ponudeni identifikator nije koristen, tada je on
	 * povratna vrijednost metode (ne kopija).
	 * Inace se vraca identifikator koji nije koristen u schemi,
	 * ali je 'slican' ponudenom (slican => ovisno o implementaciji).
	 * 
	 */
	Caseless getFreeIdentifier(Caseless offered);
	
	/**
	 * Provjerava da li je identifikator koristen u schemi.
	 * 
	 * @param id
	 * @return
	 */
	boolean isFreeIdentifier(Caseless identifier);
	
	/**
	 * Vraca bounding box unutar kojeg stanu sve komponente i zice na shemi.
	 */
	Rect2d boundingBox();
	
}














