package hr.fer.zemris.vhdllab.applets.automat;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Point2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import org.xml.sax.SAXException;
/**
 * 
 * @author ddelac
 *
 */
public class AutoDrawer extends JPanel{

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2847384905838330703L;
	/**
	 * buffer u kojem se crta slika sklopa
	 */
	private BufferedImage img = null;
	/**
	 * zastavica koja govori dali je pritisnut neki element
	 */
	private boolean pressed=false;
	/**
	 * kolekcija koja sadrzi sva stanja automata
	 */
	private LinkedList<Stanje> stanja=null;
	/**
	 * kolekcija koja sadrzi sve prijelaze automata
	 */
	private HashSet<Prijelaz> prijelazi=null;
	/**
	 * varijabla sa osnovnovnim podatcima o samom automatu
	 */
	private AUTPodatci podatci=null;
	
	/**
	 * selektirano stanje...
	 */
	private Stanje selektiran=null;
	
	/**
	 * stanje koje se dodaje
	 */
	private Stanje stanjeZaDodati=null;
	
	/**
	 * prijelaz koji se dodaje
	 */
	private Prijelaz prijelazZaDodati=null;
	/**
	 * radijus krugova za stanja
	 */
	private int radijus=25;
	
	/**
	 * Stanja rada definirat ce dali se dodaje signal, dodaje prijelaz ili editira slika.
	 * stanjeRada=1 mjenjanje postojecih objekata
	 * stanjeRada=2 dodavanje novog stanja
	 * stanjeRada=3 dodavanje novog prijelaza (selekcija stanja iz)
	 * stanjeRada=4 dodavanje novog prijelaza (selekcija stanja u)
	 * stanjeRada=5 brisanje postojecih objekata
	 */
	private int stanjeRada=1;

	
	/**
	 * konstruktor klase AutoDrawer, ne prima nikakve podatke, poziva createGUI() metodu
	 * @throws FileNotFoundException 
	 *
	 */
	
	public AutoDrawer(String strpodatci){
		super();
		this.setOpaque(true); 
		setData(strpodatci);
		createGUI();
		this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
	}

	/**
	 * ova metoda iscrtava za pocetak kako ce izgledati sklop
	 *
	 */
	private void createGUI() {
		
		img=new BufferedImage(this.getPreferredSize().width,this.getPreferredSize().height,BufferedImage.TYPE_3BYTE_BGR);
		nacrtajSklop();
		
		this.addMouseListener(new Mouse());
		this.addMouseMotionListener(new Mouse2());

		this.paintComponents(img.getGraphics());
		
	}
	
