package hr.fer.zemris.vhdllab.server.conf;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A single file type mapping.
 *
 * @author Miro Bezjak
 * @version 1.0
 * @since 6/2/2008
 */
public final class FileTypeMapping {

    /**
     * A file type of this mapping.
     */
    private String type;

    private final Map<FunctionalityType, String> functionalities;

    /**
     * Default constructor.
     */
    public FileTypeMapping() {
        functionalities = new HashMap<FunctionalityType, String>();
    }

    /**
     * Constructs a file type mapping for a specified <code>type</code>.
     *
     * @param type
     *            a file type if this mapping
     * @throws NullPointerException
     *             if type is <code>null</code>
     */
    public FileTypeMapping(String type) {
        this();
        setType(type);
    }

    /**
     * Returns a file type of this mapping.
     *
     * @return a file type of this mapping
     */
    public String getType() {
        return type;
    }

    /**
     * Sets a file type of this mapping.
     *
     * @param type
     *            a file type of this mapping
     */
    public void setType(String type) {
        if (type == null) {
            throw new NullPointerException("Type cant be null");
        }
        this.type = type;
    }

    /**
     * Adds a functionality type.
     *
     * @param funcType
     *            a functionality type
     * @param clazz
     *            a class implementing specified functionality type
     * @throws NullPointerException
     *             if either parameter is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>funcType</code> is an unknown functionality type
     */
    public void addFunctionality(String funcType, String clazz) {
        if (type == null) {
            throw new NullPointerException("Functionality type cant be null");
        }
        if (clazz == null) {
            throw new NullPointerException("Functionality class cant be null");
        }
        FunctionalityType t = FunctionalityType.valueOf(funcType
                .toUpperCase(Locale.ENGLISH));
        if (t == null) {
            throw new IllegalArgumentException(funcType
                    + " is an unknown functionality type");
        }
        functionalities.put(t, clazz);
    }

    /**
     * Returns name of a class implementing specified functionality type.
     *
     * @param funcType
     *            a functionality type
     * @return a name of a class implementing specified functionality type
     * @throws NullPointerException
     *             if <code>funcType</code> is <code>null</code>
     */
    public String getFunctionality(FunctionalityType funcType) {
        if (funcType == null) {
            throw new NullPointerException("Functionality type cant be null");
        }
        return functionalities.get(funcType);
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + type.hashCode();
        result = prime * result + functionalities.hashCode();
        return result;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof FileTypeMapping))
            return false;
        final FileTypeMapping other = (FileTypeMapping) obj;
        if (!type.equals(other.type))
            return false;
        if (!functionalities.equals(other.functionalities))
            return false;
        return true;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(30);
        sb.append("mapping ");
        sb.append("type=").append(type);
        sb.append(", functionalities=").append(functionalities);
        return sb.toString();
    }

}
