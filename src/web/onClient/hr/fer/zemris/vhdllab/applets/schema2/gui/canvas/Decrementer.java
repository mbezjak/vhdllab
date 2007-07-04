package hr.fer.zemris.vhdllab.applets.schema2.gui.canvas;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Decrementer implements ActionListener {
	
	private int maxIznos;
	private int iznos;
	private SchemaCanvas can;
	
	public Decrementer(int maxIznos, SchemaCanvas can) {
		this.maxIznos = maxIznos;
		iznos = maxIznos;
		this.can=can;
	}
	
	public int getIznos() {
		return iznos;
	}
	
	public void reset() {
		iznos = maxIznos;
	}
	


	public void actionPerformed(ActionEvent e) {
		iznos--;
		if(iznos == maxIznos/2) iznos = maxIznos;
		can.repaint();
	}
}
