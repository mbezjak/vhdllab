package hr.fer.zemris.vhdllab.applets.editor.tb.drawer;

import hr.fer.zemris.vhdllab.applets.main.UniformAppletException;
import hr.fer.zemris.vhdllab.applets.main.component.statusbar.MessageEnum;
import hr.fer.zemris.vhdllab.applets.main.dialog.RunDialog;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.interfaces.IWizard;
import hr.fer.zemris.vhdllab.applets.main.interfaces.ProjectContainer;
import hr.fer.zemris.vhdllab.applets.main.model.FileContent;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.utilities.StringUtil;
import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class Aplet extends JPanel implements IEditor, IWizard {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1428428870309310489L;
	
	JPanel panel = new JPanel();
	Platno2 in_panel= new Platno2();
	JScrollPane scrol = new JScrollPane(in_panel);
	private String data="";
	String elementGrafa="pozadina";
	CircuitInterface io;
	
	private ProjectContainer container;
	private String projectName;
	private String fileName;
	private String backupData;
	private boolean isSavable;
	private boolean isReadOnly;
	
	private String fileDepends = null;
	private String measureUnit = null;
	private String duration = null;
	
	
	
	
	
	public void setCircuitInterface(CircuitInterface i){
		io=i;
		this.stvoriSignal();
		
	}
	
	public void resetirajBoje(){
		in_panel.boja=Color.BLACK;
		in_panel.boja_brojeva=Color.GRAY;
		in_panel.boja_signala=Color.GREEN;
		in_panel.boja_crtica=Color.ORANGE;
		in_panel.pomocne_crte=Color.DARK_GRAY;
		
		//in_panel.nacrtaj_sve();
	}
	
	public void init(){
			//scrol.setIgnoreRepaint(true);
			
			//stvoriSignal();
			//setData("<signal name=\"a\" type=\"scalar\">(0,0)(100,1)(200,0)(250,1)(430,0)</signal><signal name=\"b\">(0,0)(15,1)(200,0)(250,1)(430,0)</signal><signal name=\"c\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"1\">(0,00)(120,01)(150,10)(180,11)</signal>");
			
			stvoriGUI();
			
		
	}
	
	/**vraca "prazan" signal    
 	<signal name="a" type="scalar">(0,0)</signal>
	<signal name="b">(0,0)</signal>
	<signal name="c" type="vector" rangeFrom="0" rangeTo="1">(0,00)</signal>*/


	
	private void stvoriSignal() {
		String data = "";
		List<Port> p=io.getPorts();
	
		for(Port port : p){
			if(port.getDirection().isIN()){
				if(port.getType().isVector()){
					int from=port.getType().getRangeFrom();
					int to =port.getType().getRangeTo();
					int bb=Math.abs(Math.abs(from)-Math.abs(to))+1;
					String nule="";
					for(int j=0;j<bb;j++){
						nule+="0";
					}
					String signal="<signal name=\""+port.getName()+"\" "+"type=\"vector\" "+"rangeFrom=\""+from+"\" "+"rangeTo=\""+to+"\">"+ "(0,"+nule+")</signal>";         
					data += signal + "\n";
				}else if(port.getType().isScalar()){
					String signal="<signal name=\""+port.getName()+"\">(0,0)</signal>";
					data += signal + "\n";
				}
			}
		}
		this.setData(data);
	}



	public String getData() {
		String data = createData();
		backupData = data;
		return data;
	}
	
	private String createData() {
		StringBuilder data = new StringBuilder();
		data.append("<file>").append(fileDepends).append("</file>\n");
		data.append("<measureUnit>").append(measureUnit != null ? measureUnit : "ns").append("</measureUnit>\n");
		data.append("<duration>").append(duration != null ? duration : "700").append("</duration>\n");
		Signal[] signals=this.in_panel.podatci_t;
		
		for(Signal s : signals) {
			data.append(s.toString().trim()).append("\n");
		}
		return data.toString();
	}

	private void postaviBoju(Color color) {
		
		if(this.elementGrafa.contentEquals("pozadina")){
			in_panel.boja=color;
		}else if(this.elementGrafa.contentEquals("brojevi")){
			in_panel.boja_brojeva=color;
		}else if(this.elementGrafa.contentEquals("signal")){
			in_panel.boja_signala=color;
		}else if(this.elementGrafa.contentEquals("pom_crte1")){
			in_panel.boja_crtica=color;
		}else if(this.elementGrafa.contentEquals("pom_crte2")){
			in_panel.pomocne_crte=color;
		}else if(this.elementGrafa.contentEquals("brojevi")){
			in_panel.boja_brojeva=color;
		}
		
		//in_panel.nacrtaj_sve();
		repaint();
			
	}
	
	
	
	/**postavlje sve signale na 0*/
	
	private void reset() {
		String[] temp=this.data.split("</signal>");
		for(int i=0;i<temp.length;i++){
			temp[i]=temp[i].replace(">",">#");
			String[] temp2=temp[i].split("#");
			if(temp2[0].contains("vector")){
				int broj_bitova=temp2[1].substring(1,temp2[1].length()-1).replace("(","").replace(")","!").split("!")[0].split(",")[1].length();
				String ns="";
				for(int j=0;j<broj_bitova;j++){
					ns+="0";
				}
				temp2[1]="(0,"+ns+")"+"</signal>";
			}else if(!temp2[0].contains("vector")){
				temp2[1]="(0,0)"+"</signal>";
			}
			temp[i]=temp2[0]+temp2[1];
		}
		this.data="";
		for(int i=0;i<temp.length;i++){
			data+=temp[i];
		}
		System.out.print("5465465"+this.data);
		in_panel.setData(this.data);
		repaint();
		//in_panel.nacrtaj_sve();
	}
	

	public void stvoriGUI(){
		
		JRadioButton pozadina=new JRadioButton("pozadina");
		pozadina.setBackground(Color.BLACK);
		pozadina.setForeground(Color.WHITE);
		JRadioButton signal= new JRadioButton("signal");
		signal.setBackground(Color.BLACK);
		signal.setForeground(Color.WHITE);
		JRadioButton pom_crte1=new JRadioButton("pom crte1");
		pom_crte1.setBackground(Color.BLACK);
		pom_crte1.setForeground(Color.WHITE);
		JRadioButton pom_crte2=new JRadioButton("pom crte2");
		pom_crte2.setBackground(Color.BLACK);
		pom_crte2.setForeground(Color.WHITE);
		JRadioButton brojevi=new JRadioButton("brojevi");
		brojevi.setBackground(Color.BLACK);
		brojevi.setForeground(Color.WHITE);
		
		
		
		ButtonGroup group=new ButtonGroup();
		group.add(pozadina);
		group.add(signal);
		group.add(pom_crte1);
		group.add(pom_crte2);
		group.add(brojevi);
		
		
		//JButton reset_boje = new JButton("reset");
		JButton reset=new JButton("reset");
		JButton z_in = new JButton("z. in");
		JButton z_out = new JButton("z. out");
		JTabbedPane tab=new JTabbedPane();
		JButton c_mod = new JButton("color reset");
		JPanel opt_panel = new JPanel();
		JPanel opt_panel1 = new JPanel(new GridLayout(5,1));
		final JColorChooser col_c = new JColorChooser();
		JSplitPane split= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		
		
		Dimension D_panel =new Dimension();
		D_panel.setSize(80,50);
		
		Dimension D_button = new Dimension();
		D_button.setSize(70,30);
		
		
		Dimension d1 = new Dimension();
		d1.setSize(300,300);
		
		
		col_c.getSelectionModel().addChangeListener(new ChangeListener(){

			public void stateChanged(ChangeEvent arg0) {
				postaviBoju(col_c.getColor());
				
			}

			
			
		});
		
		c_mod.setPreferredSize(D_button);
		z_in.setPreferredSize(D_button);
		z_out.setPreferredSize(D_button);
		reset.setPreferredSize(D_button);
		z_in.setForeground(Color.WHITE);
		z_in.setBackground(Color.BLACK);
		z_out.setForeground(Color.WHITE);
		z_out.setBackground(Color.BLACK);
		c_mod.setBackground(Color.BLACK);
		c_mod.setForeground(Color.WHITE);
		reset.setForeground(Color.RED);
		reset.setBackground(Color.BLUE);
		
				
		panel.setLayout(new GridLayout(8,1));
		panel.setBackground(Color.BLACK);
		panel.add(z_in);
		panel.add(z_out);
		panel.add(reset);
		panel.setPreferredSize(D_panel);
		
		col_c.setBackground(Color.BLACK);
		
		
		//opt_panel.setPreferredSize(D_panel);
		//opt_panel.setLayout(new GridLayout(8,1));
		opt_panel.setLayout(new BorderLayout());
		opt_panel.setBackground(Color.BLACK);
		opt_panel.add(c_mod,BorderLayout.NORTH);
		opt_panel.add(col_c,BorderLayout.CENTER);
		opt_panel.add(opt_panel1,BorderLayout.WEST);
		
		opt_panel.setPreferredSize(new Dimension(490,300));
		
		
		opt_panel1.add(pozadina);
		opt_panel1.add(signal);
		opt_panel1.add(pom_crte1);
		opt_panel1.add(pom_crte2);
		opt_panel1.add(brojevi);
		opt_panel1.setBackground(Color.BLACK);
		
		
		JScrollPane sc2 =new JScrollPane(opt_panel);
		sc2.revalidate();
		
		tab.addTab("w",panel);
		tab.add("opt",sc2);
		//tab.addTab("opt",opt_panel);
		tab.setForeground(Color.DARK_GRAY);
		tab.setBackground(Color.BLACK);
		tab.setPreferredSize(new Dimension(90,100));
		
		split.add(tab);
		split.add(scrol);
		
		pozadina.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				elementGrafa="pozadina";
				
			}
			
		});
		
		signal.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				elementGrafa="signal";
				
			}
			
		});
		
		
		
		pom_crte1.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				elementGrafa="pom_crte1";
				
			}
			
		});
		
		pom_crte2.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				elementGrafa="pom_crte2";
				
			}
			
		});

		brojevi.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent arg0) {
				elementGrafa="brojevi";
				
			}
			
		});

		
		c_mod.addActionListener(new ActionListener(){
			

			public void actionPerformed(ActionEvent arg0) {
				resetirajBoje();
				in_panel.repaint();
			}
			
		});
		
		
		z_in.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				if(in_panel.zoom>1){
					in_panel.zoom--;
				}
				in_panel.repaint();
			}
		});
			
		
		
		
		z_out.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
				
				in_panel.zoom++;
				in_panel.repaint();
			}
			
		});
		
		
		reset.addActionListener(new ActionListener(){

			public void actionPerformed(ActionEvent e) {
			
				reset();
				in_panel.setData(data);
			}

			
		});
		
		
		this.setLayout(new BorderLayout());
		this.add(split);
		//this.getContentPane().add(tab,BorderLayout.WEST);
		//this.getContentPane().add(scrol,BorderLayout.CENTER);
	
	
	}
	
	private void setData(String data) {
		this.data=data;
		
		//postavljanje podataka u in_panel ce nacrtat postavljene podatke
		in_panel.setData(this.data);
	}


	public String getFileName() {
		return fileName;
	}


	public String getProjectName() {
		return projectName;
	}


	public IWizard getWizard() {
		return this;
	}


	public void highlightLine(int line) {}


	public boolean isModified() {
		String data = createData();
		data = StringUtil.removeWhiteSpaces(data);
		backupData = StringUtil.removeWhiteSpaces(backupData);
		if(backupData.equals(data)) {
			return false;
		} else {
			return true;
		}
	}


	public boolean isReadOnly() {
		return isReadOnly;
	}


	public boolean isSavable() {
		return isSavable;
	}


	public void setFileContent(FileContent fContent) {
		projectName = fContent.getProjectName();
		fileName = fContent.getFileName();
		String data = fContent.getContent();
		int start, end;
		
		start = data.indexOf("<file>");
		start += "<file>".length();
		end = data.indexOf("</file>");
		fileDepends = data.substring(start, end);

		start = data.indexOf("<measureUnit>");
		if(start != -1) {
			start += "<measureUnit>".length();
			end = data.indexOf("</measureUnit>");
			measureUnit = data.substring(start, end);
		}

		start = data.indexOf("<duration>");
		if(start != -1) {
			start = data.indexOf("<duration>");
			start += "<duration>".length();
			end = data.indexOf("</duration>");
			duration = data.substring(start, end);
		}
		
		setData(data);
		if(this.in_panel.podatci_t == null) {
			try {
				CircuitInterface ci = container.getCircuitInterfaceFor(projectName, fileDepends);
				setCircuitInterface(ci);
			} catch (UniformAppletException e) {
				e.printStackTrace();
				in_panel.podatci_t = new Signal[0];
			}
		}
		init();
		backupData = data;
	}


	public void setProjectContainer(ProjectContainer pContainer) {
		this.container = pContainer;
	}


	public void setReadOnly(boolean flag) {
		isReadOnly = flag;
	}


	public void setSavable(boolean flag) {
		isSavable = flag;
	}


	public FileContent getInitialFileContent(Component parent, String projectName) {
		RunDialog dialog = new RunDialog(parent, true, container, RunDialog.COMPILATION_TYPE);
		dialog.setChangeProjectButtonText("change");
		dialog.setTitle("Select a file for which to create testbench");
		dialog.setListTitle("Select a file for which to create testbench");
		dialog.startDialog();
		if(dialog.getOption() != RunDialog.OK_OPTION) return null;
		FileIdentifier file = dialog.getSelectedFile();
		if(!projectName.equalsIgnoreCase(file.getProjectName())) {
			container.echoStatusText("Cant create testbench for file outside of '"+projectName+"'", MessageEnum.Information);
			return null;
		}
		String testbench = JOptionPane.showInputDialog(parent, "Enter a name of a testbench");
		if(testbench == null) return null;
		StringBuilder sb = new StringBuilder();
		sb.append("<file>").append(file.getFileName()).append("</file>");
		return new FileContent(projectName, testbench, sb.toString());
	}

	public void cleanUp() {}
	
}
