package hr.fer.zemris.vhdllab.applets.automat;

import java.awt.Component;
import java.awt.GridLayout;
import java.util.TreeSet;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Prijelaz {
	public String iz;
	public String u;
	public TreeSet<String> pobudaIzlaz;
	
	public Prijelaz(String iz,String u,String pobuda,String izlaz){
		super();
		this.iz=iz;
		this.u=u;
		pobudaIzlaz=new TreeSet<String>();
		String pomocni=pobuda;
		if(izlaz!=null)pomocni=new StringBuffer().append("/").append(izlaz).toString();
		pobudaIzlaz.add(pomocni);
	}
	public Prijelaz() {
		super();
		pobudaIzlaz=new TreeSet<String>();
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean pov=true;
		Prijelaz prj=(Prijelaz)obj;
		if (obj==null) pov=false;
		else if(!(prj.iz.equals(this.iz)&&prj.u.equals(this.u)))pov=false;
		return pov;
	}
	
	@Override
	public String toString() {
		String pomocni=null;
		for(String s:pobudaIzlaz){
			if(pomocni==null)pomocni=s;
			else pomocni=new StringBuffer().append(pomocni).append(", ").append(s).toString();
		}
		return pomocni;
	}
	
	public void editPrijelaz(AUTPodatci podatci,Component obj){
		String pom=null;
		if(podatci.ime.equals("Mealy")) pom=editMealy(obj);
		else pom=editMoore(obj);
		if(pom!=null)pobudaIzlaz.add(pom);
	}
	
	private String editMealy(Component obj) {
		JTextField pobuda=new JTextField("");
		JTextField izlaz=new JTextField("");
		JLabel pobudaLab=new JLabel("Unesite pobudu za prijelaz: ");
		JLabel izlazLabel=new JLabel("Unesite izlaz koji generira pobuda: ");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(2,2));
		panel.add(pobudaLab);
		panel.add(pobuda);
		panel.add(izlazLabel);
		panel.add(izlaz);
		
		JOptionPane optionPane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog=optionPane.createDialog(obj,"Editor Prijelaza");
		dialog.setVisible(true);
		Object selected=optionPane.getValue();
		
		if(selected.equals(JOptionPane.CANCEL_OPTION)) return null;
		else return new StringBuffer().append(pobuda.getText()).append("/").append(izlaz.getText()).toString();
	}
	private String editMoore(Component obj) {
		JTextField pobuda=new JTextField("");
		JLabel pobudaLab=new JLabel("Unesite pobudu za prijelaz: ");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(pobudaLab);
		panel.add(pobuda);

		
		JOptionPane optionPane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog=optionPane.createDialog(obj,"Editor Prijelaza");
		dialog.setVisible(true);
		Object selected=optionPane.getValue();
		
		if(selected.equals(JOptionPane.CANCEL_OPTION)) return null;
		else return new StringBuffer().append(pobuda.getText()).toString();
	}
	public void dodajPodatak(String pobuda, String izlaz){
		String pomocni=pobuda;
		if(izlaz!=null)pomocni=new StringBuffer().append("/").append(izlaz).toString();
		pobudaIzlaz.add(pomocni);
	}
	
	public void dodajPodatak(Prijelaz pr){
		this.pobudaIzlaz.addAll(pr.pobudaIzlaz);
	}

}
