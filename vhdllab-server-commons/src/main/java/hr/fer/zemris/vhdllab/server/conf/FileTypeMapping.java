package hr.fer.zemris.vhdllab.server.conf;

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

    /**
     * A VHDL generator class name.
     */
    private String generator;

    /**
     * A circuit interface extractor class name.
     */
    private String extractor;

    /**
     * Default constructor.
     */
    public FileTypeMapping() {
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
     * Returns a <code>VHDL</code> generator class name.
     *
     * @return a <code>VHDL</code> generator class name
     */
    public String getGenerator() {
        return generator;
    }

    /**
     * Sets a <code>VHDL</code> generator class name.
     *
     * @param generator
     *            a <code>VHDL</code> generator class name
     */
    public void setGenerator(String generator) {
        this.generator = generator;
    }

    /**
     * Returns a circuit interface extractor class name.
     *
     * @return a circuit interface extractor class name
     */
    public String getExtractor() {
        return extractor;
    }

    /**
     * Sets a circuit interface extractor class name.
     *
     * @param extractor
     *            a circuit interface extractor class name
     */
    public void setExtractor(String extractor) {
        this.extractor = extractor;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
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
        result = prime * result
                + ((generator == null) ? 0 : generator.hashCode());
        result = prime * result
                + ((extractor == null) ? 0 : extractor.hashCode());
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
        if (generator == null) {
            if (other.generator != null)
                return false;
        } else if (!generator.equals(other.generator))
            return false;
        if (extractor == null) {
            if (other.extractor != null)
                return false;
        } else if (!extractor.equals(other.extractor))
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
        sb.append(", generator=").append(generator);
        sb.append(", extractor=").append(extractor);
        return sb.toString();
    }

}
