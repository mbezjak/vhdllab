package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.misc.DrawingProperties;
import java.awt.Graphics2D;




/**
 * Sucelje preko kojeg se iscrtavaju zice.
 * Vraca ga svaka zica.
 * 
 * Drawer u nacelu ne smije mijenjati stanje
 * graphics objekta, ili ga u najgorem slucaju
 * smije ostaviti u istom stanju u kakvom ga je
 * dobio (npr. smije promijeniti stanje, ali ga
 * onda mora vratiti u prvotno).
 * 
 * @author brijest
 *
 */
public interface IWireDrawer {

	/**
	 * Iscrtava zicu. Same informacije
	 * o zici koja se iscrtava (segmenti,
	 * broj i polozaj cvorova) sadrzi
	 * konkretna implementacija.
	 * 
	 * @param graphics
	 * Adapter za iscrtavanje.
	 */
	void draw(Graphics2D graphics, DrawingProperties properties);
}
