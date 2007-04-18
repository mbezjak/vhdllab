package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import java.awt.Graphics2D;



/**
 * Sucelje koje sluzi za iscrtavanje sklopa
 * po nekom platnu.
 * Objekt koji implementira ovo sucelje vraca
 * svaka komponenta.
 * 
 * @author brijest
 *
 */
public interface IComponentDrawer {
	
	/**
	 * Iscrtava sklop. Same informacije
	 * o sklopu koji se iscrtava (velicina,
	 * broj i polozaj portova) sadrzi
	 * konkretna implementacija.
	 * 
	 * @param graphics
	 * Adapter za iscrtavanje.
	 */
	void draw(Graphics2D graphics);
}
