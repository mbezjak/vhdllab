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

import hr.fer.zemris.vhdllab.platform.i18n.LocalizationSupport;

import java.awt.Frame;

import javax.swing.JOptionPane;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.NoSuchMessageException;

abstract class AbstractOptionPaneDialogManager extends LocalizationSupport
        implements DialogManager {

    private static final String DIALOG_MANAGER_SUFFIX = "DialogManager";
    private static final String MESSAGE_CODE_PREFIX = "dialog.";

    private static final String MESSAGE_SUFFIX = ".message";
    private static final String TITLE_SUFFIX = ".title";

    @SuppressWarnings("unchecked")
    @Override
    public <T> T showDialog(Object... args) {
        String title = getTitle(args);
        String text = getText(args);
        int optionType = getOptionType();
        int messageType = getMessageType();
        Frame owner = getFrame();
        Object[] options = getOptionsForType(optionType);
        int option = JOptionPane.showOptionDialog(owner, text, title,
                optionType, messageType, null, options, options[0]);
        return (T) evaluateOption(option);
    }

    protected String getText(Object[] args) {
        String code = getMessageCodeFromSimpleClassName() + MESSAGE_SUFFIX;
        return getMessage(code, args);
    }

    protected String getTitle(Object[] args) {
        String code = getMessageCodeFromSimpleClassName() + TITLE_SUFFIX;
        return getMessage(code, args);
    }

    protected abstract Object evaluateOption(int option);

    protected abstract int getOptionType();

    protected abstract int getMessageType();

    private Object[] getOptionsForType(int optionType) {
        switch (optionType) {
        case JOptionPane.DEFAULT_OPTION:
            return getOption("ok");
        case JOptionPane.YES_NO_CANCEL_OPTION:
            return getOption("yes", "no", "cancel");
        case JOptionPane.YES_NO_OPTION:
            return getOption("yes", "no");
        case JOptionPane.OK_CANCEL_OPTION:
            return getOption("ok", "cancel");
        default:
            throw new IllegalStateException("Unknown option: " + optionType);
        }
    }

    private String[] getOption(String... options) {
        String[] localized = new String[options.length];
        for (int i = 0; i < options.length; i++) {
            try {
                localized[i] = getMessage(getMessageCodeFromSimpleClassName()
                        + options[i], null);
            } catch (NoSuchMessageException e) {
                localized[i] = getMessage("dialog." + options[i], null);
            }
        }
        return localized;
    }

    @Override
    protected String alterSimpleClassName(String simpleClassName) {
        return StringUtils.removeEnd(simpleClassName, DIALOG_MANAGER_SUFFIX);
    }

    @Override
    protected String alterMessageCode(String code) {
        return MESSAGE_CODE_PREFIX + code;
    }

}
