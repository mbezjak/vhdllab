package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;

import java.util.Map;



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
public interface ISchemaInfo extends ISerializable {
	
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
}














