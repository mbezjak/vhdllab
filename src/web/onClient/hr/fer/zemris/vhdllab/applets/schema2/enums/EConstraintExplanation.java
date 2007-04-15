package hr.fer.zemris.vhdllab.applets.schema2.enums;




public enum EConstraintExplanation {
	NO_INFORMATION() {
		@Override
		public String toString() {
			return "No information."; 
		}	
	},
	
	WRONG_TYPE() {
		@Override
		public String toString() {
			return "Wrong parameter value type.";
		}
	}
	
}
