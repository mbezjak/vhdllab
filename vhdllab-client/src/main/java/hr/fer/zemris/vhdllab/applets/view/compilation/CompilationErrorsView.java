package hr.fer.zemris.vhdllab.applets.view.compilation;

import hr.fer.zemris.vhdllab.api.results.CompilationMessage;
import hr.fer.zemris.vhdllab.api.results.CompilationResult;
import hr.fer.zemris.vhdllab.platform.manager.compilation.CompilationListener;
import hr.fer.zemris.vhdllab.platform.manager.view.impl.AbstractView;

import java.awt.BorderLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
public class CompilationErrorsView extends AbstractView {

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

    /**
     * Constructor
     * 
     * Kreira objekt i dovodi ga u pocetno stanje ciji kontekst sadrzi prazan
     * string
     */
    public CompilationErrorsView() {
    }

    /**
     * Glavna metoda koja uzima neki rezultat dobiven od strane servera.
     * 
     * @param resultTarget
     *            rezultat koji ce ciniti kontekst panela s greskama
     */
    public void setContent(CompilationResult result) {
        if (result.isSuccessful()) {
            Format formatter = new SimpleDateFormat("HH:mm:ss");
            String time = formatter.format(new Date());
            model.addElement(time + "  Compilation finished successfully.");
            return;
        }

        model.clear();
        for (CompilationMessage msg : result.getMessages()) {
            StringBuilder sb = new StringBuilder(
                    msg.getMessageText().length() + 20);
            sb.append(msg.getEntityName()).append(":").append(msg.getRow())
                    .append(":").append(msg.getColumn()).append(":").append(
                            msg.getMessageText());
            model.addElement(sb.toString());
        }
    }

    /**
     * Uzima string te se taj string usporeduje s uzorkom. Ako uzorak postoji
     * unutar tog stringa, iz njega se vade ime datoteke i linija u kojoj se
     * dogodila greska i pozivaju se odgovarajuce metode za pozicioniranje na
     * konkretnu datoteku i liniju u kojoj se dogodila greska. Ako uzorak ne
     * postoji ne dogada se nista.
     * 
     * @param error
     *            Linija koju je generira VHDL simulator
     */
    public void highlightError(String error) {
        Pattern pattern = Pattern.compile("([^:]+):([^:]+):([^:]+):(.+)");
        Matcher matcher = pattern.matcher(error);
        if (matcher.matches()) {
            // FileIdentifier resource = resultTarget.getResource();
            // Editor editor;
            // if (container.getResourceManager().getFileType(
            // resource.getProjectName(), resource.getFileName()).equals(
            // FileType.SOURCE)) {
            // IComponentIdentifier<FileIdentifier> identifier =
            // ComponentIdentifierFactory
            // .createFileEditorIdentifier(resource);
            // editor = container.getEditorManager().getOpenedEditor(
            // identifier);
            // if (editor == null) {
            // editor = container.getEditorManager().openEditorByResource(
            // identifier);
            // }
            // } else {
            // IComponentIdentifier<FileIdentifier> identifier =
            // ComponentIdentifierFactory
            // .createViewVHDLIdentifier(resource);
            // editor = container.getEditorManager().viewVHDLCode(identifier);
            // }
            // int temp = Integer.valueOf(matcher.group(2));
            // editor.highlightLine(temp);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see hr.fer.zemris.vhdllab.applets.main.interfaces.IView#init()
     */
    @Override
    public void init() {
        model = new DefaultListModel();
        listContent = new JList(model);
        listContent.setFixedCellHeight(15);
        listContent.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String selectedValue = (String) listContent
                            .getSelectedValue();
                    highlightError(selectedValue);
                }
            }
        });
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scrollPane.setPreferredSize(CompilationErrorsView.this
                        .getSize());
            }
        });

        scrollPane = new JScrollPane(listContent);

        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);

        container.getCompilationManager().addListener(new CompilationListener() {
            @Override
            public void compiled(CompilationResult result) {
                setContent(result);
                getContainer().getViewManager().select(CompilationErrorsView.class);
            }
        });
    }

}