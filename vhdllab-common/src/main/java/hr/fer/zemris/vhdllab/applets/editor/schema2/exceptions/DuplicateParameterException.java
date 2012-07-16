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
package hr.fer.zemris.vhdllab.applets.editor.schema2.exceptions;



/**
 * Baca se u slucaju dodavanja vec postojeceg
 * parametra u parametarsku kolekciju.
 * 
 * @author Axel
 *
 */
public class DuplicateParameterException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DuplicateParameterException() {
		super();
	}

	public DuplicateParameterException(String arg0) {
		super(arg0);
	}

	public DuplicateParameterException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public DuplicateParameterException(Throwable arg0) {
		super(arg0);
	}

}
