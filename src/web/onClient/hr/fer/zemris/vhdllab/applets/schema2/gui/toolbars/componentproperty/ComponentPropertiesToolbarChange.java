package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.componentproperty;

import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommand;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ICommandResponse;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.IParameter;
import hr.fer.zemris.vhdllab.applets.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.SetParameterCommand;
import hr.fer.zemris.vhdllab.applets.schema2.model.commands.SetParameterCommand.EParameterHolder;

public class ComponentPropertiesToolbarChange {
	/**
	 * Sluzi za debugiranje
	 */
	private static boolean DEBUG_MODE = true;

	private ISchemaInfo schemaInfo = null;
	private Caseless component = null;
	private String explanation = "";

	public ComponentPropertiesToolbarChange(Caseless component,
			ISchemaInfo schemaInfo) {
		this.component = component;
		this.schemaInfo = schemaInfo;

		if (DEBUG_MODE) {
			System.out.println("CPToolbarChange: constructor info");
			System.out.println("---component name:" + component.toString());
			System.out.println("---------------------------------------");
		}
	}

	public boolean ChangeProperty(IParameter parameter, Object value) {
		explanation = "";		
		ICommand command = new SetParameterCommand(component, parameter
				.getName(), EParameterHolder.component, value,
				schemaInfo);
		ICommandResponse response = command.performCommand(schemaInfo);

		if (DEBUG_MODE) {
			System.out.println("CPToolbarChange: parameterName:"
					+ parameter.getName() + ", newValue:" + value.toString());
			System.out.println("CPToolbarChange: commandName:"
					+ command.getCommandName());
			System.out.println("CPToolbarChange: responseIsSuccessful:"
					+ response.isSuccessful());
		}

		if (!response.isSuccessful()) {
			explanation = response.getError().getMessage();

			if (DEBUG_MODE) {
				System.out.println("CPToolbarChange: responseErrorExplanation:"
						+ explanation);
			}
			return false;
		}

		return true;
	}

	public String getExplanation() {
		return explanation;
	}
}
