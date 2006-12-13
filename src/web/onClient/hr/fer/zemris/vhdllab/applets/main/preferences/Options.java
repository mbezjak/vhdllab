package hr.fer.zemris.vhdllab.applets.main.preferences;

import hr.fer.zemris.ajax.shared.XMLUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class Options {

	private static final String SERIALIZATION_KEY_SINGLE_OPTION = "single.option";
	
	private Map<String, SingleOption> options;
	
	public Options() {
		options = new HashMap<String, SingleOption>();
	}
	
	public void setOption(SingleOption o) {
		if(o == null) throw new NullPointerException("Single option must not be null.");
		options.put(o.getName(), o);
	}
	
	public SingleOption getOption(String name) {
		if(name == null) throw new NullPointerException("Name must not be null.");
		return options.get(name);
	}
	
	public String serialize() {
		Properties p = new Properties();
		int i = 1;
		for(String s : options.keySet()) {
			p.setProperty(SERIALIZATION_KEY_SINGLE_OPTION + "." + i, options.get(s).serialize());
			i++;
		}
		return XMLUtil.serializeProperties(p);
	}
	
	public static Options deserialize(String data) {
		if(data == null) throw new NullPointerException("Data can not be null.");
		Properties p = XMLUtil.deserializeProperties(data);
		if(p == null) throw new IllegalArgumentException("Unknown serialization format: data");
		Options opt = new Options();
		for(int i = 1; true; i++) {
			String s = p.getProperty(SERIALIZATION_KEY_SINGLE_OPTION + "." + i);
			if(s == null) break;
			SingleOption o = SingleOption.deserialize(s);
			opt.setOption(o);
		}
		return opt;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(!(obj instanceof Options)) return false;
		Options other = (Options) obj;
		
		return options.equals(other.options);
	}
	
	@Override
	public int hashCode() {
		return options.hashCode();
	}
	
}
