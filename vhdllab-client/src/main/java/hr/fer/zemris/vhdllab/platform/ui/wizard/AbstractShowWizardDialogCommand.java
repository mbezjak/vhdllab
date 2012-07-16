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
package hr.fer.zemris.vhdllab.platform.ui.wizard;

import hr.fer.zemris.vhdllab.platform.ui.GuardedActionCommand;
import hr.fer.zemris.vhdllab.util.BeanUtil;

import org.apache.commons.lang.Validate;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.richclient.wizard.Wizard;
import org.springframework.richclient.wizard.WizardDialog;

public abstract class AbstractShowWizardDialogCommand extends
        GuardedActionCommand implements ApplicationContextAware {

    private ApplicationContext context;

    public AbstractShowWizardDialogCommand() {
        setDisplaysInputDialog(true);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException {
        Validate.notNull(applicationContext,
                "Application context can't be null");
        this.context = applicationContext;
    }

    @Override
    protected void doExecuteCommand() {
        logger.debug("Executing " + getClass());

        Wizard wizard = getWizard();
        new WizardDialog(wizard).showDialog();
    }

    protected abstract Class<? extends Wizard> getWizardClass();

    private Wizard getWizard() {
        String wizardBeanName = BeanUtil.beanName(getWizardClass());
        return (Wizard) context.getBean(wizardBeanName);
    }

}
