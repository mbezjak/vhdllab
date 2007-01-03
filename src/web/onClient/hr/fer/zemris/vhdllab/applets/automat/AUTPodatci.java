package hr.fer.zemris.vhdllab.applets.automat;

import hr.fer.zemris.vhdllab.applets.automat.entityTable.EntityParser;
import hr.fer.zemris.vhdllab.applets.automat.entityTable.EntityTable;
import hr.fer.zemris.vhdllab.applets.automat.entityTable.ReturnData;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ResourceBundle;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class AUTPodatci {
	
	public String ime;
	public String tip;
	public String interfac;
	public String pocetnoStanje="";
	public int sirinaUlaza;
	public int sirinaIzlaza;
	
	public AUTPodatci(String ime,String tip,String interfac,String pocSt){
		super();
		this.ime=ime;
		this.tip=tip;
		this.interfac=interfac;
		this.pocetnoStanje=(pocSt==null?"":pocSt);
		parseInterfac(interfac);
	}

	private void parseInterfac(String interfac2) {
		EntityParser ep=new EntityParser(interfac2);
		interfac=interfac2;
		sirinaIzlaza=ep.getOutputWidth();
		sirinaUlaza=ep.getInputWidth();
	}

	private void parseInterfac(String[][] inter) {
		EntityParser ep=new EntityParser(ime,inter);
		interfac=ep.getParsedEntity();
		sirinaUlaza=ep.getInputWidth();
		sirinaIzlaza=ep.getOutputWidth();
	}

	//TODO dovrsiti lokalizaciju...
	public AUTPodatci(Component drawer,ProjectContainer pc, ResourceBundle bu) {
		super();
		JLabel label2=new JLabel("Machine type:");
		
		//TODO pozivanje entityTable-a kad bude gotov...
		EntityTable interfac=new EntityTable();
		interfac.setProjectContainer(null);
		interfac.init();

		String[] pom={"Moore","Mealy"};
		JComboBox tip=new JComboBox(pom);
		tip.setSelectedIndex(0);
		
		JPanel panel1=new JPanel();
		panel1.setLayout(new GridLayout(1,2));
		panel1.add(label2);
		panel1.add(tip);
		
		JPanel panel2=new JPanel();
		panel2.setLayout(new BorderLayout());
		interfac.setSize(new Dimension(500,300));
		panel2.add(interfac,BorderLayout.CENTER);
		
		JPanel panel=new JPanel();
		panel.setLayout(new BorderLayout());
		panel.add(panel1,BorderLayout.NORTH);
		panel.add(panel2,BorderLayout.CENTER);
		
		boolean test=true;
		String[] options={bu.getString(LanguageConstants.DIALOG_BUTTON_OK),bu.getString(LanguageConstants.DIALOG_BUTTON_CANCEL)};
		while(test){
			JOptionPane optionPane=new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION,null,options,options[1]);
			JDialog dialog=optionPane.createDialog(drawer,"Transition Editor");
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			dialog.setSize(new Dimension(700,300));
			dialog.setVisible(true);
			Object selected=optionPane.getValue();
			if(selected.equals(options[0])){
				this.tip=(String)tip.getSelectedItem();
				ReturnData inter=interfac.getData();
				this.ime=inter.getName();
				parseInterfac(inter.getData());
				test=false;
			}else if(selected.equals(options[1])) {
				this.ime=null;
				test=false;
			}
		}
	}
/*bit ce da netreba :) 
	/**
	 * provjerava formatiranost interfacea
	 * @param text
	 * @return
	 
	
	//TODO sredi mozda netreba
	private boolean vrijedi(String text) {
		boolean test=true;
		String[] str=text.split("\n");
		for(int i=0;i<str.length;i++){
			String[] strPom=str[i].split(":");
			if(strPom[0]!=""){
				if(!strPom[0].toLowerCase().matches("[a-z]+"))test=false;
			}else test=false;
			if(strPom.length>1){
				String[] strPom2=strPom[1].trim().split(" ");
				if(strPom2[0]!=""){
					if(!(strPom2[0].trim().toLowerCase().equals("in")||strPom2[0].trim().toLowerCase().equals("out"))) 
						test=false;
					}else test=false;
				if(strPom2.length>1){	
					if(!strPom2[1].matches("[0-9]+"))test=false;
				}else test=false;
				}else test=false;
			}
		return test;
	}*/
}
