package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.awt.Graphics2D;




/**
 * Sucelje preko kojeg se iscrtavaju zice.
 * Vraca ga svaka zica.
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
	void draw(Graphics2D graphics);
}
