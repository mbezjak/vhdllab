package hr.fer.zemris.vhdllab.applets.editor.schema2.model.commands;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EComponentType;
import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.DuplicateKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.OverlapException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.UnknownKeyException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaPrototypeCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.XYLocation;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.CommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InvalidatedComponent;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;


/**
 * Ova ce komanda zamijeniti sve korisnicke komponente cije se sucelje
 * promijenilo otkako su stavljene u shemu s komponentama
 * u invalidiranom stanju. Takve komponente korisnik moze
 * iskljucivo obrisati prije nego ih zamijeni novima.
 * 
 * @author Axel
 *
 */
public class InvalidateObsoleteUserComponents implements ICommand {

	/* static fields */
	public static final String COMMAND_NAME = InvalidateObsoleteUserComponents.class.getSimpleName();

	/* private fields */
	private Set<CircuitInterface> circuits;
	

	/* ctors */
	
	
	/**
	 * Obavlja invalidaciju smatrajuci vazecima iskljucivo
	 * sucelja navedena u prototipu.
	 */
	public InvalidateObsoleteUserComponents(ISchemaPrototypeCollection prototyper) {
		circuits = new HashSet<CircuitInterface>();
		for (Entry<Caseless, ISchemaComponent> ntry : prototyper.getPrototypes().entrySet()) {
			circuits.add(ntry.getValue().getCircuitInterface());
		}
	}
	
	/**
	 * Obavlja invalidaciju smatrajuci vazecima iskljucivo
	 * sucelja navedena u ovoj listi.
	 */
	public InvalidateObsoleteUserComponents(List<CircuitInterface> userCircuits) {
		circuits = new HashSet<CircuitInterface>();
		for (CircuitInterface ci : userCircuits) {
			circuits.add(ci);
		}
	}
	

	/* methods */

	public String getCommandName() {
		return COMMAND_NAME;
	}
	
	public boolean isUndoable() {
		return false;
	}
	
	public ICommandResponse performCommand(ISchemaInfo info) {
		ISchemaComponentCollection components = info.getComponents();
		Set<ISchemaComponent> usercomps = components.fetchComponents(EComponentType.USER_DEFINED);
		Set<Caseless> toremove = new HashSet<Caseless>();
		
		for (ISchemaComponent cmp : usercomps) {
			if (!circuits.contains(cmp.getCircuitInterface())) {
				Caseless cmpname = cmp.getName();
				toremove.add(cmpname);
			}
		}
		for (Caseless remname : toremove) {
			ISchemaComponent cmp = components.fetchComponent(remname);
			XYLocation cmploc = components.getComponentLocation(remname);
			InvalidatedComponent invcmp = new InvalidatedComponent(
					remname.toString(), cmp.getWidth(), cmp.getHeight());
			
			try {
				components.removeComponent(remname);
			} catch (UnknownKeyException e) {
				throw new IllegalStateException("Component exists but cannot be found by it's name.");
			}
			try {
				components.addComponent(cmploc.x, cmploc.y, invcmp);
			} catch (DuplicateKeyException e) {
				throw new IllegalStateException("Old component under this name removed?");
			} catch (OverlapException e) {
				throw new IllegalStateException("Old component didn't overlap!");
			}			
		}
		
		return new CommandResponse(new ChangeTuple(EPropertyChange.CANVAS_CHANGE));
	}
	
	public ICommandResponse undoCommand(ISchemaInfo info)
	throws InvalidCommandOperationException
	{
		throw new InvalidCommandOperationException("Not undoable");
	}
}














