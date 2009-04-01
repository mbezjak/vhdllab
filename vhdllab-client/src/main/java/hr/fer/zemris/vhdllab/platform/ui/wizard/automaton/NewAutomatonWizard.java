package hr.fer.zemris.vhdllab.platform.ui.wizard.automaton;

import hr.fer.zemris.vhdllab.applets.editor.automat.AUTPodatci;
import hr.fer.zemris.vhdllab.applets.editor.automat.CodeGenerator;
import hr.fer.zemris.vhdllab.applets.editor.automat.Prijelaz;
import hr.fer.zemris.vhdllab.applets.editor.automat.Stanje;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.wizard.FormBackedWizardPage;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewAutomatonWizard extends AbstractNewFileWizard {

    private AutomatonInfoForm automatonForm;

    public NewAutomatonWizard() {
        super(BeanUtil.getBeanName(NewAutomatonWizard.class));
    }

    @Override
    protected void addPortWizardPage() {
        automatonForm = new AutomatonInfoForm(FormModelHelper
                .createFormModel(new AutomatonInfo()));
        addPage(new FormBackedWizardPage(automatonForm));

        super.addPortWizardPage();
        portPage.setMinimumPortCount(1);
    }

    @Override
    protected FileType getFileType() {
        return FileType.AUTOMATON;
    }

    @Override
    protected void onWizardFinished(Object formObject) {
        automatonForm.commit();
        super.onWizardFinished(formObject);
    }

    @Override
    protected String createData(CircuitInterface ci) {
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
