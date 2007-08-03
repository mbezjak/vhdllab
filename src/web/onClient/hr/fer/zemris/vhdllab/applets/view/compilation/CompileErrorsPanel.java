package hr.fer.zemris.vhdllab.applets.view.compilation;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IView;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ISystemContainer;
import hr.fer.zemris.vhdllab.vhdl.CompilationMessage;
import hr.fer.zemris.vhdllab.vhdl.CompilationResult;

import java.awt.BorderLayout;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/** 
 * Panel koji sadrzi mozebitne greske prilikom kompajliranja VHDL koda.
 *
 * @author Boris Ozegovic
 * @version 1.0
 * @since 22.12.2006.
 */
public class CompileErrorsPanel extends JPanel implements IView {
	
    /**
	 * Serial version UID.
	 */
	private static final long serialVersionUID = -7361269803493786758L;

	/** DefaultListModel */
    private DefaultListModel content;

    /** JList komponenta u koju ce se potrpati sve greske */
    private JList listContent;

    /** Panel sadrzi JScrollPane komponentu cime je omoguceno scrollanje */
    private JScrollPane scrollPane;
    
    /** SystemContainer */
    private ISystemContainer systemContainer;
    
    /** Editor */
    private IEditor editor;


    /**
     * Constructor 
     *
     * Kreira objekt i dovodi ga u pocetno stanje ciji kontekst sadrzi prazan
     * string
     */
    public CompileErrorsPanel()
    {
        super();
        content = new DefaultListModel();
        listContent = new JList(content);
        listContent.setFixedCellHeight(15);
        listContent.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent event) {}
            public void mouseReleased(MouseEvent event) {}
            public void mouseEntered(MouseEvent event){}
            public void mouseExited(MouseEvent event) {}
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2) {
                    String selectedValue = (String)listContent.getSelectedValue();
                    highlightError(selectedValue);
                }
            }
        });
        this.addComponentListener(new ComponentListener() {
			public void componentHidden(ComponentEvent e) {}
			public void componentMoved(ComponentEvent e) {}
			public void componentResized(ComponentEvent e) {
				scrollPane.setPreferredSize(CompileErrorsPanel.this.getSize());
			}
			public void componentShown(ComponentEvent e) {}
		});
        
        scrollPane = new JScrollPane(listContent);
        
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Glavna metoda koja uzima neki rezultat dobiven od strane servera.
     *
     * @param result rezultat koji ce ciniti kontekst panela s greskama
     */
    public void setContent(CompilationResult result) {
    	if(result.isSuccessful()) {
    		Format formatter = new SimpleDateFormat("HH:mm:ss");
    		String time = formatter.format(new Date());
    		content.addElement(time + "  Compilation finished successfully.");
    		return;
    	}
    	
    	content.clear();
        for(CompilationMessage msg : result.getMessages()) {
        	StringBuilder sb = new StringBuilder(msg.getMessageText().length() + 20);
        	sb.append(msg.getMessageEntity()).append(":")
        		.append(msg.getRow()).append(":")
        		.append(msg.getColumn()).append(":")
        		.append(msg.getMessageText());
            content.addElement(sb.toString());
        }
    }


    /**
     * Uzima string te se taj string usporeduje s uzorkom.  Ako uzorak postoji
     * unutar tog stringa, iz njega se vade ime datoteke i linija u kojoj se
     * dogodila greska i pozivaju se odgovarajuce metode za pozicioniranje na
     * konkretnu datoteku i liniju u kojoj se dogodila greska.  Ako uzorak ne
     * postoji ne dogada se nista.
     *
     * @param error Linija koju je generira VHDL simulator
     */
    public void highlightError(String error) {
        Pattern pattern = Pattern.compile("([^:]+):([^:]+):([^:]+):(.+)");
        Matcher matcher = pattern.matcher(error);
        if (matcher.matches()) {
    		String projectName = systemContainer.getSelectedProject();
    		if(projectName == null) {
    			// FIXME ovo stvarno treba drugacije napravit!!
    			systemContainer.echoStatusText("Select a project to highlight a line!", MessageEnum.Error);
    			return;
    		}
    		String fileName = matcher.group(1);
    		editor = systemContainer.getEditor(projectName, fileName);
			int temp = Integer.valueOf(matcher.group(2));
			editor.highlightLine(temp);
        }
    }

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#appendData(java.lang.Object)
	 */
	public void appendData(Object data) {
		setData(data);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setData(java.lang.Object)
	 */
	public void setData(Object data) {
		if(!(data instanceof CompilationResult)) {
			throw new IllegalArgumentException("Unknown data!");
		}
//		content.clear();
		setContent((CompilationResult)data);
	}

	/* (non-Javadoc)
	 * @see hr.fer.zemris.vhdllab.applets.view.IView#setProjectContainer(hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer)
	 */
	public void setSystemContainer(ISystemContainer container) {
		this.systemContainer = container;
	}
 
}