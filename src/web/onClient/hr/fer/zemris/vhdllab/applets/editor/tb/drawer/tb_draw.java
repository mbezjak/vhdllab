package hr.fer.zemris.vhdllab.applets.editor.tb.drawer;

import hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface;
import hr.fer.zemris.vhdllab.vhdl.model.Port;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;



  public class tb_draw extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7103099954077953563L;

	
	
	
		boolean a =true;
		JPanel panel = new JPanel();
		Platno2 in_panel= new Platno2(null);
		JScrollPane scrol = new JScrollPane(in_panel);
		private String data="";
		String elementGrafa="pozadina";
		CircuitInterface io;
		//JSplitPane split= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		JPanel split = new JPanel();
		
		/*static public void main(String[] argv){
			tb_draw ned=new tb_draw();
			JFrame fa= new JFrame();
			fa.add(ned);
			ned.setData("<signal name=\"a\" type=\"scalar\">(0,0)(100,1)(200,0)(250,1)(430,0)</signal><signal name=\"b\">(0,0)(15,1)(200,0)(250,1)(430,0)</signal><signal name=\"c\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"1\">(0,00)(120,01)(150,10)(180,11)</signal>");   
			fa.pack();
			fa.setVisible(true);
			fa.setPreferredSize(new Dimension(800,800));
		}*/
		
		
		
		
		public tb_draw(){
			super();
			
			stvoriSignal();
			//setData("<signal name=\"a\" type=\"scalar\">(0,0)(100,1)(200,0)(250,1)(430,0)</signal><signal name=\"b\">(0,0)(15,1)(200,0)(250,1)(430,0)</signal><signal name=\"c\" type=\"vector\" rangeFrom=\"0\" rangeTo=\"1\">(0,00)(120,01)(150,10)(180,11)</signal>");
			
			stvoriGUI();
			
			
		}
		
	
		
		
		public void setCircuitInterface(CircuitInterface i){
			io=i;
			if(this.data.contentEquals(""))this.stvoriSignal();
			
		}
		
		
		public void resetirajBoje(){
			in_panel.boja=Color.BLACK;
			in_panel.boja_brojeva=Color.GRAY;
			in_panel.boja_signala=Color.GREEN;
			in_panel.boja_crtica=Color.ORANGE;
			in_panel.pomocne_crte=Color.DARK_GRAY;
			
			//in_panel.nacrtaj_sve();
		}
		
		
		
		/**vraca "prazan" signal    
	 	<signal name="a" type="scalar">(0,0)</signal>
		<signal name="b">(0,0)</signal>
		<signal name="c" type="vector" rangeFrom="0" rangeTo="1">(0,00)</signal>*/


		
		private void stvoriSignal() {
		  if(io!=null){
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
						this.setData(signal);
					}else if(port.getType().isScalar()){
						String signal="<signal name=\""+port.getName()+"\">(0,0)</signal>";
						this.setData(signal);
					}
				}
			}
		  }else{
			  System.out.println("<signal name=\"podatci_nisu_uneseni\" type=\"scalar\">(0,0)</signal>");
			  this.setData("<signal name=\"NO_INPUT\" type=\"scalar\">(0,0)</signal>");
		  }
		}



		public void setData(String data) {
			this.data=data;
			
			//postavljanje podataka u in_panel ce nacrtat postavljene podatke
			
			in_panel.setData(this.data);
		}



		public String getData() {
			this.data=this.in_panel.getData();
			return this.data;
			
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
			System.out.print("DD"+this.data);
			in_panel.setData(this.data);
			//repaint();//in_panel.nacrtaj_sve(); TODO
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
			JButton optButtonC= new JButton("opt");
			JButton optButtonN= new JButton("opt");
			JButton c_mod = new JButton("color reset");
			JPanel opt_panel = new JPanel();
			JPanel opt_panel1 = new JPanel(new GridLayout(10,1));
			final JColorChooser col_c = new JColorChooser();
			
			
			
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
			optButtonC.setForeground(Color.WHITE);
			optButtonC.setBackground(Color.BLACK);
			optButtonN.setForeground(Color.WHITE);
			optButtonN.setBackground(Color.BLACK);
					
			panel.setLayout(new GridLayout(15,1));
			panel.setBackground(Color.BLACK);
			panel.add(z_in);
			panel.add(z_out);
			panel.add(reset);
			panel.add(optButtonN);
			panel.setPreferredSize(D_panel);
			
			
			col_c.setBackground(Color.BLACK);
			col_c.remove(1);
			
		
			opt_panel.setLayout(new BorderLayout());
			opt_panel.setBackground(Color.BLACK);
			opt_panel.add(c_mod,BorderLayout.NORTH);
			opt_panel.add(optButtonC,BorderLayout.SOUTH);
			opt_panel.add(col_c,BorderLayout.CENTER);
			opt_panel.add(opt_panel1,BorderLayout.WEST);
			
			opt_panel.setPreferredSize(new Dimension(480,250));
			
			
			opt_panel1.add(pozadina);
			opt_panel1.add(signal);
			opt_panel1.add(pom_crte1);
			opt_panel1.add(pom_crte2);
			opt_panel1.add(brojevi);
			opt_panel1.setBackground(Color.BLACK);
			
			final JScrollPane colsc =new JScrollPane(opt_panel);//sadrzi komponente za odabir boja
			//colsc.add(opt_panel); nace da radi kako treba
			
			colsc.setPreferredSize(new Dimension(300,200));
			
			
			split.setLayout(new BorderLayout());
			split.add(panel,BorderLayout.WEST);
			split.add(scrol,BorderLayout.CENTER);
			//split.setDividerLocation(100);
			//split.setDividerSize(5);
			
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
					in_panel.repaint();				}
			});
				
			
			
			
			z_out.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					in_panel.zoom++;
					repaint();
				}
				
			});
			
			
			reset.addActionListener(new ActionListener(){

				public void actionPerformed(ActionEvent e) {
					
					reset();
					in_panel.setData(data);
					repaint();
				}
				
				
			});
			
			optButtonC.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					if(a){
						a=false;
						split.remove(panel);
						split.doLayout();
						split.add(colsc,BorderLayout.WEST);
						split.doLayout();
						split.revalidate();
			
					}else{
						a=true;
						split.remove(colsc);
						split.doLayout();
						panel.setVisible(true);
						split.add(panel,BorderLayout.WEST);
						split.doLayout();
						split.revalidate();
					}
					
				}
			
			});
			
			optButtonN.addActionListener(new ActionListener(){
				
				public void actionPerformed(ActionEvent e) {
					if(a){
						a=false;
						split.remove(panel);
						split.doLayout();
						split.add(colsc,BorderLayout.WEST);
						split.doLayout();
						split.revalidate();
			
					}else{
						a=true;
						split.remove(colsc);
						split.doLayout();
						panel.setVisible(true);
						split.add(panel,BorderLayout.WEST);
						split.doLayout();
						split.revalidate();
					}
					
				}
			});
				
			this.setLayout(new BorderLayout());
			this.setPreferredSize(new Dimension(600,400));
			this.add(split);
			
		
		}
		
		
	
	
	}

	
	
	
  
  
  
  
  
 
