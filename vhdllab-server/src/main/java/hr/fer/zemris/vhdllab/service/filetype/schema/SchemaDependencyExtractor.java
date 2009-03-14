package hr.fer.zemris.vhdllab.service.filetype.schema;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileInfo;
import hr.fer.zemris.vhdllab.service.exception.DependencyExtractionException;
import hr.fer.zemris.vhdllab.service.filetype.DependencyExtractor;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;

public class SchemaDependencyExtractor implements DependencyExtractor {

    @Override
    public Set<Caseless> extract(FileInfo file)
            throws DependencyExtractionException {
        Set<Caseless> retlist = new HashSet<Caseless>();
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(file
                .getData()));
        ISchemaComponentCollection components = info.getComponents();

        for (PlacedComponent placed : components) {
            String filename = placed.comp.getCodeFileName();
            if (filename == null || filename.trim().equals(""))
                continue;
            retlist.add(new Caseless(filename));
        }

        return retlist;
    }

}
