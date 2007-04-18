package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.util.List;



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
	 * Vraca objekt u kojem je moguce pohranjivati
	 * propertye vezane uz shemu.
	 * 
	 * @return
	 */
	ISchemaProperties getProperties();
	
	
	/**
	 * Vraca objekt koji sadrzi CircuitInterface
	 * modeliranog objekta, i jos neke dodatne
	 * podatke, npr. parametre generic bloka.
	 * 
	 * @return
	 */
	ISchemaEntity getEntity();
	
	
	/**
	 * Vraca listu imena prototipova sklopova unutar sheme.
	 * 
	 * @return
	 */
	List<String> getPrototypeNames();
	
	/**
	 * Vraca listu prototipova u shemi.
	 * 
	 * @return
	 */
	List<ISchemaComponent> getPrototypes();
}














