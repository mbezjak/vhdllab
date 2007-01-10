package hr.fer.zemris.vhdllab.applets.automat;

//TODO prijelaz i equals i kad se koristi lista i dialog i kad se stvara novi prijelaz koji ide iz-u...


import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
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
import java.util.ResourceBundle;

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
	 * legenda u kutu
	 */
	private String legenda=null;
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
	private int radijus=17;
	/**
	 * Stanja rada definirat ce dali se dodaje signal, dodaje prijelaz ili editira slika.
	 * stanjeRada=1 mjenjanje postojecih objekata
	 * stanjeRada=2 dodavanje novog stanja
	 * stanjeRada=3 dodavanje novog prijelaza (selekcija stanja iz)
	 * stanjeRada=4 dodavanje novog prijelaza (selekcija stanja u)
	 * stanjeRada=5 brisanje postojecih objekata
	 * stanjeRada=6 zadavanje pocetnog stanja
	 */
	private int stanjeRada=1;
	public boolean isModified=false;
	
	private ResourceBundle bundle=null;
	private ProjectContainer pContainer=null;
	
	/**
	 * konstruktor klase AutoDrawer, ne prima nikakve podatke, poziva createGUI() metodu
	 * @throws FileNotFoundException 
	 *
	 */
	
	public AutoDrawer() {
		super();
		this.setOpaque(true);
		createGUI();
		this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
		isModified=false;
	}
	
	/*public AutoDrawer(String strpodatci){
		super();
		this.setOpaque(true); 
		setData(strpodatci);
		if(podatci.ime!=null){
			createGUI();
			this.setPreferredSize(new Dimension(img.getWidth(),img.getHeight()));
			isModified=false;
		}
	}*/

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
			//Graphics2D gr=(Graphics2D)img.getGraphics();
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
		return new CodeGenerator().generateInternalCode(podatci,prijelazi,stanja);
	}

	/**
	 * Metoda setData propisan suceljem IEditor. Dobiva podatke sklopa i na osnovi njih inicijalizira kolekcije 
	 * stanja, prijelazi i podatci.
	 * @throws FileNotFoundException 
	 */
	public void setData(String data) {
		if(!data.equals("")){
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
		}else{
			stanja=new LinkedList<Stanje>();
			prijelazi=new HashSet<Prijelaz>();
			podatci=new AUTPodatci(AutoDrawer.this,pContainer,bundle);
		}
		if(podatci.ime!=null)
			parseLegend();
	}
	
	private void parseLegend() {
		String ulazi=new String("|");
		String izlazi=new String("|");
		String[] redovi=podatci.interfac.split("\n");
		for(int i=0;i<redovi.length;i++){
			String[] rijeci=redovi[i].split(" ");
			if(rijeci[1].toUpperCase().equals("IN")) ulazi=new StringBuffer().append(ulazi).append(rijeci[0]).append("|").toString();
			else izlazi=new StringBuffer().append(izlazi).append(rijeci[0]).append("|").toString();
		}
		legenda=new StringBuffer().append(bundle.getString(LanguageConstants.LEGEND_TITLE)).append(":\n")
		.append(bundle.getString(LanguageConstants.LEGEND_IN)).append(":")
		.append(ulazi).append("\n").append(bundle.getString(LanguageConstants.LEGEND_OUT))
		.append(":").append(izlazi).toString();
	}

	/**
	 * Ova metoda zasluzna je crtanje automata uz stanjeRada==4.
	 *@param eventx ako je stanjeRada==4 daje x kordinatu misa
	 *@param eventy ako je stanjeRada==4 daje y koordinatu misa
	 */
	private void nacrtajSklop(int eventx,int eventy){
		if(dataSet()){
		Graphics2D g=(Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(3,3,img.getWidth()-6,img.getHeight()-6);
		
		//crtanje stanja
		if(stanjeZaDodati!=null){
			g.setColor(stanjeZaDodati.boja);
			g.fillArc(stanjeZaDodati.ox,stanjeZaDodati.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(stanjeZaDodati.ox+radijus/5,stanjeZaDodati.oy+radijus/5,2*(radijus-radijus/5),2*(radijus-radijus/5),0,360);
		}
		for(Stanje st:stanja){
			g.setColor(st.boja);
			g.fillArc(st.ox,st.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(st.ox+radijus/5,st.oy+radijus/5,2*(radijus-radijus/5),2*(radijus-radijus/5),0,360);
			g.setColor(st.boja);
			if(st.ime.equals(podatci.pocetnoStanje)){
				g.drawLine(st.ox-17,st.oy+radijus,st.ox,st.oy+radijus);
				int[] xP=new int[3];
				int[] yP=new int[3];
				setXYP(xP,yP,st);
				g.fillPolygon(xP,yP,3);
			}
			
		//upis u stanja
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
		
		g.setColor(Color.BLACK);
		String[] legendic=legenda.split("\n");
		g.setFont(new Font("Arial", Font.BOLD, 10));
		FontMetrics fm= g.getFontMetrics();
		int odmak=legendic[0].length()>legendic[1].length()?
				(legendic[0].length()>legendic[2].length()?fm.stringWidth(legendic[0]):fm.stringWidth(legendic[2])):
				(legendic[1].length()>legendic[2].length()?fm.stringWidth(legendic[1]):fm.stringWidth(legendic[2]));
		int xStr=img.getWidth()-odmak-10;
		int yStr=fm.getHeight()+5;
		g.drawString(legendic[0],xStr,yStr);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		fm= g.getFontMetrics();
		yStr+=fm.getHeight();
		g.drawString(legendic[1],xStr,yStr);
		yStr+=fm.getHeight();
		g.drawString(legendic[2],xStr,yStr);
		
		repaint();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
		}
		
	}
	/**
	 * crta automat bez moguceg stanjaRada4
	 *
	 */
	private void nacrtajSklop(){
		if(dataSet()){
		Graphics2D g=(Graphics2D)img.getGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.fillRect(0,0,img.getWidth(),img.getHeight());
		g.setColor(Color.WHITE);
		g.fillRect(3,3,img.getWidth()-6,img.getHeight()-6);
		
		//crtanje stanja
		if(stanjeZaDodati!=null){
			g.setColor(stanjeZaDodati.boja);
			g.fillArc(stanjeZaDodati.ox,stanjeZaDodati.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(stanjeZaDodati.ox+radijus/5,stanjeZaDodati.oy+radijus/5,2*(radijus-radijus/5),2*(radijus-radijus/5),0,360);
		}
		for(Stanje st:stanja){
			g.setColor(st.boja);
			g.fillArc(st.ox,st.oy,2*radijus,2*radijus,0,360);
			g.setColor(Color.WHITE);
			g.fillArc(st.ox+radijus/5,st.oy+radijus/5,2*(radijus-radijus/5),2*(radijus-radijus/5),0,360);
			g.setColor(st.boja);
			if(st.ime.equals(podatci.pocetnoStanje)){
				g.drawLine(st.ox-17,st.oy+radijus,st.ox,st.oy+radijus);
				int[] xP=new int[3];
				int[] yP=new int[3];
				setXYP(xP,yP,st);
				g.fillPolygon(xP,yP,3);
			}
		//upis u stanja

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
		
		g.setColor(Color.BLACK);
		String[] legendic=legenda.split("\n");
		g.setFont(new Font("Arial", Font.BOLD, 10));
		FontMetrics fm= g.getFontMetrics();
		int odmak=legendic[0].length()>legendic[1].length()?
				(legendic[0].length()>legendic[2].length()?fm.stringWidth(legendic[0]):fm.stringWidth(legendic[2])):
				(legendic[1].length()>legendic[2].length()?fm.stringWidth(legendic[1]):fm.stringWidth(legendic[2]));
		int xStr=img.getWidth()-odmak-10;
		int yStr=fm.getHeight()+5;
		g.drawString(legendic[0],xStr,yStr);
		g.setFont(new Font("Arial", Font.PLAIN, 10));
		fm= g.getFontMetrics();
		yStr+=fm.getHeight();
		g.drawString(legendic[1],xStr,yStr);
		yStr+=fm.getHeight();
		g.drawString(legendic[2],xStr,yStr);
		
		repaint();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
	}
		
	}
	

	private boolean dataSet() {
		return (prijelazi!=null)&&(podatci!=null)&&(stanja!=null);
	}

	private void setXYP(int[] xp, int[] yp, Stanje st) {
		xp[0]=st.ox;
		xp[1]=st.ox-10;
		xp[2]=st.ox-10;
		yp[0]=st.oy+radijus;
		yp[1]=st.oy+radijus+6;
		yp[2]=st.oy+radijus-6;	
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
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
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
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
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
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
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

		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_OFF);
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
		final Prijelaz pr2=pr;
		JButton add=new JButton(bundle.getString(LanguageConstants.EDITOR_ADD));
		JButton delete=new JButton(bundle.getString(LanguageConstants.EDITOR_DELETE));
		JLabel poruka=new JLabel(bundle.getString(LanguageConstants.EDITOR_LIST)+":");
		
		final DefaultListModel listam=new DefaultListModel();
		for(String st:pr2.pobudaIzlaz)listam.addElement(st);
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
				String str=pomocni.editPrijelaz2(podatci,AutoDrawer.this,bundle);
				if(str!=null){
					pomocni.iz=pr2.iz;
					pomocni.pobudaIzlaz.add(str);
					if(listam.indexOf(str)==-1&&!pomocni.equals2(pomocni,prijelazi)){
						listam.addElement(str);
						pr2.pobudaIzlaz.add(str);
					} else pr2.porukaNeDodaj(AutoDrawer.this,bundle);
				}
			};
		});
		String ok_option=bundle.getString(LanguageConstants.DIALOG_BUTTON_OK);
		String cancel_option=bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL);
		String[] options={ok_option,cancel_option};
		
		delete.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(list.getSelectedIndex()>-1)
					if (listam.size()>1) {
						String pom=(String)list.getSelectedValue();
						listam.remove(list.getSelectedIndex());
						pr2.pobudaIzlaz.remove(pom);
					}
					else JOptionPane.showMessageDialog(AutoDrawer.this,bundle.getString(LanguageConstants.EDITOR_MESSAGE));
			}	
		});
		
		JOptionPane optionPane=new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,options[0]);
		JDialog dialog=optionPane.createDialog(this,bundle.getString(LanguageConstants.EDITOR_TITLE));
		dialog.setVisible(true);
		Object selected=optionPane.getValue();
		
		if(selected.equals(options[0])){
		 	pr=pr2;
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
				if(!isModified)isModified=true;
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
						st.editStanje2(podatci,AutoDrawer.this,bundle);
						if(!isModified)isModified=true;
						nacrtajSklop();
						break;
					}
				for(Prijelaz pr:prijelazi)
					if(jelSelektiran(e,pr)){
						editorPrijelaza(pr);
						if(!isModified)isModified=true;
						break;
					}
			}
			
			if(stanjeRada==2){
				if (e.getButton()==MouseEvent.BUTTON1){
					stanjeZaDodati.boja=Color.BLACK;
					boolean zastavica=true;
					boolean dodaj=true;
					while(zastavica){
						stanjeZaDodati.editStanje(podatci,AutoDrawer.this,bundle);
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
							String[] options={bundle.getString(LanguageConstants.DIALOG_BUTTON_YES),
									bundle.getString(LanguageConstants.DIALOG_BUTTON_NO)};
							JOptionPane pane= new JOptionPane(bundle.getString(LanguageConstants.DIALOG_MESSAGE_STATEEXISTS),
									JOptionPane.WARNING_MESSAGE,JOptionPane.YES_NO_OPTION,null,options,options[0]);
							JDialog dialog=pane.createDialog(AutoDrawer.this,bundle.getString(LanguageConstants.DIALOG_TITLE_WARNING));
							dialog.setVisible(true);
							Object reza=pane.getValue();
							if(reza.equals(options[1])){
								dodaj=false;
								stanjeRada=2;
								stanjeZaDodati=null;
								stanjeZaDodati=new Stanje();
								zastavica=false;
							}
						}
					}
					if(dodaj){
						stanja.add(stanjeZaDodati);
						if(!isModified)isModified=true;
					}
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
							prijelazZaDodati.editPrijelaz(podatci,AutoDrawer.this,bundle);
							boolean test=true;
							for(Prijelaz pr:prijelazi)
								if(pr.equals(prijelazZaDodati)){
									test=false;
									pr.dodajPodatak(prijelazZaDodati,prijelazi);
								}
							if(test&&prijelazZaDodati.pobudaIzlaz.size()!=0){
								if(!prijelazZaDodati.equals2(prijelazZaDodati,prijelazi)){
									prijelazi.add(prijelazZaDodati);
									}
							}else prijelazZaDodati.porukaNeDodaj(AutoDrawer.this,bundle);
							prijelazZaDodati=null;
							prijelazZaDodati=new Prijelaz();
							stanjeRada=3;
							if(!isModified)isModified=true;
							nacrtajSklop();
							break;
						}
			if(stanjeRada==5){
				for(Stanje st:stanja)
					if (jelSelektiran(e,st)) {
						if(st.ime.equals(podatci.pocetnoStanje))podatci.pocetnoStanje=null;
						brisiStanje(st);
						if(!isModified)isModified=true;
						nacrtajSklop();
						break;
					}	
				for(Prijelaz pr:prijelazi)
					if(jelSelektiran(e,pr)){
						brisiPrijelaz(pr);
						if(!isModified)isModified=true;
						nacrtajSklop();
						break;
					}
			}
			
			if(stanjeRada==6&&e.getButton()==MouseEvent.BUTTON1){
				for(Stanje st:stanja)
					if(jelSelektiran(e,st)){
						if(!podatci.pocetnoStanje.equals(st.ime)){
							podatci.pocetnoStanje=st.ime;
							if(!isModified)isModified=true;
						}
						break;
					}
				nacrtajSklop();
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


	public AUTPodatci getPodatci() {
		return podatci;
	}

	public void setResourceBundle(ProjectContainer pc,ResourceBundle bundle) {
		this.bundle=bundle;
		this.pContainer=pc;
	}

	
}
