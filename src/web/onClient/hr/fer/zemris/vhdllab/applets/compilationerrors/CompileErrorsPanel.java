package hr.fer.zemris.vhdllab.applets.compilationerrors;


import java.awt.Dimension;
import java.awt.Graphics;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;



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
{
    private static final long serialVersionUID = 8;
    /** Sadrzi sve greske koje je generirao VHDL simulator */
    private String[] compileErrors;

    /** DefaultListModel */
    private DefaultListModel context = new DefaultListModel();

    /** JList komponenta u koju ce se potrpati sve greske */
    private JList listContext = new JList(context);

    /** Panel sadrzi JScrollPane komponentu cime je omoguceno scrollanje */
    private JScrollPane scrollPane = new JScrollPane(listContext);

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
                String selectedValue = (String)listContext.getSelectedValue();
                highlightError(selectedValue);
            }
        }
    };    


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

        listContext.addMouseListener(mouseListener);
        listContext.addListSelectionListener(listener);
        listContext.setFixedCellHeight(15);
    }


    /**
     * Glavna metoda koja uzima neki string dobiven od strane servera u
     * obliku kojeg je generirao VHDL simulator.
     *
     * @param compileErrors String koji ce ciniti kontekst panela s greskama
     */
    public void setContext(String compileErrors)
    {
        final String LIMITER = "###";
        this.compileErrors = compileErrors.split(LIMITER);

        /* prazni listu prije novog upisa */
        context.clear();
        for (String string : this.compileErrors)
        {
            context.addElement(string);
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
    public void clearContext()
    {
        context.clear();   
    }


    /**
     * Postavlja zeljeni preferredSize() konteksta, odnosno liste koja sadrzi
     * kontekst.  Postavljajuci preferredSize panela, postavit ce zeljenu
     * velicinu panela, ali ce ostaviti dimenzije liste netaknute, sto ponekad
     * nece biti dovoljno.  
     *
     * @param dimension Zeljene dimenzije
     */
    public void setContextSize(Dimension dimension)
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
            //TODO pozovi Mirinu metodu za postavljanje aktivnog taba
            // prvo naravno provjera svih aktivnih tabova, ako postoji takav
            // tab idi na njega i odi na tu liniju, inace nista. 
            System.out.println(matcher.group(1)); 
            System.out.println(matcher.group(2)); 
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
}
