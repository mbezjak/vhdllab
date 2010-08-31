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
package hr.fer.zemris.vhdllab.platform.i18n;

import java.util.Locale;

import org.apache.commons.lang.StringUtils;

public abstract class LocalizationSupport extends AbstractLocalizationSource {

    protected static final char PART_SEPARATOR = '.';

    private String cached;

    protected final String getMessageCodeFromSimpleClassName() {
        if (cached == null) {
            String className = getClass().getSimpleName();
            String altered = alterSimpleClassName(className);
            StringBuilder code = new StringBuilder(altered.length() + 30);
            String[] split = StringUtils.splitByCharacterTypeCamelCase(altered);
            for (String part : split) {
                part = part.toLowerCase(Locale.ENGLISH);
                code.append(part).append(PART_SEPARATOR);
            }
            if (split.length > 0) {
                code.deleteCharAt(code.length() - 1);
            }
            cached = alterMessageCode(code.toString());
        }
        return cached;
    }

    protected String alterSimpleClassName(String simpleClassName) {
        return simpleClassName;
    }

    protected String alterMessageCode(String code) {
        return code;
    }

}
