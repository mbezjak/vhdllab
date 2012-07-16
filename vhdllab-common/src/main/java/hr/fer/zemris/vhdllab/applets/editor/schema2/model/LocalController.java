/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package hr.fer.zemris.vhdllab.applets.editor.schema2.model;

import hr.fer.zemris.vhdllab.applets.editor.schema2.enums.EPropertyChange;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.CommandExecutorException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions.InvalidCommandOperationException;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQuery;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.IQueryResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaController;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaCore;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.ChangeTuple;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;




/**
 * Lokalni kontroler - pretpostavlja
 * da su model i view unutar istog procesa.
 * 
 * @author Axel
 *
 */
public class LocalController implements ISchemaController {
	
	/* static fields */
	private static final int COMMAND_STACK_SIZE = 30;
	
	/* private fields */
	private PropertyChangeSupport support;
	private ISchemaCore core;
	private LinkedList<ICommand> undolist;
	private LinkedList<ICommand> redolist;
	private Map<IQuery, IQueryResult> querycache;
	private Map<EPropertyChange, List<IQuery>> queryindex;
	
	
	
	
	/* ctors */
	
	public LocalController() {
		support = new PropertyChangeSupport(this);
		core = null;
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
		querycache = new HashMap<IQuery, IQueryResult>();
		queryindex = new HashMap<EPropertyChange, List<IQuery>>();
	}
	
	public LocalController(ISchemaCore coreToSendTo) {
		support = new PropertyChangeSupport(this);
		core = coreToSendTo;
		undolist = new LinkedList<ICommand>();
		redolist = new LinkedList<ICommand>();
		querycache = new HashMap<IQuery, IQueryResult>();
		queryindex = new HashMap<EPropertyChange, List<IQuery>>();
	}
	
	
	
	
	
	

	
	/* methods */

	public void registerCore(ISchemaCore coreToSendTo) {
		core = coreToSendTo;
	}
	
	public void addListener(EPropertyChange changeType, PropertyChangeListener listener) {
		changeType.assignListenerToSupport(listener, support);
	}


	public void removeListener(PropertyChangeListener listener) {
		support.removePropertyChangeListener(listener);
	}
	
//	public void handleChanges(List<ChangeTuple> changes) {
//		if (changes != null) {
//			for (ChangeTuple change : changes) {
//				change.changetype.firePropertyChanges(support, change.oldval, change.oldval);
//			}
//		}
//	}

	public ISchemaInfo getSchemaInfo() {
		return core.getSchemaInfo();
	}
	
	public ICommandResponse send(ICommand command) {
		ICommandResponse response = core.executeCommand(command);
		if (response.isSuccessful()) {
			redolist.clear();
			if (command.isUndoable()) {
				undolist.add(command);
				if (undolist.size() > COMMAND_STACK_SIZE) undolist.removeFirst();
			} else {
				undolist.clear();
			}
		}
		
		reportChanges(response);

		return response;
	}
	
	public IQueryResult send(IQuery query) {
		boolean iscacheable = query.isCacheable();
		IQueryResult queryresult = (iscacheable) ? (querycache.get(query)) : (null);
		
		if (queryresult != null) return queryresult;
		
		queryresult = query.performQuery(core.getSchemaInfo());

		// cache query if successful
		if (queryresult.isSuccessful() && iscacheable) {
			querycache.put(query, queryresult);
			
			List<EPropertyChange> propdeps = query.getPropertyDependency();
			for (EPropertyChange propdep : propdeps) {
				List<IQuery> qlist = queryindex.get(propdep);
				
				if (qlist == null) {
					qlist = new ArrayList<IQuery>();
					queryindex.put(propdep, qlist);
				}
				qlist.add(query);
			}
		}
		
		return queryresult;
	}
	
	private void clearCacheFor(EPropertyChange propchange) {
		List<IQuery> tobedel = queryindex.get(propchange);
		
		if (tobedel == null) return;
		
		for (IQuery q : tobedel) {
			querycache.remove(q);
		}
		tobedel.clear();
	}

	private void reportChanges(ICommandResponse response) {
		List<ChangeTuple> changes = response.getPropertyChanges();
		if (changes != null) for (ChangeTuple ct : changes) {
			ct.changetype.firePropertyChanges(support, ct.oldval, ct.newval);
			clearCacheFor(ct.changetype);
		}
	}
	
	public boolean canRedo() {
		return (!redolist.isEmpty());
	}

	public boolean canUndo() {
		return (!undolist.isEmpty() && undolist.get(undolist.size() - 1).isUndoable());
	}

	public List<String> getRedoList() {
		List<String> list = new LinkedList<String>();
		for (ICommand comm : redolist) {
			list.add(comm.getCommandName());
		}
		return list;
	}

	public List<String> getUndoList() {
		List<String> list = new LinkedList<String>();
		for (ICommand comm : undolist) {
			list.add(comm.getCommandName());
		}
		return list;
	}

	public ICommandResponse redo() throws CommandExecutorException {
		if (redolist.isEmpty())
			throw new CommandExecutorException("Empty redo command stack.");
		ICommand comm = redolist.getLast();
		redolist.removeLast();

        ICommandResponse response = core.executeCommand(comm);
		if (!response.isSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot redo command.");
		}

		undolist.add(comm);
		
		reportChanges(response);

		return response;
	}

	public ICommandResponse undo() throws CommandExecutorException {
		if (undolist.isEmpty())
			throw new CommandExecutorException("Empty undo command stack.");
		ICommand comm = undolist.getLast();
		undolist.removeLast();

        ICommandResponse response;
        try {
            response = core.undoCommand(comm);
        } catch (InvalidCommandOperationException e) {
            throw new CommandExecutorException(e);
        }
		if (!response.isSuccessful()) {
			undolist.clear();
			redolist.clear();
			throw new CommandExecutorException("Cannot undo command. Reason: "
					+ response.getError().toString());
		}

		redolist.add(comm);
		
		reportChanges(response);

		return response;
	}

	public void clearCommandCache() {
		undolist.clear();
		redolist.clear();
	}

	public void clearQueryCache() {
		querycache.clear();
		queryindex.clear();
	}
	
	
	

}













