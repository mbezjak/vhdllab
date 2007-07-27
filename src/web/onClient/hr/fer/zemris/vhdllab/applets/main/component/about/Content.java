package hr.fer.zemris.vhdllab.applets.main.component.about;

import java.awt.Container;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JPanel;


public class Content extends JPanel {

	/* zanemari */
	private static final long serialVersionUID = -3241546902741454173L;

	/* uvijek uzimas pokazivac appleta da bi mogoa stavljati komponente na njega */
	private Container cp = this;

	private ImageIcon ozegovicIcon = new ImageIcon(getClass().getResource("ozegovic.png"));
	private ImageIcon delacIcon = new ImageIcon(getClass().getResource("delac.png"));
	private ImageIcon rajakovicIcon = new ImageIcon(getClass().getResource(
			"rajakovic.png"));
	private ImageIcon anaIcon = new ImageIcon(getClass().getResource("ana.png"));
	private ImageIcon bezjakIcon = new ImageIcon(getClass().getResource("bezjak.png"));
	private ImageIcon prokopecIcon = new ImageIcon(getClass().getResource("prokopec.png"));
	private ImageIcon alicIcon = new ImageIcon(getClass().getResource("alic.png"));
	private ImageIcon cupicIcon = new ImageIcon(getClass().getResource("cupic.png"));
	private ImageIcon cakmakIcon = new ImageIcon(getClass().getResource("cakmak.png"));
	private ImageIcon gjurkovicIcon = new ImageIcon(getClass().getResource(
			"gjurkovic.png"));


	public Content() {
		Person ozegovic = new Person("Boris Ožegović", ozegovicIcon, Description.OZEGOVIC.getString());
		Person bezjak = new Person("Miro Bezjak", bezjakIcon,Description.BEZJAK.getString());
		Person ana = new Person("Ana Vignjević", anaIcon, Description.VIGNJEVIC.getString());
		Person cupic = new Person("Marko Čupić", cupicIcon, Description.CUPIC.getString());
		Person delac = new Person("Davor Delač", delacIcon, Description.DELAC.getString());
		Person prokopec = new Person("Aleksandar Prokopec", prokopecIcon, Description.PROKOPEC.getString());
		Person alic = new Person("Ivan Alić", alicIcon, Description.ALIC.getString());
		Person rajakovic = new Person("Tomislav Rajaković", rajakovicIcon, Description.RAJAKOVIC.getString());
		Person cakmak = new Person("Jagor Čakmak", cakmakIcon, Description.CAKMAK.getString());
		Person gjurkovic = new Person("Matej Gjurković", gjurkovicIcon, Description.GJURKOVIC.getString());

		cp.setLayout(new BoxLayout(cp, BoxLayout.PAGE_AXIS));
		// SpringLayout layout = new SpringLayout();
		// cp.setLayout(layout);

		// / layout.putConstraint(SpringLayout.WEST, prvi, 5,SpringLayout.WEST,
		// cp);

		cp.add(cupic);
		cp.add(alic);
		cp.add(bezjak);
		cp.add(cakmak);
		cp.add(delac);
		cp.add(gjurkovic);
		cp.add(ozegovic);
		cp.add(prokopec);
		cp.add(rajakovic);
		cp.add(ana);
	}
}