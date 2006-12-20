package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainPanel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPopupMenu;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class SOptionBar extends JToolBar {
	private JToggleButton noneButt;
	private JButton setEntityButton;
	private JToggleButton drawWireButton;
	private JComboBox chooseComponentGroup;
	private SchemaMainPanel parentFrame;
	private JPopupMenu popupEntitySetup;
	private JPopupMenu popupPortSetup;
	private SEntitySetupper entitySetupper;

	public SOptionBar(SchemaMainPanel mfr) {
		super("Option Bar");
		
		parentFrame = mfr;
		
		init();
	}
	
	private void init() {
		ButtonGroup group = new ButtonGroup();
		
		popupEntitySetup = new JPopupMenu("Entity Setup");
		popupPortSetup = new JPopupMenu("Port Setup");
		
		noneButt = new JToggleButton();
		group.add(noneButt);
		
		setEntityButton = new JButton("Podesi sucelje...");
		setEntityButton.addActionListener(new StartModifyingEntityListener());
		this.add(setEntityButton);
		
		setEntityButton = new JButton("Korisnicki sklopovi...");
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
				return "Osnovni sklopovi";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				cmplist.add("AND sklop");
				cmplist.add("OR sklop");
				cmplist.add("XOR sklop");
				cmplist.add("NOT sklop");
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Multipleksori";
			}
			@Override
			public void takeAction() {
				ArrayList<String> cmplist = new ArrayList<String>();
				cmplist.add("MUX2nNA1");
				parentFrame.recreateComponentBar(cmplist);
			} 
			});
		chooseComponentGroup.addItem(new CBarCategory() {
			@Override
			public String getCategoryName() {
				return "Dekoderi";
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
				return "Ostali sklopovi";
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
			popupEntitySetupper();
		}
	}
	
	public void popupEntitySetupper() {
		popupEntitySetup.removeAll();
		
		entitySetupper = new SEntitySetupper(parentFrame.getEntity(), this);
		popupEntitySetup.add(entitySetupper);

		popupEntitySetup.show(this, 25, 25);
	}
	
	public void popupPortSetupper(SchemaModelledComponentPort port) {
		popupPortSetup.removeAll();
		
		SPortSetupper portSetupper = new SPortSetupper(port, this);
		popupPortSetup.add(portSetupper);
		
		popupPortSetup.show(this, 25, 25);
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
