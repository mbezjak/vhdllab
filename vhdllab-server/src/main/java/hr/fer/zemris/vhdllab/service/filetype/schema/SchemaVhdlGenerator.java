package hr.fer.zemris.vhdllab.service.filetype.schema;

import hr.fer.zemris.vhdllab.api.results.VHDLGenerationResult;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.filetype.VhdlGenerator;

import java.io.StringReader;

public class SchemaVhdlGenerator implements VhdlGenerator {

    @Override
    public VHDLGenerationResult generate(FileInfo file)
            throws VhdlGenerationException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(file.getData()));
        
        SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
        return vhdlgen.generateVHDL(info);
    }

}
