package hr.fer.zemris.vhdllab.applets.automat;

import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Stanje {
	
	public String ime;
	public String izlaz;
	public int ox;
	public int oy;
	public Color boja=null;
	
	public Stanje(String ime,String izlaz,String ox,String oy){
		super();
		this.ime=ime;
		this.izlaz=izlaz;
		this.ox=Integer.parseInt(ox);
		this.oy=Integer.parseInt(oy);
		this.boja=Color.BLACK;
	}
	public Stanje(AUTPodatci podatci,Component obj) {
		//TODO provjera za format unosa
		super();
		editStanje(podatci,obj);
		this.boja=Color.RED;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean vrj;
		try{Stanje st=(Stanje) obj;
		vrj=this.ime.equals(st.ime);}
		catch(NullPointerException e){
			vrj=false;
		}
		return vrj;
	}
	
	@Override
	public String toString() {
		String s=null;
		if(this.izlaz==null)s=new StringBuffer().append(this.ime).toString();
		else s=new StringBuffer().append(this.ime).append("/").append(this.izlaz).toString();
		return s;
	}
	
	public void editStanje(AUTPodatci podatci,Component obj){
		if(podatci.tip.equals("Moore")) editMoore(obj);
		else editMealy(obj);
	}
	private void editMealy(Component obj) {
		JTextField ime=new JTextField("");
		JLabel imeLab=new JLabel("Unesite ime stanja: ");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(imeLab);
		panel.add(ime);
		
		JOptionPane pane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog=pane.createDialog(obj,"Editor Stanja");
		dialog.setVisible(true);
		
		Object selected=pane.getValue();		
		if(selected.equals(JOptionPane.CANCEL_OPTION)) this.ime=null;
		else this.ime=ime.getText();
		
	}
	private void editMoore(Component obj) {
		JTextField ime=new JTextField("");
		JTextField izlaz=new JTextField("");
		JLabel imeLab=new JLabel("Unesite ime stanja: ");
		JLabel izlazLab=new JLabel("Unesite izlaz za stanje: ");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(2,2));
		panel.add(imeLab);
		panel.add(ime);
		panel.add(izlazLab);
		panel.add(izlaz);
		
		JOptionPane pane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
		JDialog dialog=pane.createDialog(obj,"Editor Stanja");
		dialog.setVisible(true);
		
		Object selected=pane.getValue();		
		if(selected.equals(JOptionPane.CANCEL_OPTION)) this.ime=null;
		else {
			this.ime=ime.getText();
			this.izlaz=izlaz.getText();
		}
	}

}
