package hr.fer.zemris.vhdllab.applets.automat.entityTable;



public class EntityParser {
	private String parsedEntity=null;
	private String parsedEntityVHDL=null;
	private int inputWidth=0;
	private int outputWidth=0;
	
	public EntityParser(String data) {
		parsedEntity=data;
		unparseEntity();
	}
	/**
	 * stvara entity dio sklopa, novi red oznacen sa '#'
	 * @param ime
	 * @param pom
	 */
	public EntityParser(String ime,String[][] pom){
		super();
		
		StringBuffer buffer=new StringBuffer();
		for (int i=0;i<pom.length;i++)
			if(!pom[i][0].trim().equals("")){
				buffer.append(pom[i][0]).append(" ").append(pom[i][1].toUpperCase()).append(" ").append(pom[i][2]);
				if(pom[i][1].toUpperCase().equals("IN"))inputWidth++;
				else if(pom[i][1].toUpperCase().equals("OUT"))outputWidth++;
				if(pom[i][2].toUpperCase().equals("STD_LOGIC_VECTOR")){
					if(!pom[i][3].matches("[0-9]*"))pom[i][3]="0";
					if(!pom[i][4].matches("[0-9]*"))pom[i][4]="0";
					buffer.append(" ").append(pom[i][3]).append(" ").append(pom[i][4]);
					if(pom[i][1].toUpperCase().equals("IN"))inputWidth+=Math.abs(Integer.parseInt(pom[i][3])-Integer.parseInt(pom[i][4]));
					else if(pom[i][1].toUpperCase().equals("OUT"))outputWidth+=Math.abs(Integer.parseInt(pom[i][3])-Integer.parseInt(pom[i][4]));
				}
				buffer.append("\n");
			}
		parsedEntity=buffer.toString();
		
		buffer=new StringBuffer().append("ENTITY ").append(ime).append(" IS  PORT(");
		for (int i=0;i<pom.length;i++)
			if(!pom[i][0].trim().equals("")){
				buffer.append("\n");
				buffer.append(pom[i][0]).append(":").append(pom[i][1].toUpperCase()).append(" ")
				.append(pom[i][2]);
				if(pom[i][1].equals("in"))inputWidth++;
				else if(pom[i][1].equals("out"))outputWidth++;
				if(pom[i][2].toUpperCase().equals("STD_LOGIC_VECTOR")){
					buffer.append("(");
					if(Integer.parseInt(pom[i][3])<Integer.parseInt(pom[i][4]))
						buffer.append(Integer.parseInt(pom[i][3])).append(" TO ").append(Integer.parseInt(pom[i][4])).append(")");
					else buffer.append(Integer.parseInt(pom[i][3])).append(" DOWNTO ").append(Integer.parseInt(pom[i][4])).append(")");
					if(pom[i][1].equals("in"))inputWidth+=Math.abs(Integer.parseInt(pom[i][3])-Integer.parseInt(pom[i][4]));
					else if(pom[i][1].equals("out"))outputWidth+=Math.abs(Integer.parseInt(pom[i][3])-Integer.parseInt(pom[i][4]));
				}
				buffer.append(";");
			}
		buffer.deleteCharAt(buffer.length()-1);
		buffer.append(");\nEND ").append(ime).append(";");
		parsedEntityVHDL=buffer.toString();
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
	public String getParsedEntityVHDL() {
		return parsedEntityVHDL;
	}
}
