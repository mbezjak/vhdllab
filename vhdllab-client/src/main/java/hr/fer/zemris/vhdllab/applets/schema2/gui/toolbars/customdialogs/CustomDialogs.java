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
package hr.fer.zemris.vhdllab.applets.schema2.gui.toolbars.customdialogs;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Properties;





/**
 * Pomocu ove klase moguce je za dani tip objekta
 * dobiti odgovarajuci panel za njegovo postavljanje.
 * 
 * @author Axel
 *
 */
public class CustomDialogs {
	
	/* static fields */
	private static final String PROPERTIES_FILE = "customdialogs.properties";
	private static final CustomDialogs DIALOGS = new CustomDialogs();
	
	/* private fields */
	private Properties dialogproperties;

	
	
	/* ctors */

	private CustomDialogs() {
		dialogproperties = new Properties();
		try {
			dialogproperties.load(CustomDialogs.class.getResourceAsStream(PROPERTIES_FILE));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/* methods */

	/**
	 * Vraca panel za postavljanje danog tipa objekta.
	 * U slucaju da ne postoji panel za postavljanje danog tipa objekta,
	 * vraca se null.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ParameterSetterPanel<T> getDialogForObject(T objectvalue) {
		Class<?> objclass = objectvalue.getClass();
		String objname = objclass.getName();
		String dialogname = DIALOGS.dialogproperties.getProperty(objname);
		
		/* if there is no such dialog, return null */
		if (dialogname == null) return null;
		
		/* else, create a ParameterSetterPanel */
		ParameterSetterPanel<T> paramsetter = null;
		try {
			/* create object */
			Class<?> clazz = Class.forName(dialogname);
			Constructor<?> ctor = clazz.getConstructor((Class[])null);
			Object obj = ctor.newInstance((Object[])null);
			
			/* check if object is ok */
			if (!(obj instanceof ParameterSetterPanel<?>)) return null;
			ParameterSetterPanel<?> pspUnknown = (ParameterSetterPanel<?>) obj;
			if (!pspUnknown.isSettingValueFor().equals(objclass)) return null;
			paramsetter = (ParameterSetterPanel<T>) pspUnknown;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return paramsetter;
	}
	
	
	
}














