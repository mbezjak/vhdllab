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
package hr.fer.zemris.vhdllab.platform.support;

import hr.fer.zemris.vhdllab.platform.context.Environment;

import org.springframework.richclient.command.CommandGroup;
import org.springframework.richclient.command.CommandGroupFactoryBean;

public class SelectiveCommandGroupFactoryBean extends CommandGroupFactoryBean {

    @Override
    protected void initCommandGroupMembers(CommandGroup group) {
        Object[] members = getMembers();
        for (int i = 0; i < members.length; i++) {
            Object o = members[i];
            if (isDevelopmentMenu(o) && !Environment.isDevelopment()) {
                /*
                 * Development menu is only initialized in a development
                 * environment!
                 */
                getMembers()[i] = null;
            }
        }
        super.initCommandGroupMembers(group);
    }

    private boolean isDevelopmentMenu(Object o) {
        return o instanceof CommandGroup
                && ((CommandGroup) o).getId().equals("developmentMenu");
    }

}
