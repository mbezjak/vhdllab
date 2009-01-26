package hr.fer.zemris.vhdllab.platform.preference;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.UserFileInfo;
import hr.fer.zemris.vhdllab.platform.context.ApplicationContextHolder;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
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

    private List<String> children;
    private Properties properties;

    public DatabasePreferences(AbstractPreferences parent, String name) {
        super(parent, name);
        this.children = new ArrayList<String>();
    }

    static void setManager(UserFileManager manager) {
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
        return manager.getFile(absolutePath());
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        children.add(name);
        return new DatabasePreferences(this, name);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        String[] names = new String[children.size()];
        return children.toArray(names);
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
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
        return getProperties().getProperty(key);
    }

    @Override
    public String get(String key, String def) {
        String value = super.get(key, null);
        if (value == null) {
            value = def;
            if (value != null) {
                put(key, value);
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(absolutePath() + "#" + key + "=" + value);
        }
        return value;
    }

    @Override
    public boolean getBoolean(String key, boolean def) {
        String value = get(key, String.valueOf(def));
        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else {
            return def;
        }
    }

    @Override
    public double getDouble(String key, double def) {
        String value = get(key, String.valueOf(def));
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public float getFloat(String key, float def) {
        String value = get(key, String.valueOf(def));
        try {
            return Float.parseFloat(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public int getInt(String key, int def) {
        String value = get(key, String.valueOf(def));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    public long getLong(String key, long def) {
        String value = get(key, String.valueOf(def));
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException e) {
            return def;
        }
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        Set<Object> keySet = getProperties().keySet();
        String[] keys = new String[keySet.size()];
        return keySet.toArray(keys);
    }

    @Override
    protected void putSpi(String key, String value) {
        getProperties().put(key, value);
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

    @Override
    protected void removeSpi(String key) {
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        throw new UnsupportedOperationException(
                "This operation is not supported");
    }

}
