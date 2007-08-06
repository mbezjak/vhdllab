package hr.fer.zemris.vhdllab.applets.schema2.interfaces;

import hr.fer.zemris.vhdllab.applets.schema2.constants.CCommandNames;
import hr.fer.zemris.vhdllab.applets.schema2.exceptions.InvalidCommandOperationException;



/**
 * Interface koji propisuje nacin slanja komandi
 * interfaceu ISchemaCore.
 * 
 * @author Axel
 *
 */
public interface ICommand {
	
	/**
	 * Vraca jedinstveno ime za neku implementaciju
	 * ICommanda.
	 * 
	 * @return
	 * Vraca jedinstveno ime klase komandi.
	 * Pritom, programer je odgovoran da to ime zaista
	 * bude jedinstveno.
	 * @see CCommandNames
	 */
	String getCommandName();
	
	/**
	 * Odreduje da li je navedena komanda,
	 * da se tako izrazimo, 'povratljiva'.
	 * Mislim da ne trebam detaljno komentirat.
	 * 
	 * @return
	 * True ako se moze obaviti undo ove komande,
	 * false inace.
	 * Treba imati na umu da u slucaju da je moguce
	 * napraviti undo, komande je moguce slagati
	 * u listu, te pozivom metode undoCommand()
	 * obavljati reverznu operaciju, proizvoljan
	 * broj puta, ili do pojave komande koja nije
	 * undoable.
	 * 
	 */
	boolean isUndoable();
	
	/**
	 * Radi jednu ili vise promjena na ISchemaInfo
	 * objektu, te vraca objekt koji govori
	 * o tome da li je promjena izvedena uspjesno ili
	 * ne.
	 * OPREZ: Ako je komanda neuspjesno izvedena, onda ona
	 * NE SMIJE niposto napraviti bilo kakvu promjenu na info
	 * objektu - ovo je bitno za one koji implementiraju
	 * ovo sucelje. 
	 * 
	 * @param info
	 * Objekt koji sadrzi informacije o sklopovima,
	 * zicama, itd.
	 * Na njemu ce biti izvrsene promjene.
	 * 
	 * @return
	 * Objekt koji govori o uspjesnosti komande.
	 */
	ICommandResponse performCommand(ISchemaInfo info);
	
	/**
	 * Obavlja inverznu operaciju - undo.
	 * 
	 * @param info
	 * Objekt na kojem se obavlja undo.
	 * @return
	 * Objekt koji govori o uspjesnosti obavljanja
	 * inverzne operacije.
	 */
	ICommandResponse undoCommand(ISchemaInfo info) throws InvalidCommandOperationException;
	
}













