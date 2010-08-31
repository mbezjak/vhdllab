/*******************************************************************************
 * See the NOTICE file distributed with this work for additional information
 * regarding copyright ownership.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
