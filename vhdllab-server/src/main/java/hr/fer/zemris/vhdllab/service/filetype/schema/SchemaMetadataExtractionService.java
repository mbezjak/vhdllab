package hr.fer.zemris.vhdllab.service.filetype.schema;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entity.File;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.impl.AbstractMetadataExtractionService;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class SchemaMetadataExtractionService extends
        AbstractMetadataExtractionService {

    @Override
    public CircuitInterface extractCircuitInterface(File file)
            throws CircuitInterfaceExtractionException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd
                .deserializeSchema(new StringReader(file.getData()));
        return info.getEntity().getCircuitInterface(info);
    }

    @Override
    public Set<String> extractDependencies(File file)
            throws DependencyExtractionException {
        Set<String> retlist = new HashSet<String>();
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd
                .deserializeSchema(new StringReader(file.getData()));
        ISchemaComponentCollection components = info.getComponents();

        for (PlacedComponent placed : components) {
            String filename = placed.comp.getCodeFileName();
            if (filename == null || filename.trim().equals(""))
                continue;
            retlist.add(filename);
        }
        return retlist;
    }

    @Override
    public Result generateVhdl(File file) throws VhdlGenerationException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd
                .deserializeSchema(new StringReader(file.getData()));
        SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
        return vhdlgen.generateVHDL(info);
    }

}
