package hr.fer.zemris.vhdllab.applets.editor.automat;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

public class CustomTextFieldTest extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8526818186600110365L;
	public CustomTextFieldTest(){
		this.getContentPane().setLayout(new BorderLayout());
		final CustomTextField cost=new CustomTextField("",3);
		this.getContentPane().add(cost,BorderLayout.CENTER);
		JButton button=new JButton("test");
		button.addActionListener(new ActionListener(){
			public void actionPerformed(java.awt.event.ActionEvent e) {
				System.out.println(cost.getText());
			};

		});
		this.getContentPane().add(button,BorderLayout.NORTH);
	}
	public static void main(String[] args) {
		CustomTextFieldTest tf=new CustomTextFieldTest();
		tf.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		tf.setVisible(true);
	}
}
