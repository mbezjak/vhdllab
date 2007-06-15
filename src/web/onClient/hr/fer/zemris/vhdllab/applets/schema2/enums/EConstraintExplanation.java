package hr.fer.zemris.vhdllab.applets.schema2.enums;




public enum EConstraintExplanation {
	NO_INFORMATION() {
		@Override
		public String toString() {
			return "No information."; 
		}
		
		public boolean isAllowed() { return false; }
	},
	
	WRONG_TYPE() {
		@Override
		public String toString() {
			return "Wrong parameter value type.";
		}
	},
	
	NULL_PASSED() {
		@Override
		public String toString() {
			return "Null passed as value.";
		}
	},
	
	CONSTRAINT_BANNED() {
		@Override
		public String toString() {
			return "Value banned by constraint.";
		}
	},
	
	ALLOWED() {
		@Override
		public String toString() {
			return "Value is allowed.";
		}
		@Override
		public boolean isAllowed() {
			return true;
		}
	};
	
	public boolean isAllowed() {
		return false;
	}
}









