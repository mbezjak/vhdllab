package hr.fer.zemris.vhdllab.applets.schema.components.properties;

import hr.fer.zemris.vhdllab.applets.schema.components.Ptr;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

public class TextField extends AbstractPropField {
	Ptr<Object> p;
	JTextField field;
	
	public TextField(Ptr<Object> pStr) {
		p = pStr;
		field = new JTextField();
		field.setText((String)p.val);
		// kasnije cemo promijeniti, tako da reagira iskljucivo na neke pobude 
		// (npr. samo na ENTER)
		field.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateProperty();
			}
		});
	}
	
	
	public void updateProperty() {
		p.val = field.getText();
	}
	
	public Component getComponent() {
		return field;
	}
}
