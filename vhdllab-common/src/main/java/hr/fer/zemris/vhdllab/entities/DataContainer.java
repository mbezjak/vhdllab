package hr.fer.zemris.vhdllab.entities;

/**
 * Any entity that contains data must implement this interface.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
interface DataContainer {

    /**
     * Maximum data length.
     */
    static final int DATA_LENGTH = 16000000; // ~ 16 MB

    /**
     * Returns the data from this data container.
     * 
     * @return the data from this data container
     */
    String getData();

    /**
     * Sets the data to this data container.
     * 
     * @param data
     *            the data for this data container
     * @throws NullPointerException
     *             if <code>data</code> is <code>null</code>
     * @throws IllegalArgumentException
     *             if <code>data</code> is too long
     */
    void setData(String data);

}
