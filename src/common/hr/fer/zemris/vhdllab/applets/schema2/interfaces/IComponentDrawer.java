package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.misc.DrawingProperties;

import java.awt.Graphics2D;



/**
 * Sucelje koje sluzi za iscrtavanje sklopa
 * po nekom platnu.
 * Objekt koji implementira ovo sucelje vraca
 * svaka komponenta.
 * 
 * Drawer u nacelu ne smije mijenjati stanje
 * graphics objekta, ili ga u najgorem slucaju
 * smije ostaviti u istom stanju u kakvom ga je
 * dobio (npr. smije promijeniti stanje, ali ga
 * onda mora vratiti u prvotno).
 * 
 * Svaka implementacija Drawer-a MORA imati jedan
 * konstruktor koji prima ISchemaComponent.
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
	void draw(Graphics2D graphics, DrawingProperties properties);
}













