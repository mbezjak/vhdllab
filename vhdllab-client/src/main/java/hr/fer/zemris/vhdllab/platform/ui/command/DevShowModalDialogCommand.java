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
package hr.fer.zemris.vhdllab.platform.ui.command;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.springframework.richclient.application.Application;
import org.springframework.richclient.command.ActionCommand;

public class DevShowModalDialogCommand extends ActionCommand {

    public static final String ID = "showModalDialogCommand";

    public DevShowModalDialogCommand() {
        super(ID);
    }

    @Override
    protected void doExecuteCommand() {
        Frame owner = Application.instance().getActiveWindow().getControl();
        JOptionPane.showMessageDialog(owner, "is it modal?");
    }

}
