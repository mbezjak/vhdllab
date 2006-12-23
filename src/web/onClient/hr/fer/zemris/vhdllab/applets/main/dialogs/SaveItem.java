package hr.fer.zemris.vhdllab.applets.main.dialogs;
    /**
     * Represents an item that should be displayed in SaveDialog. 
     * 
     * @author Miro Bezjak
     * @see SaveDialog
     */
    public class SaveItem {
    	
    	private String projectName;
    	private String fileName;
    	private boolean selected;
    	
    	/**
    	 * Constructor.
    	 * @param selected whether checkbox should be selected or not
    	 * @param projectName a name of a project
    	 * @param fileName a name of a file
    	 */
    	public SaveItem(boolean selected, String projectName, String fileName) {
    		this.selected = selected;
    		this.projectName = projectName;
    		this.fileName = fileName;
		}

		/**
		 * Getter for selected;
		 * @return value of selected
		 */
    	public boolean isSelected() {
			return selected;
		}
		/**
		 * Setter for selected.
		 * @param selected value to be set
		 */
		public void setSelected(boolean selected) {
			this.selected = selected;
		}
		
		/**
		 * Getter for file name.
		 * @return file name
		 */
		public String getFileName() {
			return fileName;
		}

		/**
		 * Getter for project name.
		 * @return project name
		 */
		public String getProjectName() {
			return projectName;
		}

		/**
		 * Creates text out of project name and file name.
		 * @return created text
		 */
		public String getText() {
			return fileName + " [" + projectName + "]";
		}
    	
    }