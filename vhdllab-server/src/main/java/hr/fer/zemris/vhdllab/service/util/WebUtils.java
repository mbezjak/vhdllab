package hr.fer.zemris.vhdllab.service.util;

import java.io.File;

import javax.servlet.ServletContext;

public abstract class WebUtils {

    public static File getLocation(ServletContext context, String realPath) {
        return new File(context.getRealPath(realPath));
    }

}
