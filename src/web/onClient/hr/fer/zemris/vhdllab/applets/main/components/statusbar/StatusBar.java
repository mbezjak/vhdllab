package hr.fer.zemris.vhdllab.applets.main.components.statusbar;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IStatusBar;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;

public class StatusBar extends JPanel implements IStatusBar {

	/**
	 * Serial Version UID
	 */
	private static final long serialVersionUID = 9102232350033953560L;

	private JLabel statusText;
	private JLabel timeLabel;

	private final String TIME = " HH:mm ";
    
	public StatusBar() {
		statusText = new JLabel();
		timeLabel = new JLabel(TIME);
		
		Box box = Box.createHorizontalBox();
		box.add(new JSeparator(SwingConstants.VERTICAL));
		box.add(timeLabel);
		
		this.setLayout(new BorderLayout());
		this.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(), BorderFactory.createLoweredBevelBorder()));
		this.add(statusText, BorderLayout.CENTER);
		this.add(box, BorderLayout.EAST);
		this.setPreferredSize(new Dimension(0, 24));
		
		final DateFormat df = new SimpleDateFormat(TIME);
		timeLabel.setText(df.format(new Date()));
        Timer timer = new Timer(1000*5,new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
                timeLabel.setText(df.format(new Date()));
            }
        });
        timer.start();
	}
	
	public void setText(String text) {
		statusText.setText(text);
	}
	
}