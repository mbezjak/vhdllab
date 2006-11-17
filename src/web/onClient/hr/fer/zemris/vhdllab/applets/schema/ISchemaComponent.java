package hr.fer.zemris.vhdllab.applets.schema;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;


/**
 * Ovo je interface za apstraktni model
 * komponente koja se iscrtava na shemi.
 * 
 * @author Axel
 * @author Tommy
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
	public void draw(SchemaAdapter adapter);
	
	
	/**
	 * Ono sto nam je cesto bitno jest broj ulaznih
	 * portova sklopa.
	 * @return Broj ulaznih portova.
	 */
	public int getNumberOfInPorts();
	
	
	/**
	 * Ono sto nam je cesto bitno jest broj izlaznih
	 * portova sklopa.
	 * @return Broj izlaznih portova.
	 */
	public int getNumberOfOutPorts();
	
	
	/**
	 * Vraca koordinate za trazeni ulazni port.
	 * @param portNum
	 * Broj ulaznog porta cije koordinate zelimo.
	 * @return
	 * Koordinate trazenog porta.
	 */
	public Point getInPortCoordinate(int portNum);
	
	
	/**
	 * Vraca koordinate za trazeni izlazni port.
	 * @param portNum
	 * Broj izlaznog porta cije koordinate zelimo.
	 * @return
	 * Koordinate trazenog porta.
	 */
	public Point getOutPortCoordinate(int portNum);
	
	
	/**
	 * Za generiranje liste svojstava zadane
	 * komponente.
	 * @return Vraca ComponentPropertyList
	 * objekt koji sadrzi sva svojstva za
	 * tu komponentu (broj ulaza, izlaza, ime...).
	 */
	public ComponentPropertyList getPropertyList();
	
	/**
	 * Za dodavanje connection pointa komponente u
	 * connetion point manager koji se brine za iscrtavanje, povezivanje 
	 * i slicno....svih ostalih komponenti
	 */	
	public ArrayList<ConnectionPoint> getConnectionPoints();
}
