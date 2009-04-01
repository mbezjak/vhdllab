package hr.fer.zemris.vhdllab.applets.editor.schema2.misc;



/**
 * Enkapsulira postavke iscrtavanja komponenti i zica.
 * Predaje se pri pozivu <code>draw()</code> sucelja <code>IComponentDrawer</code>
 * i <code>IWireDrawer.</code>
 * Nije nuzno da se odredena implementacija sucelja <code>IComponentDrawer</code>
 * ili <code>IWireDrawer</code> drzi svih postavki iscrtavanja u slucaju
 * da se postavka da naslutiti iz samog crteza. Na primjer, ako se nazivi portova
 * daju naslutiti iz samog crteza komponente, onda odredena implementacija drawer-a
 * moze zaobici postavku ispisivanja imena portova.
 * 
 * Odredeni podatkovni clanovi ostavljeni su kao javni kako bi se omogucio
 * brzi pristup postavkama i samim time brze iscrtavanje. Duznost je programera
 * da pri implementaciji drawer-a NE MIJENJA te vrijednosti.
 * 
 * @author Axel
 *
 */
public class DrawingProperties {
	
	/* static fields */
	
	
	/* private fields */
	public boolean drawingPortNames;
	public boolean drawingComponentNames;
	public boolean drawingWireNames;
	
	
	/* ctors */

	public DrawingProperties() {
		drawingPortNames = true;
		drawingComponentNames = true;
		drawingWireNames = true;
	}
	
	
	/* methods */

	
	/**
	 * Postavlja ispis imena portova u slucaju da se radi o
	 * komponenti.
	 * @param drawingPortNames
	 */
	public void setDrawingPortNames(boolean drawingPortNames) {
		this.drawingPortNames = drawingPortNames;
	}
	
	/**
	 * Da li se ispisuju imena portova ako se radi o komponenti.
	 */
	public boolean isDrawingPortNames() {
		return drawingPortNames;
	}

	/**
	 * Postavlja iscrtavanje imena komponente.
	 * @param drawingNames
	 */
	public void setDrawingComponentNames(boolean drawingNames) {
		this.drawingComponentNames = drawingNames;
	}

	/**
	 * Da li se iscrtava ime komponente.
	 */
	public boolean isDrawingComponentNames() {
		return drawingComponentNames;
	}

	/**
	 * Postavlja iscrtavanje imena zice.
	 * @param drawingWireNames
	 */
	public void setDrawingWireNames(boolean drawingWireNames) {
		this.drawingWireNames = drawingWireNames;
	}
	
	/**
	 * Da li se iscrtava ime zice.
	 */
	public boolean isDrawingWireNames() {
		return drawingWireNames;
	}
	
}














