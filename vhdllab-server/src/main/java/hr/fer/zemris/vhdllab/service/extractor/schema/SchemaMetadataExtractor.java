package hr.fer.zemris.vhdllab.service.extractor.schema;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.SchemaInfo2VHDL;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.service.ci.CircuitInterface;
import hr.fer.zemris.vhdllab.service.exception.CircuitInterfaceExtractionException;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.exception.VhdlGenerationException;
import hr.fer.zemris.vhdllab.service.extractor.AbstractMetadataExtractor;
import hr.fer.zemris.vhdllab.service.result.Result;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class SchemaMetadataExtractor extends AbstractMetadataExtractor {

    @Override
    protected CircuitInterface doExtractCircuitInterface(String data)
            throws CircuitInterfaceExtractionException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(data));
        return info.getEntity().getCircuitInterface(info);
    }

    @Override
    protected Set<String> doExtractDependencies(String data)
            throws DependencyExtractionException {
        Set<String> retlist = new HashSet<String>();
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(data));
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
    protected Result doGenerateVhdl(String data) throws VhdlGenerationException {
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(data));
        SchemaInfo2VHDL vhdlgen = new SchemaInfo2VHDL();
        return vhdlgen.generateVHDL(info);
    }

}
