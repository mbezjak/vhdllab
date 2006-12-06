package hr.fer.zemris.vhdllab.vhdl;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.Properties;

public class CompilationMessage extends Message {
	
	protected static final String COMPILATION_MESSAGE_SUPER = "compilation.message.super";
	protected static final String COMPILATION_MESSAGE_ROW = "compilation.message.row";
	protected static final String COMPILATION_MESSAGE_COLUMN = "compilation.message.column";

	private int row;
	private int column;
	
	public CompilationMessage(String message, int row, int column) {
		super(message);
		if(row < 0 || column < 0) throw new IllegalArgumentException("Row or column can not be negative.");
		this.row = row;
		this.column = column;
	}

	public int getRow() {
		return row;
	}
	
	public int getColumn() {
		return column;
	}
	
	@Override
	public String serialize() {
		Properties prop = new Properties();
		String superSerialization = super.serialize();
		prop.setProperty(COMPILATION_MESSAGE_SUPER, superSerialization);
		prop.setProperty(COMPILATION_MESSAGE_ROW, String.valueOf(row));
		prop.setProperty(COMPILATION_MESSAGE_COLUMN, String.valueOf(column));
		return XMLUtil.serializeProperties(prop);
	}
	
	public static CompilationMessage deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties prop = XMLUtil.deserializeProperties(data);
		if(prop == null) throw new IllegalArgumentException("Unknown serialization format: data");
		int row = Integer.parseInt(prop.getProperty(COMPILATION_MESSAGE_ROW));
		int column = Integer.parseInt(prop.getProperty(COMPILATION_MESSAGE_COLUMN));
		String superSerialization = prop.getProperty(COMPILATION_MESSAGE_SUPER);
		Message msg = Message.deserialize(superSerialization);
		String messageText = msg.getMessageText();
		return new CompilationMessage(messageText, row, column);
	}

}