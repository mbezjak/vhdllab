package hr.fer.zemris.vhdllab.applets.main.constant;

/**
 * Constants for helping to deal with resource bundles are declared here.
 * This constants contain only keys (not actual values) in resource bundles
 * which should be used to load values through resource bundle's get method.
 * In addition to keys this class may contain constants for language, country
 * and name of application resources to be used.
 */
public class LanguageConstants {

	/**
	 * Application resources for main applet.
	 */
	public static final String APPLICATION_RESOURCES_NAME_MAIN = "Client_Main_ApplicationResources";

	/**
	 * Append this keyword to existing key to get its mnemonic value.
	 */
	public static final String MNEMONIC_APPEND = ".mnemonic";

	/**
	 * Append this keyword to existing key to get its accelerator value.
	 */
	public static final String ACCELERATOR_APPEND = ".accelerator";
	
	/**
	 * Append this keyword to existing key to get its tooltip value.
	 */
	public static final String TOOLTIP_APPEND = ".tooltip";

	
	/**
	 * Part of a key for view title. In addition to this key user needs
	 * to append one of view type to this key in order for key to be complete.
	 */
	public static final String VIEW_TITLE_FOR = "view.title.for.";
	
	public static final String MENU_FILE = "menu.file";
	public static final String MENU_EDIT = "menu.edit";
	public static final String MENU_VIEW = "menu.view";
	public static final String MENU_TOOLS = "menu.tools";
	public static final String MENU_HELP = "menu.help";

	public static final String MENU_FILE_NEW = "menu.file.new";
	public static final String MENU_FILE_NEW_PROJECT = "menu.file.new.project";
	public static final String MENU_FILE_NEW_VHDL_SOURCE = "menu.file.new.vhdl.source";
	public static final String MENU_FILE_NEW_TESTBENCH = "menu.file.new.testbench";
	public static final String MENU_FILE_NEW_SCHEMA = "menu.file.new.schema";
	public static final String MENU_FILE_NEW_AUTOMAT = "menu.file.new.automat";
	public static final String MENU_FILE_OPEN = "menu.file.open";
	public static final String MENU_FILE_SAVE = "menu.file.save";
	public static final String MENU_FILE_SAVE_ALL = "menu.file.save.all";
	public static final String MENU_FILE_CLOSE = "menu.file.close";
	public static final String MENU_FILE_CLOSE_OTHER = "menu.file.close.other";
	public static final String MENU_FILE_CLOSE_ALL = "menu.file.close.all";
	public static final String MENU_FILE_EXIT = "menu.file.exit";
	public static final String MENU_VIEW_MAXIMIZE_ACTIVE_WINDOW = "menu.view.maximize.active.window";
	public static final String MENU_VIEW_SHOW_VIEW = "menu.view.show.view";
	public static final String MENU_VIEW_SHOW_VIEW_COMPILATION_ERRORS = "menu.view.show.view.compilation.errors";
	public static final String MENU_VIEW_SHOW_VIEW_SIMULATION_ERRORS = "menu.view.show.view.simulation.errors";
	public static final String MENU_VIEW_SHOW_VIEW_STATUS_HISTORY = "menu.view.show.view.status.history";
	public static final String MENU_TOOLS_COMPILE_WITH_DIALOG = "menu.tools.compile.with.dialog";
	public static final String MENU_TOOLS_COMPILE_ACTIVE = "menu.tools.compile.active";
	public static final String MENU_TOOLS_COMPILE_HISTORY = "menu.tools.compile.history";
	public static final String MENU_TOOLS_SIMULATE_WITH_DIALOG = "menu.tools.simulate.with.dialog";
	public static final String MENU_TOOLS_SIMULATE_ACTIVE = "menu.tools.simulate.active";
	public static final String MENU_TOOLS_SIMULATE_HISTORY = "menu.tools.simulate.history";
	public static final String MENU_TOOLS_VIEW_VHDL_CODE = "menu.tools.view.vhdl.code";

	
	public static final String DIALOG_BUTTON_YES = "dialog.button.yes";
	public static final String DIALOG_BUTTON_NO = "dialog.button.no";
	public static final String DIALOG_BUTTON_OK = "dialog.button.ok";
	public static final String DIALOG_BUTTON_CANCEL = "dialog.button.cancel";
	public static final String DIALOG_BUTTON_SELECT_ALL = "dialog.button.select.all";
	public static final String DIALOG_BUTTON_DESELECT_ALL = "dialog.button.deselect.all";

