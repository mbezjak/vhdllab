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
package hr.fer.zemris.vhdllab.applets.editor.schema2.enums;


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
	INNER_QUERY_NOT_PERFORMED,
	EVENT_NOT_COMPLETED,
	COMPONENT_INVALIDATED,
	DUPLICATE_PROTOTYPE
}
