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
package hr.fer.zemris.vhdllab.platform.ui.wizard.schema;

import hr.fer.zemris.vhdllab.applets.editor.schema2.constants.Constants;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.Caseless;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.InOutSchemaComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaEntity;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaSerializer;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.entity.FileType;
import hr.fer.zemris.vhdllab.platform.ui.wizard.AbstractNewFileWizard;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.FileForm;
import hr.fer.zemris.vhdllab.platform.ui.wizard.support.PortWizardPage;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.ci.Port;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashSet;

import org.apache.commons.lang.UnhandledException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class NewSchemaWizard extends AbstractNewFileWizard {

    private static final int MARGIN_OFFSET = Constants.GRID_SIZE * 2;

    private FileForm fileForm;
    private PortWizardPage portWizardPage;

    @Override
    public void addPages() {
        fileForm = new FileForm();
        addForm(fileForm);

        portWizardPage = new PortWizardPage();
        addPage(portWizardPage);
    }

    @Override
    protected File getFile() {
        return getFile(fileForm);
    }

    @Override
    protected FileType getFileType() {
        return FileType.SCHEMA;
    }

    @Override
    protected String createData() {
        CircuitInterface ci = getCircuitInterface(fileForm, portWizardPage);
        try {
            return createSchema(ci);
        } catch (Exception e) {
            throw new UnhandledException(e);
        }
    }

    private String createSchema(CircuitInterface ci) throws Exception {
        ISchemaInfo info = new SchemaInfo();

        Caseless cname = new Caseless(ci.getName());
        info.getEntity().getParameters().setValue(SchemaEntity.KEY_NAME, cname);
        HashSet<Object> allowed = new HashSet<Object>();
        allowed.add(cname);
        info.getEntity().getParameters().getParameter(SchemaEntity.KEY_NAME)
                .getConstraint().setPossibleValues(allowed);

        int ly = MARGIN_OFFSET, ry = MARGIN_OFFSET;
        ISchemaComponentCollection components = info.getComponents();
        for (Port p : ci.getPorts()) {
            InOutSchemaComponent inout = new InOutSchemaComponent(p);
            if (p.isIN()) {
                components.addComponent(MARGIN_OFFSET, ly, inout);
                ly += inout.getHeight() + MARGIN_OFFSET;
            } else { // else isOUT
                components.addComponent(Constants.DEFAULT_SCHEMA_WIDTH, ry,
                        inout);
                ry += inout.getHeight() + MARGIN_OFFSET;
            }
        }

        SchemaSerializer ss = new SchemaSerializer();
        StringWriter writer = new StringWriter(1000 + 1000 * ci.getPorts()
                .size());
        try {
            ss.serializeSchema(writer, info);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
        return writer.toString();
    }

}
