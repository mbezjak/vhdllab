package hr.fer.zemris.vhdllab.applets.view.simulation;

import hr.fer.zemris.vhdllab.platform.manager.editor.PlatformContainer;
import hr.fer.zemris.vhdllab.platform.manager.simulation.SimulationAdapter;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.DefaultListModel;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JScrollPane;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.richclient.application.support.AbstractView;

/**
 * Panel koji sadrzi mozebitne greske prilikom kompajliranja VHDL koda.
 * 
 * @author Boris Ozegovic
 * @version 1.0
 * @since 22.12.2006.
 */
public class SimulationErrorsView extends AbstractView {

    @Autowired
    private PlatformContainer container;

    /** DefaultListModel */
    private DefaultListModel model;

    void setContent(Result result) {
        if (result.isSuccessful()) {
            // TODO ovo ucitat iz bundle-a
            Format formatter = new SimpleDateFormat("HH:mm:ss");
            String time = formatter.format(new Date());
            model.addElement(time + "  Simulation finished successfully.");
        } else {
            model.clear();
            for (String msg : result.getMessages()) {
                model.addElement(msg);
            }
        }
    }

    @Override
    protected JComponent createControl() {
        model = new DefaultListModel();
        JList listContent = new JList(model);
        listContent.setFixedCellHeight(15);

        container.getSimulationManager().addListener(new SimulationAdapter() {
            @SuppressWarnings("synthetic-access")
            @Override
            public void simulated(Result result) {
                setContent(result);
                getActiveWindow().getPage().setActiveComponent(
                        SimulationErrorsView.this);
            }
        });
        return new JScrollPane(listContent);
    }

}
