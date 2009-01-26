package hr.fer.zemris.vhdllab.client.core.bundle;


import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Provider of resource bundles that uses preferences to determine language of
 * bundles. All user needs to do is provide actual name of resource bundle.
 * <p>
 * Note that all methods in this class can be invoked concurrently by multiple
 * threads without the need for external synchronization.
 * </p>
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since 1/9/2007
 */
public final class ResourceBundleProvider {

    /**
     * A default language. Used when initialization can't be done
     * (UserPreferences is not yet initialized) or as a default value to
     * UserPreferences's get method.
     */
    private static final String DEFAULT_LANGUAGE = "en";

    /**
     * Language for all returned resource bundle
     */
    private static String language;

    static {
        /*
         * static initialization
         */
        init();
    }

    /**
     * Initializes the language that is used as bundle's locale. This should be
     * invoked only once during system initialization!
     */
    public static void init() {
        language = DEFAULT_LANGUAGE;
    }

    /**
     * Returns a resource bundle for the given base name. Returned value will
     * never be <code>null</code>.
     * 
     * @param baseName
     *            the base name of the resource bundle, a fully qualified class
     *            name (i.e. a name before language suffix)
     * @return a resource bundle for the given base name
     * @throws NullPointerException
     *             if <code>baseName</code> is <code>null</code>
     * @throws MissingResourceException
     *             if no bundle for such <code>name</code> can't be found
     */
    public static ResourceBundle getBundle(String baseName) {
        if (baseName == null) {
            throw new NullPointerException("Base bundle name cant be null.");
        }
        /*
         * CachedResourceBundle takes care or synchronization.
         */
        return CachedResourceBundles.getBundle(baseName, language);
    }

}
