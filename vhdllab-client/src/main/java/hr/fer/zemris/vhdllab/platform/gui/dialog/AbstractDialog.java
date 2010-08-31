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
package hr.fer.zemris.vhdllab.platform.gui.dialog;

import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSource;

import javax.swing.JDialog;
import javax.swing.WindowConstants;

public class AbstractDialog<T> extends JDialog {

    private static final long serialVersionUID = 1L;

    private LocalizationSource source;

    private T result = null;

    public AbstractDialog(LocalizationSource source) {
        super(source.getFrame(), true);
        this.source = source;
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    }

    public T getResult() {
        return result;
    }

    protected void setResult(T result) {
        this.result = result;
    }

    public void startDialog() {
        pack();
        setLocationRelativeTo(source.getFrame());
        setVisible(true);
    }

    public void closeDialog(T resultOfExecution) {
        setResult(resultOfExecution);
        setVisible(false);
    }

}
