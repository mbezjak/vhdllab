package hr.fer.zemris.vhdllab.platform.ui;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.SwingUtilities;

public class MouseClickAdapter extends MouseAdapter {

    @Override
    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isLeftMouseButton(e) && e.getClickCount() == 2) {
            onDoubleClick(e);
        } else if (SwingUtilities.isRightMouseButton(e)) {
            onRightMouseClick(e);
        }
    }

    /**
     * Invoked when double click occurs for left mouse button.
     * 
     * @param e
     *            mouse event
     */
    protected void onDoubleClick(MouseEvent e) {
    }

    /**
     * Invoked when right mouse is clicked.
     * 
     * @param e
     *            mouse event
     */
    protected void onRightMouseClick(MouseEvent e) {
    }

}
