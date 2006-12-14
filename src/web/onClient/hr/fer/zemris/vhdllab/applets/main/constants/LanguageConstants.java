package hr.fer.zemris.vhdllab.applets.main.constants;

/**
 * Constants for helping to deal with resource bundles are declared here.
 * This constants contain only keys (not actual values) in resource bundles
 * which should be used to load values through resource bundle's get method.
 * In addition to keys this class may contain constants for language, country
 * and name of application resources.
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
	 * Key for 'File' menu.
	 */
	public static final String MENU_FILE = "menu.file";
	/**
	 * Key for 'Edit' menu.
	 */
	public static final String MENU_EDIT = "menu.edit";
	/**
	 * Key for 'View' menu.
	 */
	public static final String MENU_VIEW = "menu.view";
	/**
	 * Key for 'Tools' menu.
	 */
	public static final String MENU_TOOLS = "menu.tools";
	/**
	 * Key for 'Help' menu.
	 */
	public static final String MENU_HELP = "menu.help";

	
	/**
	 * Key for 'New' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_NEW = "menu.file.new";

	/**
	 * Key for 'Project' menu item in 'New' menu item.
	 */
	public static final String MENU_FILE_NEW_PROJECT = "menu.file.new.project";
	
	/**
	 * Key for 'VHDL Source' menu item in 'New' menu item.
	 */
	public static final String MENU_FILE_NEW_VHDL_SOURCE = "menu.file.new.vhdl.source";

	/**
	 * Key for 'Testbench' menu item in 'New' menu item.
	 */
	public static final String MENU_FILE_NEW_TESTBENCH = "menu.file.new.testbench";

	/**
	 * Key for 'Schema' menu item in 'New' menu item.
	 */
	public static final String MENU_FILE_NEW_SCHEMA = "menu.file.new.schema";

	/**
	 * Key for 'Automat' menu item in 'New' menu item.
	 */
	public static final String MENU_FILE_NEW_AUTOMAT = "menu.file.new.automat";

	/**
	 * Key for 'Open' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_OPEN = "menu.file.open";
	
	/**
	 * Key for 'Save' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_SAVE = "menu.file.save";

	/**
	 * Key for 'Save All' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_SAVE_ALL = "menu.file.save.all";

	/**
	 * Key for 'Close' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_CLOSE = "menu.file.close";

	/**
	 * Key for 'Close Other' menu item in editor popup menu.
	 */
	public static final String MENU_FILE_CLOSE_OTHER = "menu.file.close.other";

	/**
	 * Key for 'Close All' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_CLOSE_ALL = "menu.file.close.all";
	
	/**
	 * Key for 'Exit' menu item in 'File' menu.
	 */
	public static final String MENU_FILE_EXIT = "menu.file.exit";
	
	/**
	 * Key for 'Maximize Active Window' menu item in 'View' menu.
	 */
	public static final String MENU_VIEW_MAXIMIZE_ACTIVE_WINDOW = "menu.view.maximize.active.window";
	
	/**
	 * Key for 'Compile' menu item in 'Tools' menu.
	 */
	public static final String MENU_TOOLS_COMPILE = "menu.tools.compile";

	/**
	 * Key for 'Simulate' menu item in 'Tools' menu.
	 */
	public static final String MENU_TOOLS_SIMULATE = "menu.tools.simulate";


	/**
	 * Yes button in dialog.
	 */
	public static final String DIALOG_BUTTON_YES = "dialog.button.yes";
	/**
	 * No button in dialog.
	 */
	public static final String DIALOG_BUTTON_NO = "dialog.button.no";
	/**
	 * Cancel button in dialog.
	 */
	public static final String DIALOG_BUTTON_CANCEL = "dialog.button.cancel";
	
	/**
	 * Title for option dialog when trying to save resource.
	 */
	public static final String DIALOG_OPTION_SAVE_SINGLE_RESOURCE_TITLE = "dialog.option.save.single.resource.title";
	/**
	 * Message content for option dialog when trying to save resource.
	 */
	public static final String DIALOG_OPTION_SAVE_SINGLE_RESOURCE_MESSAGE = "dialog.option.save.single.resource.message";
	/**
	 * Title for option dialog when trying to save multiple resources.
	 */
	public static final String DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_TITLE = "dialog.option.save.multiple.resource.title";
	/**
	 * Message content for option dialog when trying to save multiple resources.
	 */
	public static final String DIALOG_OPTION_SAVE_MULTIPLE_RESOURCE_MESSAGE = "dialog.option.save.multiple.resource.message";

	/**
	 * Title for option dialog when trying to compile a file.
	 */
	public static final String DIALOG_OPTION_SAVE_RESOURCES_FOR_COMPILATION_TITLE = "dialog.option.save.resources.for.compilation.title";
	/**
	 * Message content for option dialog when trying to compile a file.
	 */
	public static final String DIALOG_OPTION_SAVE_RESOURCES_FOR_COMPILATION_MESSAGE = "dialog.option.save.resources.for.compilation.message";
	/**
	 * Title for option dialog when trying to simulate a file.
	 */
	public static final String DIALOG_OPTION_SAVE_RESOURCES_FOR_SIMULATION_TITLE = "dialog.option.save.resources.for.simulation.title";
	/**
	 * Message content for option dialog when trying to simulate a file.
	 */
	public static final String DIALOG_OPTION_SAVE_RESOURCES_FOR_SIMULATION_MESSAGE = "dialog.option.save.resources.for.simulation.message";

	
	/**
	 * Key for StatusBar when applet loading is completed.
	 */
	public static final String STATUSBAR_LOAD_COMPLETE = "statusbar.load.complete";
	/**
	 * Key for StatusBar when language setting in user file is not found.
	 */
	public static final String STATUSBAR_LANGUAGE_SETTING_NOT_FOUND = "statusbar.language.setting.not.found";
	/**
	 * Key for StatusBar when file already exists.
	 */
	public static final String STATUSBAR_EXISTS_FILE = "statusbar.exists.file";
	/**
	 * Key for StatusBar when project already exists.
	 */
	public static final String STATUSBAR_EXISTS_PROJECT = "statusbar.exists.project";
	/**
	 * Key for StatusBar when file could not be saved.
	 */
	public static final String STATUSBAR_CANT_SAVE_FILE = "statusbar.cant.save.file";
	/**
	 * Key for StatusBar when project could not be saved.
	 */
	public static final String STATUSBAR_CANT_SAVE_PROJECT = "statusbar.cant.save.project";
	/**
	 * Key for StatusBar when file could not be created.
	 */
	public static final String STATUSBAR_CANT_CREATE_FILE = "statusbar.cant.create.file";
	/**
	 * Key for StatusBar when project could not be created.
	 */
	public static final String STATUSBAR_CANT_CREATE_PROJECT = "statusbar.cant.create.project";
	/**
	 * Key for StatusBar when file content could not be loaded.
	 */
	public static final String STATUSBAR_CANT_LOAD_FILE_CONTENT = "statusbar.cant.load.file.content";
	/**
	 * Key for StatusBar when workspace could not be loaded.
	 */
	public static final String STATUSBAR_CANT_LOAD_WORKSPACE = "statusbar.cant.load.workspace";
	/**
	 * Key for StatusBar when compilation could not be run.
	 */
	public static final String STATUSBAR_CANT_COMPILE = "statusbar.cant.compile";
	/**
	 * Key for StatusBar when simulation could not be run.
	 */
	public static final String STATUSBAR_CANT_SIMULATE = "statusbar.cant.simulate";



}
