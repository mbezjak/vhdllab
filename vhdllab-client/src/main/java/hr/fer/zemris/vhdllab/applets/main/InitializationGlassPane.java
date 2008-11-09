package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.client.core.log.MessageType;
import hr.fer.zemris.vhdllab.client.core.log.SystemError;
import hr.fer.zemris.vhdllab.client.core.log.SystemLog;
import hr.fer.zemris.vhdllab.client.core.log.SystemLogAdapter;
import hr.fer.zemris.vhdllab.client.core.log.SystemMessage;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * A custom glass pane used to display initialization process to a user.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 11/9/2007
 */
public final class InitializationGlassPane extends JPanel {

	private static final long serialVersionUID = 1L;
	/**
	 * A arc (both width and height) for round rectangle.
	 */
	private static final int RECT_ARC = 20;
	/**
	 * A font size for show message.
	 */
	private static final int FONT_SIZE = 15;

	private SystemMessage message;
	private SystemLogAdapter listener;
	/**
	 * A flag indicating if listener has been added in system log.
	 */
	private boolean listenerInstalled;

	/**
	 * Constructs a glass pane.
	 */
	public InitializationGlassPane() {
		setOpaque(false);
		MouseAdapter mouseAdapter = new MouseAdapter() {
			/*
			 * No implementation! Intent is to block all user inputs so system
			 * can initialize without user interruption.
			 */
		};
		addMouseListener(mouseAdapter);
		addMouseMotionListener(mouseAdapter);
		addMouseWheelListener(mouseAdapter);
		listener = new SystemLogAdapter() {
			@Override
			public void systemMessageAdded(final SystemMessage message) {
				if (SwingUtilities.isEventDispatchThread()) {
					updateMessage(message);
				} else {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							updateMessage(message);
						}
					});
				}
			}
			
			@Override
			public void errorMessageAdded(SystemError message) {
				systemMessageAdded(message);
			}
		};
	}

	/**
	 * Updates a message that this glass pane shows. This method will also
	 * repaint a component and therefor should be invoked by Event Dispatching
	 * Thread.
	 * 
	 * @param message
	 *            a message to set
	 */
	private void updateMessage(SystemMessage message) {
		this.message = message;
		repaint();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	protected void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		Graphics2D g = (Graphics2D) graphics;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);

		int width = getWidth();
		int height = getHeight();
		g.setColor(new Color(154, 182, 255, 230));
		int rectWidth = width / 3;
		int rectHeight = height / 3;
		int x = (width - rectWidth) / 2;
		int y = (height - rectHeight) / 2;
		g.fillRoundRect(x, y, rectWidth, rectHeight, RECT_ARC, RECT_ARC);

		if (message != null) {
			String content;
			if(message.getType().equals(MessageType.ERROR)) {
				content = "Error!";
			} else {
				content = message.getContent();
			}
			g.setColor(Color.BLACK);
			Font font = g.getFont();
			g.setFont(new Font(font.getFontName(), font.getStyle(), FONT_SIZE));
			FontMetrics fm = g.getFontMetrics();
			int stringWidth = fm.stringWidth(content);
			int stringX = x + (rectWidth - stringWidth) / 2;
			int stringY = y + rectHeight / 2;
			g.drawString(content, stringX, stringY);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	@Override
	public void setVisible(boolean flag) {
		super.setVisible(flag);
		/*
		 * When this glass pane is set visible register a system log listener
		 * and when it is hidden remove a listener.
		 */
		if (flag && !listenerInstalled) {
			SystemLog.instance().addSystemLogListener(listener);
			listenerInstalled = true;
		} else if (!flag && listenerInstalled) {
			SystemLog.instance().removeSystemLogListener(listener);
			listenerInstalled = false;
		}
	}

}
