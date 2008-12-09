package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;
import java.util.Set;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import org.apache.log4j.Logger;

public class DatabasePreferences extends AbstractPreferences {

    /**
     * Logger for this class
     */
    private static final Logger LOG = Logger
            .getLogger(DatabasePreferences.class);

    private static UserFileManager manager;

    private Properties properties;

    public DatabasePreferences(AbstractPreferences parent, String name) {
        super(parent, name);
    }

    public static void setManager(UserFileManager manager) {
        DatabasePreferences.manager = manager;
    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            UserFileInfo file = getFile();
            if (file != null) {
                try {
                    properties.load(new StringReader(file.getData()));
                } catch (IOException e) {
                    // Should never happen!
                    throw new IllegalStateException(e);
                }
            }
        }
        return properties;
    }

    private UserFileInfo getFile() {
        try {
            return manager.getFile(absolutePath());
        } catch (RuntimeException e) {
            LOG.error("Error while retrieving user file", e);
            return null;
        }
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        LOG.debug("childSpi(String):" + name);
        return new DatabasePreferences(this, name);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        LOG.debug("childrenNamesSpi()");
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        LOG.debug("flushSpi()");
        Properties props = getProperties();
        if (!props.isEmpty()) {
            StringWriter writer = new StringWriter();
            try {
                props.store(writer, null);
            } catch (IOException e) {
                // Should never happen!
                throw new IllegalStateException(e);
            }
            UserFileInfo file = getFile();
            String data = writer.toString();
            if (file == null) {
                Caseless userId = ApplicationContextHolder.getContext()
                        .getUserId();
                file = new UserFileInfo(userId, new Caseless(absolutePath()),
                        data);
            } else {
                file.setData(data);
            }
            manager.setFile(file);
        }
    }

    @Override
    public void flush() throws BackingStoreException {
        super.flush();
        if (this == userRoot()) {
            try {
                manager.saveFiles();
            } catch (RuntimeException e) {
                throw new BackingStoreException(e);
            }
        }
    }

    @Override
    protected String getSpi(String key) {
        LOG.debug("getSpi(String):" + key);
        return getProperties().getProperty(key);
    }

    @Override
    public String get(String key, String def) {
        String value = super.get(key, null);
        if (value == null) {
            value = def;
            put(key, value);
        }
        return value;
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        LOG.debug("keysSpi()");
        Set<Object> keySet = properties.keySet();
        String[] keys = new String[keySet.size()];
        return keySet.toArray(keys);
    }

    @Override
    protected void putSpi(String key, String value) {
        LOG.debug("putSpi(String, String):" + key + "--" + value);
        getProperties().put(key, value);
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        LOG.debug("removeNodeSpi()");
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

    @Override
    protected void removeSpi(String key) {
        LOG.debug("removeSpi(String):" + key);
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        LOG.debug("syncSpi()");
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

}