	public static final String DIALOG_SAVE_CHECKBOX_ALWAYS_SAVE_RESOURCES = "dialog.save.checkbox.always.save.resources";
	public static final String DIALOG_SAVE_RESOURCES_TITLE = "dialog.save.resources.title";
	public static final String DIALOG_SAVE_RESOURCES_MESSAGE = "dialog.save.resources.message";
	public static final String DIALOG_SAVE_RESOURCES_FOR_COMPILATION_TITLE = "dialog.save.resources.for.compilation.title";
	public static final String DIALOG_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE = "dialog.save.resources.for.compilation.message";
	public static final String DIALOG_SAVE_RESOURCES_FOR_SIMULATION_TITLE = "dialog.save.resources.for.simulation.title";
	public static final String DIALOG_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE = "dialog.save.resources.for.simulation.message";

	public static final String DIALOG_SAVE_SIMULATION_TITLE = "dialog.save.simulation.title";
	public static final String DIALOG_SAVE_SIMULATION_MESSAGE = "dialog.save.simulation.message";

	public static final String DIALOG_CREATE_NEW_PROJECT_TITLE = "dialog.create.new.project.title";
	public static final String DIALOG_CREATE_NEW_PROJECT_MESSAGE = "dialog.create.new.project.message";

	public static final String DIALOG_RUN_CURRENT_PROJECT_LABEL = "dialog.run.current.project.label";
	public static final String DIALOG_RUN_ACTIVE_PROJECT_LABEL_NO_ACTIVE_PROJECT = "dialog.run.active.project.label.no.active.project";
	public static final String DIALOG_RUN_CURRENT_PROJECT_TITLE = "dialog.run.current.project.title";
	public static final String DIALOG_RUN_CHANGE_CURRENT_PROJECT_BUTTON = "dialog.run.change.current.project.button";
	public static final String DIALOG_RUN_COMPILATION_LIST_TITLE = "dialog.run.compilation.list.title";
	public static final String DIALOG_RUN_COMPILATION_TITLE = "dialog.run.compilation.title";
	public static final String DIALOG_RUN_SIMULATION_LIST_TITLE = "dialog.run.simulation.list.title";
	public static final String DIALOG_RUN_SIMULATION_TITLE = "dialog.run.simulation.title";

	
	public static final String STATUSBAR_LOAD_COMPLETE = "statusbar.load.complete";
	public static final String STATUSBAR_LANGUAGE_SETTING_NOT_FOUND = "statusbar.language.setting.not.found";
	public static final String STATUSBAR_EXISTS_FILE = "statusbar.exists.file";
	public static final String STATUSBAR_EXISTS_PROJECT = "statusbar.exists.project";
	public static final String STATUSBAR_FILE_SAVED = "statusbar.file.saved";
	public static final String STATUSBAR_FILE_SAVED_ALL = "statusbar.file.saved.all";
	public static final String STATUSBAR_FILE_CREATED = "statusbar.file.created";
	public static final String STATUSBAR_PROJECT_CREATED = "statusbar.project.created";
	public static final String STATUSBAR_NO_SELECTED_PROJECT = "statusbar.no.selected.project";
	public static final String STATUSBAR_CANT_SAVE_FILE = "statusbar.cant.save.file";
	public static final String STATUSBAR_CANT_SAVE_PROJECT = "statusbar.cant.save.project";
	public static final String STATUSBAR_CANT_CREATE_FILE = "statusbar.cant.create.file";
	public static final String STATUSBAR_CANT_CREATE_PROJECT = "statusbar.cant.create.project";
	public static final String STATUSBAR_CANT_LOAD_FILE_CONTENT = "statusbar.cant.load.file.content";
	public static final String STATUSBAR_CANT_LOAD_WORKSPACE = "statusbar.cant.load.workspace";
	public static final String STATUSBAR_CANT_COMPILE = "statusbar.cant.compile";
	public static final String STATUSBAR_CANT_SIMULATE = "statusbar.cant.simulate";
	public static final String STATUSBAR_COMPILED = "statusbar.compiled";
	public static final String STATUSBAR_SIMULATED = "statusbar.simulated";
	public static final String STATUSBAR_CANT_OPEN_VIEW = "statusbar.cant.open.view";
	public static final String STATUSBAR_CANT_VIEW_VHDL_CODE = "statusbar.cant.view.vhdl.code";
	
}