package hr.fer.zemris.vhdllab.applets.view.simulation;

import hr.fer.zemris.vhdllab.api.results.SimulationMessage;
import hr.fer.zemris.vhdllab.api.results.SimulationResult;
import hr.fer.zemris.vhdllab.client.core.log.ResultTarget;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogListener;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractView;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.text.Format;
import java.text.SimpleDateFormat;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;

/**
 * Panel koji sadrzi mozebitne greske prilikom kompajliranja VHDL koda.
 * 
 * @author Boris Ozegovic
 * @version 1.0
 * @since 22.12.2006.
 */
public class SimulationErrorsView extends AbstractView {

	/**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7361269803493786758L;

	/** DefaultListModel */
	private DefaultListModel model;

	/** JList komponenta u koju ce se potrpati sve greske */
	private JList listContent;

	/** Panel sadrzi JScrollPane komponentu cime je omoguceno scrollanje */
	private JScrollPane scrollPane;

	private SystemLogListener systemLogListener;

	/**
	 * Constructor
	 * 
	 * Kreira objekt i dovodi ga u pocetno stanje ciji kontekst sadrzi prazan
	 * string
	 */
	public SimulationErrorsView() {
	}

	/**
	 * Glavna metoda koja uzima neki rezultat dobiven od strane servera.
	 * 
	 * @param resultTarget
	 *            rezultat koji ce ciniti kontekst panela s greskama
	 */
	public void setContent(ResultTarget<SimulationResult> resultTarget) {
		SimulationResult result = resultTarget.getResult();
		if (result.isSuccessful()) {
			// TODO ovo ucitat iz bundle-a
			Format formatter = new SimpleDateFormat("HH:mm:ss");
			String time = formatter.format(resultTarget.getDate());
			model.addElement(time + "  Simulation finished successfully.");
			return;
		}

		model.clear();
		for (SimulationMessage msg : result.getMessages()) {
			StringBuilder sb = new StringBuilder(
					msg.getMessageText().length() + 20);
			sb.append(msg.getEntityName()).append(":").append(
					msg.getMessageText());
			model.addElement(sb.toString());
		}
	}

	@Override
	public void doInit() {
		model = new DefaultListModel();
		listContent = new JList(model);
		// TODO ovo ucitat iz preference-a
		listContent.setFixedCellHeight(15);
		scrollPane = new JScrollPane(listContent);

		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(SimulationErrorsView.this.getSize());
			}
		});

		this.setLayout(new BorderLayout());
		this.add(scrollPane, BorderLayout.CENTER);
		
		systemLogListener = new SystemLogAdapter() {
			@Override
			public void simulationTargetAdded(ResultTarget<SimulationResult> result) {
				setContent(result);
			}
		};
		SystemLog.instance().addSystemLogListener(systemLogListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IView#dispose()
	 */
	@Override
	public void doDispose() {
		SystemLog.instance().removeSystemLogListener(systemLogListener);
	}
	
}