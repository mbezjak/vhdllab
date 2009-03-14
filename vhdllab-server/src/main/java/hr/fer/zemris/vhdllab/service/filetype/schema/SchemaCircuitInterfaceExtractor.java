package hr.fer.zemris.vhdllab.service.filetype.schema;

import hr.fer.zemris.vhdllab.api.vhdl.CircuitInterface;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterfaceExtractor;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;

import java.io.StringReader;

public class SchemaCircuitInterfaceExtractor implements
        CircuitInterfaceExtractor {

    @Override
    public CircuitInterface extract(FileInfo file)
            throws CircuitInterfaceExtractionException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(file
                .getData()));

        return info.getEntity().getCircuitInterface(info);
    }

}