	/**
	 * metoda paintComponent klase JPanel overrideana
	 */
	protected void paintComponent(Graphics g) {
		if(img == null) {
			img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
			nacrtajSklop();
		} else {
			if (img.getHeight()!=this.getHeight()||img.getWidth()!=this.getWidth()){
				img=new BufferedImage(this.getWidth(),this.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
				nacrtajSklop();
			}
			g.drawImage(img,0,0,img.getWidth(),img.getHeight(),null);
		}
	}
	
	/**
	 * Metoda get data propisana suceljem IEditor. Vraca podatke o sklopu.
	 */
	public String getData() {
		// TODO naprvi get data!!!!
		return null;
	}

	/**
	 * Metoda setData propisan suceljem IEditor. Dobiva podatke sklopa i na osnovi njih inicijalizira kolekcije 
	 * stanja, prijelazi i podatci.
	 * @throws FileNotFoundException 
	 */
	public void setData(String data) {
		AUTParser aut=new AUTParser();
		try {
			aut.AUTParse(data);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		stanja=aut.stanja;
		prijelazi=aut.prijelazi;
		podatci=aut.podatci;
	}
	
	/**
	 * Ova metoda zasluzna je crtanje automata uz stanjeRada==4.
	 *@param eventx ako je stanjeRada==4 daje x kordinatu misa
	 *@param eventy ako je stanjeRada==4 daje y koordinatu misa
	 */
	private void nacrtajSklop(int eventx,int eventy){
		Graphics2D g=(Graphics2D)img.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(3,3,img.getWidth()-6,img.getHeight()-6);
		
		//crtanje stanja
		if(stanjeZaDodati!=null){
			g.setColor(stanjeZaDodati.boja);
			g.fillArc(stanjeZaDodati.ox,stanjeZaDodati.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(stanjeZaDodati.ox+5,stanjeZaDodati.oy+5,2*radijus-10,2*radijus-10,0,360);
		}
		for(Stanje st:stanja){
			g.setColor(st.boja);
			g.fillArc(st.ox,st.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(st.ox+5,st.oy+5,2*radijus-10,2*radijus-10,0,360);
			
		//upis u stanja
			g.setColor(st.boja);
			String tekst=null;
			if(podatci.tip.equals(new String("Moore")))
				tekst=new StringBuffer().append(st.ime).append("/").append(st.izlaz).toString();
			else if (podatci.tip.equals(new String("Mealy"))) 
				tekst=new StringBuffer().append(st.ime).toString();
			g.setFont(new Font("Helvetica", Font.BOLD, radijus/2));
			FontMetrics fm= g.getFontMetrics();
			int xString=st.ox+radijus-fm.stringWidth(tekst)/2;
			int yString=st.oy+radijus+fm.getAscent()/2;
			g.drawString(tekst,xString,yString);
			g.setColor(Color.WHITE);
		}
		
		//crtanje prijelaza
		for(Prijelaz pr:prijelazi){
			Stanje iz=null;
			Stanje ka=null;
			for(Stanje stanje:stanja){
				if(stanje.ime.equals(pr.iz)) iz=stanje;
				if(stanje.ime.equals(pr.u)) ka=stanje;
			}
			nacrtajPrijelaz(iz, ka,pr);
		}
		
		if(stanjeRada==4){
			for(Stanje st:stanja) if(st.ime==prijelazZaDodati.iz){
				g.setColor(Color.CYAN);
				g.drawLine(st.ox+radijus,st.oy+radijus,eventx,eventy);
				break;
			}
		}
		
		repaint();
		
	}
	/**
	 * crta automat bez moguceg stanjaRada4
	 *
	 */
	private void nacrtajSklop(){
		Graphics2D g=(Graphics2D)img.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(3,3,img.getWidth()-6,img.getHeight()-6);
		
		//crtanje stanja
		if(stanjeZaDodati!=null){
			g.setColor(stanjeZaDodati.boja);
			g.fillArc(stanjeZaDodati.ox,stanjeZaDodati.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(stanjeZaDodati.ox+5,stanjeZaDodati.oy+5,2*radijus-10,2*radijus-10,0,360);
		}
		for(Stanje st:stanja){
			g.setColor(st.boja);
			g.fillArc(st.ox,st.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(st.ox+5,st.oy+5,2*radijus-10,2*radijus-10,0,360);
			
		//upis u stanja
			g.setColor(st.boja);
			String tekst=null;
			if(podatci.tip.equals(new String("Moore")))
				tekst=new StringBuffer().append(st.ime).append("/").append(st.izlaz).toString();
			else if (podatci.tip.equals(new String("Mealy"))) 
				tekst=new StringBuffer().append(st.ime).toString();
			g.setFont(new Font("Helvetica", Font.BOLD, radijus/2));
			FontMetrics fm= g.getFontMetrics();
			int xString=st.ox+radijus-fm.stringWidth(tekst)/2;
			int yString=st.oy+radijus+fm.getAscent()/2;
			g.drawString(tekst,xString,yString);
			g.setColor(Color.WHITE);
		}
		
		//crtanje prijelaza
		for(Prijelaz pr:prijelazi){
			Stanje iz=null;
			Stanje ka=null;
			for(Stanje stanje:stanja){
				if(stanje.ime.equals(pr.iz)) iz=stanje;
				if(stanje.ime.equals(pr.u)) ka=stanje;
			}
			nacrtajPrijelaz(iz, ka,pr);
		}
		
		repaint();
		
	}
	

	/**
	 * metoda crta prijelaz iz stanja u stanje
	 * @param iz daje iz kojeg stanja
	 * @param ka daje u koje stanje
	 */
	private void nacrtajPrijelaz(Stanje iz, Stanje ka,Prijelaz pr){

		//racunanje polozaja tocki potrebnih za crtanje prijelaza...
		int x1,x2,y1,y2;
		double fi=0;
		if(ka.ox!=iz.ox){
			fi=Math.atan((double)(ka.oy-iz.oy)/(ka.ox-iz.ox));
			if(ka.ox<iz.ox)fi=Math.PI+fi;
		}else{
			fi=Math.PI/2;
			if(ka.oy<iz.oy)fi=Math.PI+fi;
		}
		double strOdm=0.2;
		x1=ka.ox+radijus-(int)(Math.cos(fi+strOdm)*radijus);
		y1=ka.oy+radijus-(int)(Math.sin(fi+strOdm)*radijus);
		x2=iz.ox+radijus+(int)(Math.cos(fi-strOdm)*radijus);
		y2=iz.oy+radijus+(int)(Math.sin(fi-strOdm)*radijus);
		Graphics2D g=(Graphics2D) img.getGraphics();
		if(ka.equals(selektiran)||iz.equals(selektiran)) g.setColor(Color.ORANGE); 
		else g.setColor(Color.BLACK);
		double l=radijus/2;
		int x3=(int)(Math.abs(x2+(x1-x2)/2)+Math.cos(Math.PI/2-fi)*l);
		int y3=(int)(Math.abs(y2+(y1-y2)/2)-Math.sin(Math.PI/2-fi)*l);
		if(ka.equals(iz)){
			strOdm=0.7;
			x1=ka.ox+radijus-(int)(Math.cos(fi+strOdm)*radijus);
			y1=ka.oy+radijus-(int)(Math.sin(fi+strOdm)*radijus);
			x2=iz.ox+radijus-(int)(Math.cos(fi-strOdm)*radijus);
			y2=iz.oy+radijus-(int)(Math.sin(fi-strOdm)*radijus);
			x3=(int)(Math.abs(x2+(x1-x2)/2));
			y3=(int)(Math.abs(y2+(y1-y2)/2)-7*l);
			strOdm=-0.2;
			
		}
		
		//crtanje prijelaza.....
		QuadCurve2D curve=new QuadCurve2D.Double();
		Point2D start, end ,control;
		start = new Point2D.Double();
		end = new Point2D.Double();
		control = new Point2D.Double();
		start.setLocation(x2,y2);
		end.setLocation(x1,y1);
		control.setLocation(x3,y3);

		curve.setCurve(start,control,end);		
		g.draw(curve);
		
		//upis podataka prijelaza
		upisiPodatkePrijelaza(iz,ka,pr,x3,y3,fi);
		
		//crtanje strijelica...
		int l2=radijus/2;
		int[] xstr = new int[3],ystr = new int[3];
		xstr[1]=(int) (x1-l2*Math.cos(fi+0.3+strOdm));
		ystr[1]=(int) (y1-l2*Math.sin(fi+0.3+strOdm));
		xstr[2]=(int) (x1-l2*Math.cos(fi-0.3+strOdm));
		ystr[2]=(int) (y1-l2*Math.sin(fi-0.3+strOdm));
		xstr[0]=x1;ystr[0]=y1;
		g.fillPolygon(xstr,ystr,3);

	}
	/**
	 * ispis podataka prijelaza
	 * @param iz stanje iz kojeg prijelaz ide
	 * @param ka stanje ui koje prijelaz ide
	 * @param pr podatci o priejelazu
	 * @param x3 polaozaj korfinate x konetrolne tocke krivulje prijelaza
	 * @param y3 polozaj kordinate y kontrolne tocke krivulje prijelaza
	 * @param fi 
	 */
	private void upisiPodatkePrijelaza(Stanje iz, Stanje ka, Prijelaz pr, int x3, int y3, double fi) {
		Graphics2D g=(Graphics2D) img.getGraphics();
		
		String tekst=pr.toString();
		g.setFont(new Font("Helvetica", Font.PLAIN, 2*radijus/5));
		
		
		Color cl=g.getColor();
		g.setColor(getMyColor(iz,ka));
		
		FontMetrics fm=g.getFontMetrics();
		int xtekst=x3-fm.stringWidth(tekst)/2;
		int ytekst=y3+fm.getAscent()/2;
		
		if(iz.equals(ka)) {
			ytekst+=(int)(3*radijus/2.2);
		}else{
			xtekst+=(int)((double)fm.stringWidth(tekst)/2*Math.sin(fi));
			ytekst-=(int)((double)fm.getAscent()/2*Math.cos(fi));
		}
		
		g.drawString(tekst,xtekst,ytekst);
		g.setColor(cl);
	}

	/**
	 * Odreduje koje ce boje biti tekst prijelaza
	 * @param iz stanje iz kojeg se prelazi
	 * @param ka stanje u koje se prelazi
	 * @return boja koja nam treba
	 */
	private Color getMyColor(Stanje iz, Stanje ka) {
		Color cl=Color.BLACK;
		if(selektiran!=null)
			if(selektiran.equals(iz)) cl=Color.RED;
			else if(selektiran.equals(ka)) cl=Color.BLUE;
		return cl;
	}
	
	@Override
	public Dimension getPreferredSize() {
		int x,y;
		if(this.getWidth()<100) x=100; else x=this.getWidth();
		if(this.getHeight()<100) y=100; else y=this.getHeight();
		return new Dimension(x,y);
	}

	/**
	 * Ova metoda provjerava dali je mis na podrucju selekcije prijelaza pr
	 * @param e MouseEvent koji se dogodio
	 * @param pr Prijelaz koji provjeravamo
	 * @return true ili false
	 */
	private boolean jelSelektiran(MouseEvent e,Prijelaz pr){
		Stanje ka=null,iz=null;
		for(Stanje st:stanja){
			if(st.ime.equals(pr.iz))iz=st;
			if(st.ime.equals(pr.u))ka=st;
		}
		
		int x1,x2,y1,y2;
		double fi=0;
		if(ka.ox!=iz.ox){
			fi=Math.atan((double)(ka.oy-iz.oy)/(ka.ox-iz.ox));
			if(ka.ox<iz.ox)fi=Math.PI+fi;
		}else{
			fi=Math.PI/2;
			if(ka.oy<iz.oy)fi=Math.PI+fi;
		}
		double strOdm=0.2;
		x1=ka.ox+radijus-(int)(Math.cos(fi+strOdm)*radijus);
		y1=ka.oy+radijus-(int)(Math.sin(fi+strOdm)*radijus);
		x2=iz.ox+radijus+(int)(Math.cos(fi-strOdm)*radijus);
		y2=iz.oy+radijus+(int)(Math.sin(fi-strOdm)*radijus);
		Graphics2D g=(Graphics2D) img.getGraphics();
		if(ka.equals(selektiran)||iz.equals(selektiran)) g.setColor(Color.ORANGE); 
		else g.setColor(Color.BLACK);
		double l=radijus/2;
		int x3=(int)(Math.abs(x2+(x1-x2)/2)+Math.cos(Math.PI/2-fi)*l);
		int y3=(int)(Math.abs(y2+(y1-y2)/2)-Math.sin(Math.PI/2-fi)*l);
		if(ka.equals(iz)){
			strOdm=0.7;
			x1=ka.ox+radijus-(int)(Math.cos(fi+strOdm)*radijus);
			y1=ka.oy+radijus-(int)(Math.sin(fi+strOdm)*radijus);
			x2=iz.ox+radijus-(int)(Math.cos(fi-strOdm)*radijus);
			y2=iz.oy+radijus-(int)(Math.sin(fi-strOdm)*radijus);
			x3=(int)(Math.abs(x2+(x1-x2)/2));
			y3=(int)(Math.abs(y2+(y1-y2)/2)-7*l);
			strOdm=-0.2;
			}

		String tekst=pr.toString();
		FontMetrics fm=g.getFontMetrics(new Font("Helvetica",Font.PLAIN,2*radijus/5));
		int xtekst=x3-fm.stringWidth(tekst)/2;
		int ytekst=y3+fm.getAscent()/2;
		
		if(iz.equals(ka)) {
			ytekst+=(int)(3*radijus/2.2);
		}else{
			xtekst+=(int)((double)fm.stringWidth(tekst)/2*Math.sin(fi));
			ytekst-=(int)((double)fm.getAscent()/2*Math.cos(fi));
		}
		if(e.getX()>xtekst&&e.getX()<xtekst+fm.stringWidth(tekst)&&e.getY()<ytekst&&e.getY()>ytekst-fm.getHeight())return true;
		else return false;
	}
	
	/**
	 * Ova metoda provjerava dali je mis na podrucju selekcije zadanog stanja
	 * @param e MouseEvent koji se dogodio
	 * @param st Stanje za koje provjeravamo
	 * @return true ili false
	 */
	private boolean jelSelektiran(MouseEvent e,Stanje st){
		int x1=e.getX();
		int y1=e.getY();
		return (Math.sqrt(Math.pow(x1-st.ox-radijus,2)+Math.pow(y1-st.oy-radijus,2)))<radijus;
	}
	



	public void setStanjeRada(int stanjeRada) {
		stanjeZaDodati=null;
		prijelazZaDodati=null;
		nacrtajSklop();
		this.stanjeRada = stanjeRada;
		if(stanjeRada==2)stanjeZaDodati=new Stanje();
/*		if(stanjeRada==2){
			boolean zastavica=true;
			while(zastavica){
				stanjeZaDodati=new Stanje(podatci,this);
				if(stanjeZaDodati.ime==null){
					this.stanjeRada=1;
					stanjeZaDodati=null;
					zastavica=false;
				}
				boolean z2=true;
				if(zastavica)
					for(Stanje st:stanja) if(st.equals(stanjeZaDodati))z2=false;
				if(z2)zastavica=false;
				else{
					int x=JOptionPane.showConfirmDialog(this,
							"Unjeli ste ime stanja koje vec postoji\nZelite li pokusat ponovo?",
							"Upozorenje",
							JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
					if(x==JOptionPane.NO_OPTION){
						this.stanjeRada=1;
						stanjeZaDodati=null;
						zastavica=false;
					}
				}
			}
		
		}*/
		if(stanjeRada==3)prijelazZaDodati=new Prijelaz();
	}

	public void brisiPrijelaz(Prijelaz pr) {
		prijelazi.remove(pr);
		nacrtajSklop();
	}

	public void brisiStanje(Stanje st) {
		HashSet<Prijelaz> pomocni=new HashSet<Prijelaz>();
		for(Prijelaz pr:prijelazi){
			if(st.ime.equals(pr.iz)||st.ime.equals(pr.u))pomocni.add(pr);
		}
		prijelazi.removeAll(pomocni);
		stanja.remove(st);
	}

	public void editorPrijelaza(Prijelaz pr) {
		JButton add=new JButton("Dodaj...");
		JButton delete=new JButton("Obrisi...");
		JLabel poruka=new JLabel("Popis prijelaza:");
		
		final DefaultListModel listam=new DefaultListModel();
		for(String st:pr.pobudaIzlaz)listam.addElement(st);
		final JList list=new JList(listam);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setVisibleRowCount(-1);
		list.setSelectedIndex(0);
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 80));
		
		JPanel panel=new JPanel(new BorderLayout());
		panel.add(listScroller,BorderLayout.CENTER);
		JPanel panel2=new JPanel(new GridLayout(2,1));
		panel2.add(add);
		panel2.add(delete);
		panel.add(panel2,BorderLayout.EAST);
		panel.add(poruka,BorderLayout.NORTH);
		
		add.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				Prijelaz pomocni=new Prijelaz();
				String str=pomocni.editPrijelaz2(podatci,AutoDrawer.this);
				if(listam.indexOf(str)==-1)listam.addElement(str);
			};
		});
		
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex()>-1)
					if (listam.size()>1) listam.remove(list.getSelectedIndex());
					else JOptionPane.showMessageDialog(AutoDrawer.this,"Mora ostati barem jedan prijelaz u listi!");
			}	
		});
		
		JOptionPane optionPane=new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog=optionPane.createDialog(this,"Editor Prijelaza");
		dialog.setVisible(true);
		Object selected=optionPane.getValue();
		
		if(selected.equals(JOptionPane.OK_OPTION)){
			pr.pobudaIzlaz.clear();
			for(int i=0;i<listam.getSize();i++) pr.pobudaIzlaz.add((String)listam.get(i));
			nacrtajSklop();
		}
	}
	
