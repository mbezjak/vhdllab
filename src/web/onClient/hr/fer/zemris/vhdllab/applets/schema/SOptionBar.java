package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.automat.entityTable.EntityTable;
import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class SOptionBar extends JToolBar {
	private JToggleButton noneButt;
	private JButton setEntityButton;
	private JToggleButton drawWireButton;
	private JComboBox chooseComponentGroup;
	private SchemaMainPanel parentFrame;
	private JDialog dialogEntitySetup;

	public SOptionBar(SchemaMainPanel mfr) {
		super("Option Bar");
		
		parentFrame = mfr;
		
		init();
	}
	
	private void init() {
		ButtonGroup group = new ButtonGroup();
		
		noneButt = new JToggleButton();
		group.add(noneButt);
		
		setEntityButton = new JButton("Podesi sucelje...");
		setEntityButton.addActionListener(new StartModifyingEntityListener());
		this.add(setEntityButton);
		
		setEntityButton = new JButton("Korisnicki sklopovi...");
		this.add(setEntityButton);
		
		setEntityButton = new JButton("Izgeneriraj VHDL");
		this.add(setEntityButton);
		
		setEntityButton = new JButton("Serijaliziraj u konzolu");
		setEntityButton.addActionListener(new SerializeListener());
		this.add(setEntityButton);
		
		setEntityButton = new JButton("Deserijaliziraj...");
		setEntityButton.addActionListener(new DeserializeListener());
		this.add(setEntityButton);
		
		drawWireButton = new JToggleButton("Crtaj zice");
		drawWireButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				parentFrame.handleDrawWiresSelected();
			}
		});
		group.add(drawWireButton);
		this.add(drawWireButton);
		
		JLabel lab = new JLabel("  Kategorija: ");
		this.add(lab);
		
		chooseComponentGroup = new JComboBox();
		chooseComponentGroup.setMaximumSize(new Dimension(140, 20));
		fillWithCategories();
		this.add(chooseComponentGroup);
	}
	
	private abstract class CBarCategory {
		public String toString() { return getCategoryName(); }
		public abstract String getCategoryName();
		public abstract void takeAction();
	}
	
	private void fillWithCategories() {
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Svi sklopovi";
			}
			@Override
			public void takeAction() {
				parentFrame.recreateComponentBar(null);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Osnovni kombinacijski";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				cmplist.add("Sklop_AND");
				cmplist.add("Sklop_OR");
				cmplist.add("Sklop_XOR");
				cmplist.add("Sklop_NOT");
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Slozeni kombinacijski";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				cmplist.add("Sklop_MUX2nNA1");
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Sekvencijski sklopovi";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Korisnicki sklopovi";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		
		chooseComponentGroup.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent ie) {
				CBarCategory cbcat = (CBarCategory)ie.getItem();
				cbcat.takeAction();
			}
		});
	}
	
	private class StartModifyingEntityListener implements ActionListener {
		public StartModifyingEntityListener() {
		}
		public void actionPerformed(ActionEvent ae) {
			showEntitySetupper();
		}
	}
	
	private class SerializeListener implements ActionListener {
		public SerializeListener() {
		}
		public void actionPerformed(ActionEvent ae) {
			SchemaSerializableInformation info = parentFrame.getSchemaSerializableInfo();
			SSerialization serialization = new SSerialization(info);
			String s = serialization.getSerializedData();
			System.out.println(s);
		}
	}
	
	private class DeserializeListener implements ActionListener {
		public DeserializeListener() {
		}
		public void actionPerformed(ActionEvent ae) {
			SchemaSerializableInformation info = parentFrame.getSchemaSerializableInfo();
			SSerialization serialization = new SSerialization(info);
			String s = serialization.getSerializedData();
			
			try {
				SDeserialization deserialization = new SDeserialization(s, parentFrame);
			} catch (Exception e) {
				System.out.print(e.getMessage());
				e.printStackTrace();
			}
		}
	}
	
	public void showEntitySetupper() {
		Frame frame = JOptionPane.getFrameForComponent(parentFrame);
		dialogEntitySetup = new JDialog(frame, "Entity Setup", true);
		String[] st = {"Name","Direction","Type","From","To"};
		EntityTable table = new EntityTable("Entity declaration:",st,"Entity name: ");
		table.setProjectContainer(parentFrame.getProjectContainer());
		table.init();
		if (parentFrame.getCircuitInterface() != null) table.setData(parentFrame.getCircuitInterface());
		dialogEntitySetup.add(table);
		dialogEntitySetup.setBounds(0, 0, 480, 230);
		dialogEntitySetup.setLocation(250, 250);
		dialogEntitySetup.setVisible(true);
		parentFrame.setCircuitInterface(table.getCircuitInterface());
	}
	
	public void selectNoOption() {
		noneButt.setSelected(true);
	}
	
	public boolean isDrawWireSelected() {
		return drawWireButton.isSelected();
	}
	
	public void selectDrawWire() {
		drawWireButton.setSelected(true);
	}
}
