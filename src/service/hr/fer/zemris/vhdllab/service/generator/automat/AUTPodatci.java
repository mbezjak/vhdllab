package hr.fer.zemris.vhdllab.service.generator.automat;


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

	/*private void parseInterfac(String[][] inter) {
		EntityParser ep=new EntityParser(ime,inter);
		interfac=ep.getParsedEntity();
		sirinaUlaza=ep.getInputWidth();
		sirinaIzlaza=ep.getOutputWidth();
	}

	/*public AUTPodatci(JComponent drawer) {
		super();
		JLabel label2=new JLabel("Machine type:");
		JTextField ime=new JTextField();
		String[] st={"Name","Direction","Type","From","To"};
		EntityTable interfac=new EntityTable("Interface:",st,"Entity Name: ");

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
		while(test){
			JOptionPane optionPane=new JOptionPane(panel,JOptionPane.PLAIN_MESSAGE,JOptionPane.OK_CANCEL_OPTION);
			JDialog dialog=optionPane.createDialog(drawer,"Transition Editor");
			dialog.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
			dialog.setSize(new Dimension(700,300));
			dialog.setVisible(true);
			Object selected=optionPane.getValue();
			if(selected.equals(JOptionPane.OK_OPTION)){
				this.ime=ime.getText();
				this.tip=(String)tip.getSelectedItem();
				String[][] inter=interfac.getData();
				parseInterfac(inter);
				test=false;
			}else if(selected.equals(JOptionPane.CANCEL_OPTION)) {
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
