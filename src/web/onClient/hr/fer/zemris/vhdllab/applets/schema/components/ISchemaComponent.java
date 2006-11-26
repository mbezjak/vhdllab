package hr.fer.zemris.vhdllab.applets.schema.components;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaDrawingAdapter;


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
	public void draw(SchemaDrawingAdapter adapter);
	
	
	/**
	 * Ono sto nam je cesto bitno jest broj portova.
	 * @return Broj ulaznih portova.
	 */
	public int getNumberOfPorts();
	
	
	/**
	 * Vraca sve informacije o pojedinom portu.
	 * @param portNum
	 * Broj porta cije koordinate zelimo.
	 * @return
	 * Koordinate trazenog porta.
	 */
	public AbstractSchemaPort getSchemaPort(int portIndex);
	
	
	/**
	 * Za generiranje liste svojstava zadane
	 * komponente.
	 * @return Vraca ComponentPropertyList
	 * objekt koji sadrzi sva svojstva za
	 * tu komponentu (broj ulaza, izlaza, ime...).
	 */
	public ComponentPropertyList getPropertyList();
	
	/**
	 * Sirina komponente - za crtanje.
	 */
	public int getComponentWidth();
	
	/**
	 * Visina komponente - za crtanje.
	 */
	public int getComponentHeight();
}
