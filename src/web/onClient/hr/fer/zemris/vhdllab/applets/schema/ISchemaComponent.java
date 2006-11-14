package hr.fer.zemris.vhdllab.applets.schema;
import java.awt.Dimension;
import java.awt.Point;


/**
 * Ovo je interface za apstraktni model
 * komponente koja se iscrtava na shemi.
 * 
 * @author Axel
 *
 */
public interface ISchemaComponent {
	/**
	 * Za iscrtavanje na komponente na gridu.
	 * Pretpostavlja se da su u adapteru vec
	 * pozvane metode setMagnificationFactor
	 * i setX i setY.
	 * Metode za iscrtavanje se pozivaju putem
	 * adaptera, a on onda brine o lokaciji
	 * komponente.
	 * @param adapter
	 */
	void draw(SchemaAdapter adapter);
	
	
	/**
	 * Ono sto nam je cesto bitno jest broj ulaznih
	 * portova sklopa.
	 * @return Broj ulaznih portova.
	 */
	int getNumberOfInPorts();
	
	
	/**
	 * Ono sto nam je cesto bitno jest broj izlaznih
	 * portova sklopa.
	 * @return Broj izlaznih portova.
	 */
	int getNumberOfOutPorts();
	
	
	/**
	 * Vraca koordinate za trazeni ulazni port.
	 * @param portNum
	 * Broj ulaznog porta cije koordinate zelimo.
	 * @return
	 * Koordinate trazenog porta.
	 */
	Point getInPortCoordinate(int portNum);
	
	
	/**
	 * Vraca koordinate za trazeni izlazni port.
	 * @param portNum
	 * Broj izlaznog porta cije koordinate zelimo.
	 * @return
	 * Koordinate trazenog porta.
	 */
	Point getOutPortCoordinate(int portNum);
	
	
	/**
	 * Za generiranje liste svojstava zadane
	 * komponente.
	 * @return Vraca ComponentPropertyList
	 * objekt koji sadrzi sva svojstva za
	 * tu komponentu (broj ulaza, izlaza, ime...).
	 */
	ComponentPropertyList getPropertyList();
}