//****************************NESTED CLASESS****************************************
	
	/**
	 * Klasa koja se bavi pomicanjem misa i selektiranih elemenata. Implementira MouseMotionListener
	 * @author ddelac
	 *
	 */
	private class Mouse2 implements MouseMotionListener{
		
		/**
		 * Funkcija propisana suceljem, obavlja dio drag&drop funkcionalnosti
		 */
		public void mouseDragged(MouseEvent e) {
			if(pressed){
					selektiran.ox=e.getX()-radijus;
					selektiran.oy=e.getY()-radijus;
					if(selektiran.ox>img.getWidth()-2*radijus) selektiran.ox=img.getWidth()-2*radijus;
					if(selektiran.ox<0)selektiran.ox=0;
					if(selektiran.oy>img.getHeight()-2*radijus) selektiran.oy=img.getHeight()-2*radijus;
					if(selektiran.oy<0)selektiran.oy=0;
					nacrtajSklop();
			}
		}
		
		/**
		 * Funkcija propisana suceljem, ne radi nista...
		 */
		public void mouseMoved(MouseEvent e) {
			
			if(stanjeRada==2){
				stanjeZaDodati.ox=e.getX()-radijus;
				stanjeZaDodati.oy=e.getY()-radijus;
				if(stanjeZaDodati.ox>img.getWidth()-2*radijus) stanjeZaDodati.ox=img.getWidth()-2*radijus;
				if(stanjeZaDodati.ox<0)stanjeZaDodati.ox=0;
				if(stanjeZaDodati.oy>img.getHeight()-2*radijus) stanjeZaDodati.oy=img.getHeight()-2*radijus;
				if(stanjeZaDodati.oy<0)stanjeZaDodati.oy=0;
				nacrtajSklop();
			}
			
			if(stanjeRada==4){
				nacrtajSklop(e.getX(),e.getY());
			}
			
		}
		
	}
	
	
	private class Mouse implements MouseListener{

		/**
		 * Funkcija propisana suceljem, ne radi nista...
		 */
		public void mouseClicked(MouseEvent e) {
			
			if(stanjeRada==1&&e.getButton()==MouseEvent.BUTTON3){
				for(Stanje st:stanja)
					if(jelSelektiran(e,st)){
						st.editStanje2(podatci,AutoDrawer.this);
						nacrtajSklop();
						break;
					}
				for(Prijelaz pr:prijelazi)
					if(jelSelektiran(e,pr)){
						editorPrijelaza(pr);
						break;
					}
			}
			
			if(stanjeRada==2){
				if (e.getButton()==MouseEvent.BUTTON1){
					stanjeZaDodati.boja=Color.BLACK;
					boolean zastavica=true;
					boolean dodaj=true;
					while(zastavica){
						stanjeZaDodati.editStanje(podatci,AutoDrawer.this);
						if(stanjeZaDodati.ime==null){
							stanjeRada=2;
							dodaj=false;
							stanjeZaDodati=null;
							stanjeZaDodati=new Stanje();
							zastavica=false;
						}
						boolean z2=true;
						if(zastavica)
							for(Stanje st:stanja) if(st.equals(stanjeZaDodati))z2=false;
						if(z2)zastavica=false;
						else{
							int x=JOptionPane.showConfirmDialog(AutoDrawer.this,
									"Unjeli ste ime stanja koje vec postoji\nZelite li pokusat ponovo?",
									"Upozorenje",
									JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
							if(x==JOptionPane.NO_OPTION){
								dodaj=false;
								stanjeRada=2;
								stanjeZaDodati=null;
								stanjeZaDodati=new Stanje();
								zastavica=false;
							}
						}
					}
					if(dodaj)stanja.add(stanjeZaDodati);
					stanjeRada=1;
					stanjeZaDodati=null;
					setStanjeRada(2);
					nacrtajSklop();
				}

			}
			
			if(stanjeRada==3&&e.getButton()==MouseEvent.BUTTON1){
				for(Stanje st:stanja)
					if(jelSelektiran(e,st)){
						prijelazZaDodati.iz=st.ime;
						stanjeRada=4;
						break;
					} }
			else if(stanjeRada==4&&e.getButton()==MouseEvent.BUTTON1)
					for(Stanje sta:stanja)
						if(jelSelektiran(e,sta)){
							prijelazZaDodati.u=sta.ime;
							prijelazZaDodati.editPrijelaz(podatci,AutoDrawer.this);
							boolean test=true;
							for(Prijelaz pr:prijelazi)
								if(pr.equals(prijelazZaDodati)){
									test=false;
									pr.dodajPodatak(prijelazZaDodati);
								}
							if(test&&prijelazZaDodati.pobudaIzlaz.size()!=0)prijelazi.add(prijelazZaDodati);
							prijelazZaDodati=null;
							prijelazZaDodati=new Prijelaz();
							stanjeRada=3;
							nacrtajSklop();
							break;
						}
			if(stanjeRada==5){
				for(Stanje st:stanja)
					if (jelSelektiran(e,st)) {
						brisiStanje(st);
						nacrtajSklop();
						break;
					}	
				for(Prijelaz pr:prijelazi)
					if(jelSelektiran(e,pr)){
						brisiPrijelaz(pr);
						nacrtajSklop();
						break;
					}
			}
			
			/*if(e.getButton()==MouseEvent.BUTTON3){
				stanjeZaDodati=null;
				prijelazZaDodati=null;
				stanjeRada=1;
				nacrtajSklop();
			}*/
		}
		
		/**
		 * Funkcija propisana suceljem, obavlja dio drag&drop funkcionalnosti
		 */
		public void mousePressed(MouseEvent e) {
			if(stanjeRada==1)
			if(e.getButton()==MouseEvent.BUTTON1 )
				for(Stanje st:stanja)
					if(jelSelektiran(e,st)){
						st.boja=Color.GREEN;
						pressed=true;
						selektiran=st;
						nacrtajSklop();
						break;
					}
			
		}

		/**
		 * Funkcija propisana suceljem, obavlja dio drag&drop funkcionalnosti
		 */
		public void mouseReleased(MouseEvent e) {	
			
			if(pressed){
				selektiran.boja=Color.BLACK;
				selektiran=null;
				nacrtajSklop();
				pressed=false;
			}
			
		}

		/**
		 * nis ne radi...
		 */
		public void mouseEntered(MouseEvent e) {

			}

		/**
		 * nis ne radi...
		 */
		public void mouseExited(MouseEvent e) {

		}
		
	}


}
