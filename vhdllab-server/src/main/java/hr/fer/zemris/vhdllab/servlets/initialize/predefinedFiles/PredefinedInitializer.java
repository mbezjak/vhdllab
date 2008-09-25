package hr.fer.zemris.vhdllab.servlets.initialize.predefinedFiles;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.utilities.FileUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PredefinedInitializer {
	
	private static final String PREDEFINED_FILES = "predefinedFiles.properties";
	private static final String PREDEFINED_FILES_LIST = "files";
	private static final String PREDEFINED_FILES_SEPARATOR = ";";

	private static final PredefinedInitializer INSTANCE = new PredefinedInitializer();
	private List<FileAndContent> sources;

	/**
	 * Private constructor
	 */
	private PredefinedInitializer() {
		super();
		sources = new ArrayList<FileAndContent>();
		initSources();
	}

    /**
	 * Returns an instance of preferences parser.
	 * 
	 * @return an instance of preferences parser
	 */
	public static PredefinedInitializer instance() {
		return INSTANCE;
	}
	
	public List<FileAndContent> getSources() {
        return sources;
    }

	private void initSources() {
        Class<?> clazz = this.getClass();
        InputStream is = clazz
                .getResourceAsStream(PREDEFINED_FILES);
        Properties p = FileUtil.getProperties(is);
        String fileList = p
                .getProperty(PREDEFINED_FILES_LIST);
        String[] files = fileList
                .split(PREDEFINED_FILES_SEPARATOR);
        for (String s : files) {
            s = s.trim();
            if (s.equals(""))
                continue;
            String data = FileUtil.readFile(clazz.getResourceAsStream(s));
            FileAndContent fc = new FileAndContent();
            fc.setFileName(new Caseless(s));
            fc.setContent(data);
            sources.add(fc);
        }
	}
	
	public static class FileAndContent {
	    private Caseless fileName;
	    private String content;
        public Caseless getFileName() {
            return fileName;
        }
        public void setFileName(Caseless fileName) {
            this.fileName = fileName;
        }
        public String getContent() {
            return content;
        }
        public void setContent(String content) {
            this.content = content;
        }
	}

}
