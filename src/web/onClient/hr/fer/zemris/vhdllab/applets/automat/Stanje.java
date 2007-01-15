package hr.fer.zemris.vhdllab.applets.automat;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.GridLayout;
import java.util.ResourceBundle;

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
	public Stanje() {
		super();
		this.ime="";
		izlaz="";
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
	
	public void editStanje(AUTPodatci podatci,Component obj,ResourceBundle bundle){
		if(podatci.tip.equals("Moore")) editMoore(obj,podatci,bundle);
		else editMealy(obj,bundle);
	}
	private void editMealy(Component obj, ResourceBundle bundle) {
		JTextField ime=new JTextField(this.ime);
		JLabel imeLab=new JLabel(bundle.getString(LanguageConstants.DIALOG_INPUT_STATENAME));
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(imeLab);
		panel.add(ime);
		String[] options={bundle.getString(LanguageConstants.DIALOG_BUTTON_OK),
				bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL)
		};
		JOptionPane pane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,null);
		JDialog dialog=pane.createDialog(obj,bundle.getString(LanguageConstants.DIALOG_INPUT_STATELABEL));
		dialog.setVisible(true);
		
		Object selected=pane.getValue();		
		if(selected.equals(options[1])) this.ime=null;
		else this.ime=ime.getText().toUpperCase();
		
	}
	private void editMoore(Component obj, AUTPodatci podatci, ResourceBundle bundle) {
		JTextField ime=new JTextField(this.ime);
		JTextField izlaz=new CustomTextField(this.izlaz,podatci.sirinaIzlaza,false);
		JLabel imeLab=new JLabel(bundle.getString(LanguageConstants.DIALOG_INPUT_STATENAME));
		JLabel izlazLab=new JLabel(bundle.getString(LanguageConstants.DIALOG_INPUT_STATEOUT));
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(2,2));
		panel.add(imeLab);
		panel.add(ime);
		panel.add(izlazLab);
		panel.add(izlaz);
		String[] options={bundle.getString(LanguageConstants.DIALOG_BUTTON_OK),
				bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL)
		};
		JOptionPane pane=new JOptionPane(panel,JOptionPane.QUESTION_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,null);
		JDialog dialog=pane.createDialog(obj,bundle.getString(LanguageConstants.DIALOG_INPUT_STATELABEL));
		dialog.setVisible(true);
		
		Object selected=pane.getValue();		
		if(selected.equals(options[1])) this.ime=null;
		else {
			this.ime=ime.getText().toUpperCase();
			this.izlaz=izlaz.getText();
		}
	}
	
	public void editStanje2(AUTPodatci podatci, Component obj,ResourceBundle bundle){
		if(podatci.tip.equals("Moore")) editMoore2(obj,podatci,bundle);
		else {
			String[] options={bundle.getString(LanguageConstants.DIALOG_BUTTON_OK)
			};
			JOptionPane pane=new JOptionPane(bundle.getString(LanguageConstants.DIALOG_INPUT_MEALYEDITOUTPUT),
					JOptionPane.INFORMATION_MESSAGE,JOptionPane.DEFAULT_OPTION,null,options,null);
			Dialog dialog=pane.createDialog(obj,bundle.getString(LanguageConstants.DIALOG_INPUT_STATEEDITLABEL));
			dialog.setVisible(true);
		}
	}
	private void editMoore2(Component obj, AUTPodatci podatci, ResourceBundle bundle) {
		JTextField izlaz=new CustomTextField(this.izlaz,podatci.sirinaIzlaza,false);
		JLabel izlazLab=new JLabel(bundle.getString(LanguageConstants.DIALOG_INPUT_STATEOUT)+":");
		JPanel panel=new JPanel();
		panel.setLayout(new GridLayout(1,2));
		panel.add(izlazLab);
		panel.add(izlaz);
		
		String[] options={bundle.getString(LanguageConstants.DIALOG_BUTTON_OK),
				bundle.getString(LanguageConstants.DIALOG_BUTTON_CANCEL)
		};
		
		JOptionPane pane=new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,null);
		JDialog dialog=pane.createDialog(obj,bundle.getString(LanguageConstants.DIALOG_INPUT_STATEEDITLABEL));
		dialog.setVisible(true);
		
		Object selected=pane.getValue();		
		if(selected.equals(options[0])) {
			this.izlaz=izlaz.getText();
		}
	}

}
