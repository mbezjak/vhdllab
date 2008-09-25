package hr.fer.zemris.vhdllab.service.extractors;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.io.StringReader;
import java.util.Properties;

public class SchemaCircuitInterfaceExtractor implements
        CircuitInterfaceExtractor {

    /*
     * (non-Javadoc)
     * 
     * @see
     * hr.fer.zemris.vhdllab.service.Functionality#configure(java.util.Properties
     * )
     */
    @Override
    public void configure(Properties properties) {
    }

    /*
     * (non-Javadoc)
     * 
     * @seehr.fer.zemris.vhdllab.service.CircuitInterfaceExtractor#
     * extractCircuitInterface(hr.fer.zemris.vhdllab.entities.File)
     */
    @Override
    public CircuitInterface execute(File file) throws ServiceException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(file
                .getData()));

        return info.getEntity().getCircuitInterface(info);
    }

}
