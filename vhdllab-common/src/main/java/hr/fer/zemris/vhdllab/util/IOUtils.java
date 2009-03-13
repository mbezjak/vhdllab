package hr.fer.zemris.vhdllab.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.UnhandledException;

public abstract class IOUtils {

    public static final String DEFAULT_ENCODING = "UTF-8";

    public static String toString(InputStream is) {
        try {
            return org.apache.commons.io.IOUtils.toString(is, DEFAULT_ENCODING);
        } catch (IOException e) {
            throw new UnhandledException(e);
        }
    }

}
