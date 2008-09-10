package hr.fer.zemris.vhdllab.service.dependencies;

import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaComponentCollection;
import hr.fer.zemris.vhdllab.applets.editor.schema2.interfaces.ISchemaInfo;
import hr.fer.zemris.vhdllab.applets.editor.schema2.misc.PlacedComponent;
import hr.fer.zemris.vhdllab.applets.editor.schema2.model.serialization.SchemaDeserializer;
import hr.fer.zemris.vhdllab.entities.File;
import hr.fer.zemris.vhdllab.service.DependencyExtractor;
import hr.fer.zemris.vhdllab.service.ServiceException;

import java.io.StringReader;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

public class SchemaDependency implements DependencyExtractor {

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
     * @see
     * hr.fer.zemris.vhdllab.service.DependencyExtractor#execute(hr.fer.zemris
     * .vhdllab.entities.File)
     */
    @Override
    public Set<String> execute(File file) throws ServiceException {
        Set<String> retlist = new HashSet<String>();
        SchemaDeserializer sd = new SchemaDeserializer();
        ISchemaInfo info = sd.deserializeSchema(new StringReader(file
                .getContent()));
        ISchemaComponentCollection components = info.getComponents();

        for (PlacedComponent placed : components) {
            String filename = placed.comp.getCodeFileName();
            if (filename == null || filename.trim().equals(""))
                continue;
            retlist.add(filename);
        }

        return retlist;
    }

}
