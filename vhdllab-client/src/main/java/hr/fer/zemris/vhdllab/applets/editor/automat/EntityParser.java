package hr.fer.zemris.vhdllab.applets.editor.automat;



public class EntityParser {
	private String parsedEntity=null;
	private int inputWidth=0;
	private int outputWidth=0;
	
	public EntityParser(String data) {
		parsedEntity=data;
		unparseEntity();
	}
	
	private void unparseEntity(){
		String[] redovi=parsedEntity.split("\n");
		for(int i=0;i<redovi.length;i++){
			String[] pom=redovi[i].split(" ");
			int br=1;
			if(pom[2].toUpperCase().equals("STD_LOGIC_VECTOR")){
				br+=Math.abs(Integer.parseInt(pom[3])-Integer.parseInt(pom[4]));	
			}
			if(pom[1].toUpperCase().equals("IN"))inputWidth+=br;
			else outputWidth+=br;
		}
	}
	
	public String getParsedEntity(){
		return parsedEntity;
	}
	public int getInputWidth(){
		return inputWidth;
	}
	public int getOutputWidth(){
		return outputWidth;
	}
}
