package hr.fer.zemris.vhdllab.applets.main.interfaces;

public class FileContent {
	private String name;
	private String content;
	
	public FileContent(String name, String content) {
		this.name = name;
		this.content = content;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getName() {
		return name;
	}
	
	
}
