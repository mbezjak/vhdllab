package hr.fer.zemris.vhdllab.applets.schema2.enums;




public enum EPropertyChange {
	ANY_CHANGE,
	CANVAS_CHANGE() {
		@Override
		public String toString() {
			return "CANVAS_CHANGE";
		}
	},
	COMPONENT_PROPERTY_CHANGE() {
		@Override
		public String toString() {
			return "COMPONENT_PROPERTY_CHANGE";
		}
	}
}
