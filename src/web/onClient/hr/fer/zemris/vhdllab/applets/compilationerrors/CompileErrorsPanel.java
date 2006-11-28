package hr.fer.zemris.vhdllab.applets.compilationerrors;


import hr.fer.zemris.vhdllab.applets.main.interfaces.FileContent;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;



/** 
 * Panel koji sadrzi mozebitne greske prilikom kompajliranja VHDL koda.  Panel
 * ce primiti string preko HHTP-a, koji je zapisan u internom formatu, pa je
 * prije svega potrebno parsirati string i izdvojiti linije koje je generirao
 * VHDL simulator.  Linije cine kontekst panela i ispisuju se u panel, odnosnu u
 * JList komponentu koju ce sadrzavati panel. Ako nema nikakvih gresaka ispisuje
 * se "Compilation was succesful".  Takoder je zamisljeno da se prilikom svakog
 * poziva 'Compile' izbrise kontekst panela, tako da bi nakon svakog
 * kompajliranja korisnik u svakom trenutku znao trenutnu stanje aplikacije.
 *
 * @author Boris Ozegovic
 * @version 1.0
 * @date 21.9.2006.
 */
public class CompileErrorsPanel extends JPanel
	implements IWizard, IEditor
{
    private static final long serialVersionUID = 8;
    /** Sadrzi sve greske koje je generirao VHDL simulator */
    private String[] compileErrors;

    /** DefaultListModel */
    private DefaultListModel content = new DefaultListModel();

    /** JList komponenta u koju ce se potrpati sve greske */
    private JList listContent = new JList(content);

    /** Panel sadrzi JScrollPane komponentu cime je omoguceno scrollanje */
    private JScrollPane scrollPane = new JScrollPane(listContent);
    
    /** FileContent */
    private FileContent fileContent;
    
    /** ProjectContainer */
    private ProjectContainer projectContainer;
    
    /** VHDL editor */
    private VHDLEditor editor;


    /**
     * Constructor 
     *
     * Kreira objekt i dovodi ga u pocetno stanje ciji kontekst sadrzi prazan
     * string
     */
    public CompileErrorsPanel()
    {
        super();
        compileErrors = new String[1];
        compileErrors[0] = "";
        this.add(scrollPane);

        listContent.addMouseListener(mouseListener);
        listContent.addListSelectionListener(listener);
        listContent.setFixedCellHeight(15);
    }

    public String getData() 
    {
		return null;
	}


	public String getFileName() 
	{
		return fileContent.getFileName();
	}


	public String getProjectName() 
	{
		return fileContent.getProjectName();
	}


	public IWizard getWizard() 
	{
		return null;
	}


	public boolean isModified() 
	{
		return false;
	}


	public void setFileContent(FileContent fContent) 
	{
		this.fileContent = fContent;
		setContent(fileContent.getContent());
	}


	public FileContent getInitialFileContent() 
	{
		return null;
	}


	public void setProjectContainer(ProjectContainer pContainer) 
	{
		this.projectContainer = pContainer;
	}


	public void setupWizard() 
	{
		;
	}
	

	/** 
     * Listener koji osluskuje selekciju liste i na temelju selekcije vraca
     * indeks selektiranog retka, te se pozicionira na redak .vhdl datoteke u
     * kojoj se nalazi greska
     */
    private ListSelectionListener listener = new ListSelectionListener()
    {
        public void valueChanged(ListSelectionEvent e)
        {
           
        }
    };


    /**
     * Slusa dvoklik misa, te na temelju selekcije u listi vraca selektiranu
     * vrijednosti, tj. string koji je zapravo greska.  Greska se tada parsira
     * tako da se uzima ime datoteke u kojoj se nalazi greska, te broj linije u
     * kojoj se greska dogodila.
     */
    private MouseListener mouseListener = new MouseListener()
    {

        public void mousePressed(MouseEvent event) 
        {
            ; 
        }

        public void mouseReleased(MouseEvent event) 
        {
            ;
        }

        public void mouseEntered(MouseEvent event) 
        {
            ;
        }

        public void mouseExited(MouseEvent event) 
        {
            ;
        }

        public void mouseClicked(MouseEvent event) 
        {
            int clickCount = event.getClickCount();

            if (clickCount == 2)
            {
                String selectedValue = (String)listContent.getSelectedValue();
                highlightError(selectedValue);
            }
        }
    };    
    

    /**
     * Glavna metoda koja uzima neki string dobiven od strane servera u
     * obliku kojeg je generirao VHDL simulator.
     *
     * @param compileErrors String koji ce ciniti kontekst panela s greskama
     */
    public void setContent(String compileErrors)
    {
        final String LIMITER = "###";
        this.compileErrors = compileErrors.split(LIMITER);

        /* prazni listu prije novog upisa */
        content.clear();
        for (String string : this.compileErrors)
        {
            content.addElement(string);
        }
    }


    /**
     * Metoda se koristi za praznjenje konteksta.  Zamisljeno je da se kontekst
     * prazni prije svakog poziva 'Compile' tako da korisnik u svakom trenutku
     * zna je li kompajliranje u tijeku/proslo kako spada/ili je bilo gresaka.
     * <strong>Ako class user i ne pozove ovu metodu <code>setContext()</code>
     * stvar ce se napraviti kako spada</strong>, ali ako se pozove dvaput
     * 'Compile' i oba puta bude "Compilation was succesful" korisnik nece biti
     * siguran je li kompilacija uopce sprovedena ili ne.
     */
    public void clearContent()
    {
        content.clear();   
    }


    /**
     * Postavlja zeljeni preferredSize() konteksta, odnosno liste koja sadrzi
     * kontekst.  Postavljajuci preferredSize panela, postavit ce zeljenu
     * velicinu panela, ali ce ostaviti dimenzije liste netaknute, sto ponekad
     * nece biti dovoljno.  
     *
     * @param dimension Zeljene dimenzije
     */
    public void setContentSize(Dimension dimension)
    {
        this.scrollPane.setPreferredSize(dimension);
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
    public void highlightError(String error)
    {
        Pattern pattern = Pattern.compile("([^:]+):([^:]+):([^:]+):(.+)");
        Matcher matcher = pattern.matcher(error);
        if (matcher.matches())
        {
        	// TODO mozda jos eventualna provjera postoji li uopce otvoren tab
        	projectContainer.openFile(fileContent.getProjectName(), matcher.group(1));
        	Integer temp = Integer.valueOf(matcher.group(2));
        	editor.highlightLine(temp.intValue());
        }
    }


    /**
     * Metoda koja crta panel
     *
     *@param g Graphics objekt
     */
    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
    }
    
    public void highlightLine(int line) {
    	// TODO Auto-generated method stub
    	
    }

	public void setReadOnly(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public void setSavable(boolean flag) {
		// TODO Auto-generated method stub
		
	}

	public boolean isReadOnly() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isSavable() {
		// TODO Auto-generated method stub
		return false;
	}
}
