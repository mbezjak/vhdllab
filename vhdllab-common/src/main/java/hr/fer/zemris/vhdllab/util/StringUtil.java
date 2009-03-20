package hr.fer.zemris.vhdllab.util;

public abstract class StringUtil {

    public static String[] splitToNewLines(String string) {
        if (string == null) {
            return null;
        }
        return string.split("\r\n|\r|\n");
    }

}
