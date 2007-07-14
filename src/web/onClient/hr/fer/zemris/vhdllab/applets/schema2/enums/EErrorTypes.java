package hr.fer.zemris.vhdllab.applets.schema2.enums;


/**
 * Popis svih mogucih kodova pogresaka
 * kod scheme.
 * 
 * @author Axel
 *
 */
public enum EErrorTypes {
	/* special */
	NO_ERROR, UNKNOWN_TYPE,
	USER_DEFINED,
	
	/* normal */
	CANNOT_REDO, CANNOT_UNDO,
	DUPLICATE_COMPONENT_NAME,
	DUPLICATE_WIRE_NAME,
	NONEXISTING_PROTOTYPE,
	COMPONENT_OVERLAP,
	WIRE_OVERLAP,
	NONEXISTING_COMPONENT_NAME,
	NONEXISTING_WIRE_NAME,
	NONEXISTING_PORT_NAME,
	NONEXISTING_SEGMENT,
	PARAMETER_NOT_FOUND,
	PARAMETER_CONSTRAINT_BAN,
	MAPPING_ERROR,
	EVENT_NOT_COMPLETED
}
