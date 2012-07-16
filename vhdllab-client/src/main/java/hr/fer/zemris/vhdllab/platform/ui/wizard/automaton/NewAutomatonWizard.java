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
package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.applets.editor.automat.CodeGenerator;
import hr.fer.zemris.vhdllab.applets.editor.automaton.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Prijelaz;
import hr.fer.zemris.vhdllab.applets.editor.automaton.Stanje;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewAutomatonWizard extends AbstractNewFileWizard {

    private FileForm fileForm;
    private AutomatonInfoForm automatonForm;
    private PortWizardPage portWizardPage;

    @Override
    public void addPages() {
        fileForm = new FileForm();
        addForm(fileForm);

        automatonForm = new AutomatonInfoForm();
        addForm(automatonForm);

        portWizardPage = new PortWizardPage();
        portWizardPage.setMinimumPortCount(1);
        addPage(portWizardPage);
    }

    @Override
    protected File getFile() {
        return getFile(fileForm);
    }

    @Override
    protected FileType getFileType() {
        return FileType.AUTOMATON;
    }

    @Override
    protected String createData() {
        CircuitInterface ci = getCircuitInterface(fileForm, portWizardPage);
        AutomatonInfo automatonInfo = (AutomatonInfo) automatonForm
                .getFormObject();
        String ime = ci.getName();
        String tip = automatonInfo.getAutomatonType();
        String interfac = createInterface(ci);
        String pocSt = null;
        String rs = automatonInfo.getResetValue();
        String cl = automatonInfo.getClockValue();
        String s = String.valueOf(AUTPodatci.CONST_SIR);
        String v = String.valueOf(AUTPodatci.CONST_VIS);

        AUTPodatci pod = new AUTPodatci(ime, tip, interfac, pocSt, rs, cl, s, v);
        LinkedList<Stanje> stanja = new LinkedList<Stanje>();
        HashSet<Prijelaz> prijelazi = new HashSet<Prijelaz>();
        return new CodeGenerator().generateInternalCode(pod, prijelazi, stanja);
    }

    private String createInterface(CircuitInterface ci) {
        List<String> portStrings = new ArrayList<String>(ci.getPorts().size());
        for (Port port : ci.getPorts()) {
            StringBuilder sb = new StringBuilder(30);
            sb.append(port.getName()).append(" ");
            sb.append(port.getDirection().toString().toUpperCase()).append(" ");
            sb.append(port.getTypeName().toUpperCase());
            if (port.isVector()) {
                sb.append(" ");
                sb.append(port.getFrom()).append(" ").append(port.getTo());
            }
            portStrings.add(sb.toString());
        }
        return StringUtils.join(portStrings, "\n");
    }

}
