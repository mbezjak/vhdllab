package hr.fer.zemris.vhdllab.applets.schema;

import hr.fer.zemris.vhdllab.applets.schema.drawings.SchemaMainFrame;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

public class SOptionBar extends JToolBar {
	private JToggleButton noneButt;
	private JToggleButton drawWireButton;
	private SchemaMainFrame parentFrame;

	public SOptionBar(SchemaMainFrame mfr) {
		super("Option Bar");
		
		parentFrame = mfr;
		
		init();
	}
	
	private void init() {
		ButtonGroup group = new ButtonGroup();
		
		noneButt = new JToggleButton();
		group.add(noneButt);
		
		drawWireButton = new JToggleButton("Draw wires");
		drawWireButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				super.mouseClicked(arg0);
				parentFrame.handleDrawWiresSelected();
			}
		});
		group.add(drawWireButton);
		this.add(drawWireButton);
	}
	
	public void selectNoOption() {
		noneButt.setSelected(true);
	}
	
	public boolean isDrawWireSelected() {
		return drawWireButton.isSelected();
	}
}
