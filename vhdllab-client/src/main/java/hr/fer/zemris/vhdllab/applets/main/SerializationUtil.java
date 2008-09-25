package hr.fer.zemris.vhdllab.applets.main;

import hr.fer.zemris.vhdllab.applets.main.interfaces.IEditor;
import hr.fer.zemris.vhdllab.applets.main.model.FileIdentifier;
import hr.fer.zemris.vhdllab.entities.Caseless;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SerializationUtil {

	private static final String PROJECT_FILE_SEPARATOR = "/";
	private static final String SEPARATOR_FOR_EACH_ROW = "\n";
	
	public static String serializeEditorInfo(List<IEditor> editors) {
		// guessing file name and project name (together) to be 15 characters
		StringBuilder sb = new StringBuilder(editors.size() * 20);
		for(IEditor e : editors) {
			if(e.isSavable()) {
				sb.append(e.getProjectName()).append(PROJECT_FILE_SEPARATOR)
					.append(e.getFileName()).append(SEPARATOR_FOR_EACH_ROW);
			}
		}
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	public static List<FileIdentifier> deserializeEditorInfo(String data) {
		if(data == null || data.equals(""))  {
			return Collections.emptyList();
		}
		String[] lines = data.split(SEPARATOR_FOR_EACH_ROW);
		List<FileIdentifier> files = new ArrayList<FileIdentifier>(lines.length);
		for(String s : lines) {
			String[] info = s.split(PROJECT_FILE_SEPARATOR);
			FileIdentifier f = new FileIdentifier(new Caseless(info[0]), new Caseless(info[1]));
			files.add(f);
		}
		return files;
	}
	
	public static String serializeViewInfo(List<String> views) {
		// guessing view type to be 10 characters
		StringBuilder sb = new StringBuilder(views.size() * 10);
		for(String s : views) {
			sb.append(s).append(SEPARATOR_FOR_EACH_ROW);
		}
		if(sb.length() != 0) {
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb.toString();
	}
	
	public static List<String> deserializeViewInfo(String data) {
		if(data == null || data.equals("")) {
			return Collections.emptyList();
		}
		String[] lines = data.split(SEPARATOR_FOR_EACH_ROW);
		List<String> files = new ArrayList<String>(lines.length);
		for(String s : lines) {
			files.add(s);
		}
		return files;
	}
	
}
