package hr.fer.zemris.vhdllab.applets.schema2.enums;


/**
 * Popis svih mogucih kodova pogresaka
 * kod scheme.
 * 
 * @author Axel
 *
 */
public enum EErrorTypes {
	CANNOT_REDO, CANNOT_UNDO,
	NO_ERROR, UNKNOWN_TYPE,
	DUPLICATE_COMPONENT_NAME,
	DUPLICATE_WIRE_NAME,
	NONEXISTING_PROTOTYPE,
	COMPONENT_OVERLAP,
	WIRE_OVERLAP,
	NONEXISTING_COMPONENT_NAME,
	NONEXISTING_WIRE_NAME,
	PARAMETER_NOT_FOUND,
	PARAMETER_CONSTRAINT_BAN
}
