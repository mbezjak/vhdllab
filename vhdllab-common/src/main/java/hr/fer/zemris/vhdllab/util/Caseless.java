package hr.fer.zemris.vhdllab.util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Locale;

/**
 * Represents a thin wrapper around case-insensitive {@link String}. This class
 * is immutable and therefore thread-safe.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public final class Caseless implements Serializable, Comparable<Caseless> {

    private static final long serialVersionUID = 1L;

    public static final Caseless EMPTY = new Caseless();

    /**
     * An inner string
     */
    private String inner;

    /**
     * Default constructor for persistence provider.
     */
    Caseless() {
        this("");
    }

    /**
     * Constructs a caseless for specified string.
     * 
     * @param inner
     *            a string for whom to construct caseless
     * @throws NullPointerException
     *             if <code>inner</code> is <code>null</code>
     */
    public Caseless(String inner) {
        this.inner = inner;
        checkProperties();
    }

    /**
     * Ensures that properties are in correct state.
     * 
     * @throws NullPointerException
     *             if <code>inner</code> is <code>null</code>
     */
    private void checkProperties() {
        if (inner == null) {
            throw new NullPointerException("String can't be null");
        }
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
        result = prime * result + inner.toLowerCase(Locale.ENGLISH).hashCode();
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
        if (!(obj instanceof Caseless))
            return false;
        Caseless other = (Caseless) obj;
        return inner.equalsIgnoreCase(other.inner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    @Override
    public int compareTo(Caseless o) {
        return inner.compareToIgnoreCase(o.inner);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return inner;
    }

    /**
     * Ensures that properties are in correct state after deserialization.
     */
    private void readObject(ObjectInputStream in) throws IOException,
            ClassNotFoundException {
        in.defaultReadObject();
        checkProperties();
    }

    /**
     * Returns length of an inner string.
     * 
     * @return length of an inner string
     */
    public int length() {
        return inner.length();
    }

    /**
     * Returns caseless whose inner string is all upper case.
     * 
     * @return upper case caseless
     */
    public Caseless toUpperCase() {
        return new Caseless(inner.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Returns caseless whose inner string is all lower case.
     * 
     * @return lower case caseless
     */
    public Caseless toLowerCase() {
        return new Caseless(inner.toLowerCase(Locale.ENGLISH));
    }

}
