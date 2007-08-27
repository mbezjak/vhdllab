package hr.fer.zemris.vhdllab.vhdl.model;

import hr.fer.zemris.vhdllab.model.File;
import hr.fer.zemris.vhdllab.model.Project;
import hr.fer.zemris.vhdllab.service.ServiceException;
import hr.fer.zemris.vhdllab.service.VHDLLabManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <p>This class contains various static method for VHDL source examination.</p>
 * @author marcupic
 */
public class Extractor {

	/* Private arrays of characters. */
	private final static char[] ch_IS = "IS".toCharArray();
	private final static char[] ch_GENERIC = "GENERIC".toCharArray();
	private final static char[] ch_PORT = "PORT".toCharArray();
	//private final static char[] ch_ENTITY = "ENTITY".toCharArray();
	private final static char[] ch_IN = "IN".toCharArray();
	private final static char[] ch_OUT = "OUT".toCharArray();
	private final static char[] ch_INOUT = "INOUT".toCharArray();
	private final static char[] ch_BUFFER = "BUFFER".toCharArray();
	private final static char[] ch_DVOTOCKA = ":".toCharArray();
	//private final static char[] ch_DVOTJEDN = ":=".toCharArray();
	private final static char[] ch_RAZMAK = " ".toCharArray();
	private final static char[] ch_ZAREZ = ",".toCharArray();
	private final static char[] ch_ZOBLA = ")".toCharArray();

	/* Private array groups. */
	private final static char[][] ch_DVOTOCKA_RAZMAK_ZAREZ_ZOBLA = new char[][] {ch_DVOTOCKA,ch_RAZMAK,ch_ZAREZ,ch_ZOBLA};
	private final static char[][] ch_IN_OUT_INOUT_BUFFER = new char[][] {ch_IN,ch_OUT,ch_INOUT,ch_BUFFER};
	
	/**
	 * Use this method to remove all comments from VHDL source.
	 * Comments begins with two successive minus signs (--) and
	 * go to the end of the line.
	 * 
	 * @param source VHDL source code that can include comments
	 * @return VHDL source code without comments
	 */
	public static String decomment(String source) {
		if(source==null) throw new NullPointerException("No source given!");
		char[] chs = source.toCharArray();
		int pos = 0;
		int i;
		for(i = 0; i < chs.length-1; i++) {
			chs[pos] = chs[i];
			if(chs[i]=='-' && chs[i+1]=='-') {
				i+=2;
				while(i<chs.length && chs[i]!='\n') i++;
				continue;
			}
			pos++;
		}
		if(i<chs.length) {
			chs[pos]=chs[i];
			pos++;
		}
		if(pos==0) return new String();
		return new String(chs,0,pos);
	}
	
	/**
	 * Use this method to replace any succession of whitespaces with a single
	 * whitespace sign. Whitespaces include tabs, spaces, CR and LF.
	 * 
	 * @param source VHDL source code with arbitrary successions of whitespace
	 *        characters.
	 * @return VHDL source where only whitespace character is the space character,
	 *         and there are no whitespace successions.
	 */
	public static String removeWhiteSpaces(String source) {
		if(source==null) throw new NullPointerException("No source given!");
		char[] chs = source.toCharArray();
		int pos = 0;
		int i;
		for(i = 0; i < chs.length-1; i++) {
			chs[pos] = chs[i];
			if(chs[i]==' ' || chs[i]=='\n' || chs[i]=='\t' || chs[i]=='\r') {
				chs[pos] = ' ';
				if(pos==0) pos--;
				char c = chs[i+1];
				if(c==' ' || c=='\n' || c=='\t' || chs[i]=='\r') {
					do {
						i++;
						if(i+1>=chs.length) break;
						c = chs[i+1];
					} while(c==' ' || c=='\n' || c=='\t' || chs[i]=='\r');
				}
				pos++;
				continue;
			}
			pos++;
		}
		if(i<chs.length) {
			i = chs.length-1;
			if(chs[i]!=' ' && chs[i]!='\n' && chs[i]!='\t' || chs[i]=='\r') {
				chs[pos] = chs[i];
				pos++;
			}
		}
		if(pos==0) return new String();
		if(chs[pos-1]==' ') pos--;
		if(pos==0) return new String();
		return new String(chs,0,pos);
	}
	
	/**
	 * Use this method to extract circuit interface of VHDL module.
	 * This method is not VHDL93 compliant, and uses some heuristical
	 * decisions when extracting interface. Most notably, there is no
	 * support for array types. However, for many usual cases this will
	 * work.
	 * 
	 * @param source VHDL source contaning some module.
	 * @return CircuitInterface representation in form of
	 * 			{@linkplain hr.fer.zemris.vhdllab.vhdl.model.CircuitInterface} interface.
	 */
	public static CircuitInterface extractCircuitInterface(String source) {
		Entity entity = Extractor.extractInterface(source);
		CircuitInterface ci = Extractor.wrapToCircuitInterface(entity);
		return ci;
	}
	
	/**
	 * Use this method to extract circuit interface of VHDL module.
	 * This method is not VHDL93 compliant, and uses some heuristical
	 * decisions when extracting interface. Most notably, there is no
	 * support for array types. However, for many usual cases this will
	 * work.
	 * 
	 * @param source VHDL source contaning some module
	 * @return entity representation in form of {@linkplain Entity} class.
	 *         This method will return <code>null</code> if errors are encountered.
	 */
	private static Entity extractInterface(String source) {
		List<Generic_entry> generic_entries = new ArrayList<Generic_entry>();
		List<Port_entry> entity_entries = new ArrayList<Port_entry>();
		source = Extractor.decomment(source);
		source = Extractor.removeWhiteSpaces(source);
		source = source.toUpperCase();
		char[] chs = source.toCharArray();
		int pos = source.indexOf("ENTITY");
		if(pos==-1) {
			throw new NullPointerException();
			//return null;
		}
		pos += 7;
		int start = pos;
		while(chs[pos]!=' ') pos++;
		String entity_name = new String(chs,start,pos-start);
		pos++;
		if(!startsWith(chs,pos,ch_IS)||chs[pos+ch_IS.length]!=' '){
			throw new NullPointerException();
			//return null;
		}
		pos+=3;
		if(startsWith(chs,pos,ch_GENERIC)&&(chs[pos+ch_GENERIC.length]==' '||chs[pos+ch_GENERIC.length]=='(')) {
			if(chs[pos+ch_GENERIC.length]==' ') pos++;
			pos += ch_GENERIC.length;
			if(chs[pos]!='(') {
				throw new NullPointerException();
				//return null;
			}
			pos++;
			if(chs[pos]==' ') pos++;
			int[] res = new int[2];
			while(true) {
				List<String> nazivi = new ArrayList<String>();
				while(true) {
					findEnd(res, chs,pos,ch_DVOTOCKA_RAZMAK_ZAREZ_ZOBLA);
					if(res[0]==-1) {
						throw new NullPointerException();
						//return null;
					}
					if(res[0]==0) {
						// dvotocka:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==' ') pos++;
						break;
					}
					if(res[0]==1) {
						// razmak:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==':') {
							pos++;
							if(chs[pos]==' ') pos++;
							break;
						}
						if(chs[pos]==',') {
							pos++;
							if(chs[pos]==' ') pos++;
							continue;
						}
						throw new NullPointerException();
						//return null;

					}
					if(res[0]==2) {
						// zarez:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==' ') pos++;
						continue;
					}
					if(res[0]==3) break;
				}
				if(res[0]==3) {
					if(nazivi.isEmpty()) break;
					throw new NullPointerException();
					//return null;

				}
				start = pos;
				pos = findTypeEnd(chs,pos);
				String type_name = new String(chs, start, pos-start);
				String initializer_value = null;
				if(chs[pos]==' ') pos++;
				if(chs[pos]==':') {
					pos++;
					if(chs[pos]!='=') {
						throw new NullPointerException();
						//return null; // mora doci :=
					}
					pos++;
					if(chs[pos]==' ') pos++;
					start = pos;
					pos = findInitializerEnd(chs, pos);
					initializer_value = new String(chs,start,pos-start);
					
				}
				for(String naziv : nazivi) {
					Generic_entry ge = new Generic_entry(naziv,type_name,initializer_value);
					generic_entries.add(ge);					
				}
				if(chs[pos]==' ') pos++;
				if(chs[pos]!=';') {
					break;
				}
				pos++;
				if(chs[pos]==' ') pos++;
			}
			if(chs[pos]!=')') {
				throw new NullPointerException();
				//return null;
			}
			pos++; if(chs[pos]==' ') pos++;
			if(chs[pos]!=';') {
				throw new NullPointerException();
				//return null;
			}
			pos++; if(chs[pos]==' ') pos++;
		}

		if(startsWith(chs,pos,ch_PORT)) {
			pos += ch_PORT.length;
			if(chs[pos]==' ') pos++;
			if(chs[pos]!='(') {
				throw new NullPointerException();
				//return null;
			}
			pos++; if(chs[pos]==' ') pos++;
			int[] res = new int[2];
			while(true) {
				List<String> nazivi = new ArrayList<String>();
				while(true) {
					findEnd(res, chs,pos,ch_DVOTOCKA_RAZMAK_ZAREZ_ZOBLA);
					if(res[0]==-1) {
						throw new NullPointerException();
						//return null;

					}
					if(res[0]==0) {
						// dvotocka:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==' ') pos++;
						break;
					}
					if(res[0]==1) {
						// razmak:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==':') {
							pos++;
							if(chs[pos]==' ') pos++;
							break;
						}
						if(chs[pos]==',') {
							pos++;
							if(chs[pos]==' ') pos++;
							continue;
						}
						throw new NullPointerException();
						//return null;

					}
					if(res[0]==2) {
						// zarez:
						nazivi.add(new String(chs,pos,res[1]-pos));
						pos = res[1]+1;
						if(chs[pos]==' ') pos++;
						continue;
					}
					if(res[0]==3) break;
				}
				if(res[0]==3) {
					if(nazivi.isEmpty()) break;
					throw new NullPointerException();
					//return null;

				}
				String direction = null;
				int w = whichStarts(chs,pos,ch_IN_OUT_INOUT_BUFFER);
				if(w!=-1) {
					pos += ch_IN_OUT_INOUT_BUFFER[w].length;
					if(chs[pos]==' ') pos++;
					direction = new String(ch_IN_OUT_INOUT_BUFFER[w]);
				} else {
					direction = new String(ch_IN);
				}
				start = pos;
				pos = findTypeEnd(chs,pos);
				String type_name = new String(chs, start, pos-start).trim();
				//String initializer_value = null;
				if(chs[pos]==' ') pos++;
				if(chs[pos]==':') {
					pos++;
					if(chs[pos]!='=') {
						throw new NullPointerException();
						//return null; // mora doci :=
					}
					pos++;
					if(chs[pos]==' ') pos++;
					start = pos;
					pos = findInitializerEnd(chs, pos);
					//initializer_value = new String(chs,start,pos-start);
					
				}
				Range range = null;
				int x = type_name.indexOf('(');
				if(x>0) {
					String rangeText = type_name.substring(x);
					type_name = type_name.substring(0,x).trim();
					range = Range.extractRange(rangeText);
				}
				for(String naziv : nazivi) {
					Port_entry ee = new Port_entry(naziv,direction,type_name,range);
					entity_entries.add(ee);
				}
				if(chs[pos]==' ') pos++;
				if(chs[pos]!=';') {
					break;
				}
				pos++;
				if(chs[pos]==' ') pos++;
			}
			if(chs[pos]!=')') {
				throw new NullPointerException();
				//return null;

			}
			pos++; if(chs[pos]==' ') pos++;
			if(chs[pos]!=';') {
				throw new NullPointerException();
				//return null;

			}
			pos++; if(chs[pos]==' ') pos++;
		
		}

		return new Entity(entity_name, generic_entries,entity_entries);
	}
	
	/**
	 * Wraps <code>Entity</code> to <code>CircuitInterface</code>.
	 * 
	 * @param entity an entity to be wrapped.
	 * @return an instance of CircuitInterface that contains all 
	 * 		   information of <code>entity</code>.
	 */
	private static CircuitInterface wrapToCircuitInterface(Extractor.Entity entity) {
		if(entity==null) throw new NullPointerException("Entity can not be null.");
		List<Port> ports = new ArrayList<Port>(entity.getPort_part().size());
		for(Extractor.Port_entry p : entity.getPort_part()) {
			String name = p.getName();
			String mode = p.getMode();
			String type = p.getType();
			Extractor.Range range = p.getRange();
			
			int[] r = null;
			if(range!=null) r = new int[] {Integer.parseInt(range.getFrom()), Integer.parseInt(range.getTo())};
			
			Type t = null;
			if(r==null) t = new DefaultType(type, DefaultType.SCALAR_RANGE, DefaultType.SCALAR_VECTOR_DIRECTION);
			else t = new DefaultType(type, r, range.getDirection());
			
			Direction dir = null;
			if(mode.equalsIgnoreCase(String.valueOf(ch_IN))) dir = Direction.IN;
			else if(mode.equalsIgnoreCase(String.valueOf(ch_OUT))) dir = Direction.OUT;
			else if(mode.equalsIgnoreCase(String.valueOf(ch_INOUT))) dir = Direction.INOUT;
			else if(mode.equalsIgnoreCase(String.valueOf(ch_BUFFER))) dir = Direction.BUFFER;
			
			ports.add(new DefaultPort(name, dir, t));
		}
		return new DefaultCircuitInterface(entity.getEntity_name(), ports);
	}
	
	/**
	 * This method finds position in given VHDL source represented
	 * as character array, starting from given initial position, of
	 * character which terminates signal initializer. This can be one
	 * of: ';', ')'. However, character ')' is considered terminator
	 * only if it is not closure of some '(' opened after the initial
	 * position.
	 * @param chs VHDL source represented as character array
	 * @param pos initial position from where to start the search
	 * @return position of character terminating signal initializer 
	 */
	private static int findInitializerEnd(char[] chs, int pos) {
		while(true) {
			if(chs[pos]==';' || chs[pos]==')') return pos;
			if(chs[pos]=='(') {
				int cnt=1;
				for(pos=pos+1; true; pos++) {
					if(chs[pos]=='(') cnt++;
					if(chs[pos]==')') {
						cnt--;
						if(cnt==0) {
							break;
						}
					}
				}
			}
			pos++;
		}
	}

	/**
	 * This method finds position in given VHDL source represented
	 * as character array, starting from given initial position, of
	 * character which terminates signal type declaration. This can be
	 * one of: ':', ';', ')'. However, character ')' is considered
	 * terminator only if it is not closure of some '(' opened after 
	 * the initial position.
	 * @param chs VHDL source represented as character array
	 * @param pos initial position from where to start the search
	 * @return position of character terminating signal type declaration 
	 */
	private static int findTypeEnd(char[] chs, int pos) {
		while(true) {
			if(chs[pos]==':' || chs[pos]==';' || chs[pos]==')') return pos;
			if(chs[pos]=='(') {
				int cnt=1;
				for(pos=pos+1; true; pos++) {
					if(chs[pos]=='(') cnt++;
					if(chs[pos]==')') {
						cnt--;
						if(cnt==0) {
							break;
						}
					}
				}
			}
			pos++;
		}
	}

	/**
	 * Use this method to find out which of character successions starts
	 * in given array at given position. Candidates are given as last
	 * parameter. If no candidate starts at given position, method will
	 * return -1.<br>
	 * For example:<br>
	 * <code>
	 * char[] text = new char[] {'S','i','g','n','a','l',' ','x',' ',':',' ','I','N'};<br>
	 * char[][] options = new char[][] {<br>
	 *   new char[] {'O','U','T'},<br>
	 *   new char[] {'I','N'},<br>
	 *   new char[] {'B','U','F','F','E','R'}<br>
	 * };<br>
	 * int which = whichStarts(text,11,options);<br>
	 * </code><br>
	 * will result with <code>which = 1;</code>
	 * @param buf character array in which we want to perform search
	 * @param pos position in buf from where the search will be performed
	 * @param options zero or more character array which will be looked for.
	 * @return index of the first array which is also found in buf at given
	 *          position, or -1 if no sush array exists.
	 */
	private static int whichStarts(char[] buf, int pos, char[][] options) {
		for(int i = 0; i < options.length; i++) {
			if(startsWith(buf,pos,options[i])) return i;
		}
		return -1;
	}
	
	/**
	 * Use this method to find where is the first occurence of any of given
	 * craracter successions (last parameter) in given array (first parameter)
	 * starting from given initial position.
	 * @param res where to store result of find operation. This must be an array
	 *        containing two elements. After the search is done, element with index
	 *        0 will be set to the index of option that was first found. Element 
	 *        with index 1 will be set to the position where this match starts.
	 * @param buf where to find
	 * @param pos from which position to start search
	 * @param options an array of character successions which are valid candidates 
	 */
	private static void findEnd(int[] res, char[] buf, int pos, char[][] options) {
		while(pos < buf.length) {
			int w = whichStarts(buf, pos, options);
			if(w!=-1) {
				res[0] = w;
				res[1] = pos;
				return;
			}
			pos++;
		}
		res[0] = -1;
	}

	/**
	 * Use this method to check if character array on given position
	 * contains given sub-array. 
	 * @param buf original array on which check is performed 
	 * @param pos start position
	 * @param option sub-array
	 * @return returns true if sub-array is found in original array
	 *         at given position, or false otherwise 
	 */
	private static boolean startsWith(char[] buf, int pos, char[] option) {
		if(pos+option.length>buf.length) return false;
		for(int i = 0; i < option.length; i++) {
			if(buf[pos+i]!=option[i]) return false;
		}
		return true;
	}

	/**
	 * <p>This class represents a single generic entry declaration.</p>
	 * @author marcupic
	 */
	public static class Generic_entry {
		private String name;
		private String type;
		private String value;
		
		/**
		 * Constructor.
		 * @param name name of parameter
		 * @param type type of parameter
		 * @param value initial value of parameter; this can be null.
		 */
		public Generic_entry(String name, String type, String value) {
			super();
			if(name==null) throw new NullPointerException("Generic parameter name can not be null.");
			if(type==null) throw new NullPointerException("Generic parameter type can not be null.");
			this.name = name;
			this.type = type;
			this.value = value;
		}

		/**
		 * Getter for parameter name.
		 * @return parameter name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter for parameter type.
		 * @return parameter type
		 */
		public String getType() {
			return type;
		}

		/**
		 * Getter for parameter initial value.
		 * @return parameter initial value; this can be null.
		 */
		public String getValue() {
			return value;
		}

		/**
		 * This method can compare object with {@linkplain String} or 
		 * with other {@linkplain Generic_entry}. Object is equal to
		 * a {@linkplain String} if its name is equal to given
		 * {@linkplain String}. Object is equal to {@linkplain Generic_entry} 
		 * if all components are equal.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object arg0) {
			if(arg0==null) return false;
			if(arg0 instanceof String) {
				return name.equalsIgnoreCase((String)arg0);
			}
			if(arg0 instanceof Generic_entry) {
				Generic_entry ge = (Generic_entry)arg0;
				return name.equals(ge.name) && type.equals(ge.type) && ((value==null && ge.value==null) || (value!=null && value.equals(ge.value)));
			}
			return false;
		}
	}
	
	/**
	 * <p>This class represents a single port declaration.</p>
	 * @author marcupic
	 */
	public static class Port_entry {
		private String name;
		private String mode;
		private String type;
		private Range range;
		
		/**
		 * Constructor.
		 * @param name port name
		 * @param mode port mode (i.e. direction)
		 * @param type port type
		 * @param range port type range, if type is vector. This can be null.
		 */
		public Port_entry(String name, String mode, String type, Range range) {
			super();
			if(name==null) throw new NullPointerException("Port name can not be null.");
			if(type==null) throw new NullPointerException("Port type can not be null.");
			if(mode==null) throw new NullPointerException("Port mode can not be null.");
			this.name = name;
			this.mode = mode;
			this.type = type;
			this.range = range;
		}

		/**
		 * Getter method for port mode.
		 * @return port mode
		 */
		public String getMode() {
			return mode;
		}

		/**
		 * Getter method for port name.
		 * @return port name
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter method for port range.
		 * @return port range; can be null
		 */
		public Range getRange() {
			return range;
		}

		/**
		 * Getter method for port type.
		 * @return port type
		 */
		public String getType() {
			return type;
		}

		/**
		 * This method can compare object with {@linkplain String} or 
		 * with other {@linkplain Port_entry}. Object is equal to
		 * a {@linkplain String} if its name is equal to given
		 * {@linkplain String}. Object is equal to {@linkplain Port_entry} 
		 * if all components are equal.
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object arg0) {
			if(arg0==null) return false;
			if(arg0 instanceof String) {
				return name.equalsIgnoreCase((String)arg0);
			}
			if(!(arg0 instanceof Port_entry)) return false;
			Port_entry other = (Port_entry)arg0;
			if(!name.equalsIgnoreCase(other.name) || !mode.equalsIgnoreCase(other.mode) || !type.equalsIgnoreCase(other.type)) return false;
			if((range!=null && !range.equals(other.range)) || (range==null && other.range!=null)) return false;
			return true;
		}
	}
	
	/**
	 * <p>This class represents a discrete range.</p>
	 * @author marcupic
	 */
	public static class Range {
		private String from;
		private String to;
		private String direction;
		
		/**
		 * Constructor.
		 * @param from start of the range; this can be some generic parameter name or even expression
		 * @param to end f the range; this can be some generic parameter name or even expression
		 * @param direction direction (TO or DOWNTO)
		 */
		public Range(String from, String to, String direction) {
			super();
			this.from = from;
			this.to = to;
			this.direction = direction;
		}

		/**
		 * Getter method for range direction.
		 * @return range direction
		 */
		public String getDirection() {
			return direction;
		}

		/**
		 * Getter method for interval start.
		 * @return start of interval
		 */
		public String getFrom() {
			return from;
		}

		/**
		 * Getter method for interval end.
		 * @return end of interval
		 */
		public String getTo() {
			return to;
		}

		@Override
		public boolean equals(Object arg0) {
			if(arg0==null) return false;
			if(!(arg0 instanceof Range)) return false;
			Range other = (Range)arg0;
			return from.equals(other.from) && to.equals(other.to) && direction.equals(other.direction);
		}
		
		@Override
		public String toString() {
			return "("+from+" "+direction+" "+to+")";
		}

		/**
		 * Use this method to extract range from string. This method support following
		 * form of range:<br>
		 * <code>(X TO Y)</code> or <code>(X DOWNTO Y)</code><br>
		 * where <code>X</code> and <code>Y</code> can be numbers or parameter names.
		 * @param rangeText string representaion of range
		 * @return Range object representing given range, or <code>null</code>
		 *         if error is encountered.
		 */
		public static Range extractRange(String rangeText) {
			rangeText = rangeText.substring(1,rangeText.length()-1).trim();
			int pos1 = rangeText.indexOf(' ');
			String from = rangeText.substring(0,pos1);
			rangeText = rangeText.substring(pos1+1);
			int pos2 = rangeText.indexOf(' ');
			String direction = rangeText.substring(0,pos2);
			String to = rangeText.substring(pos2+1);
			if(!direction.equals("TO")&&!direction.equals("DOWNTO")) return null;
			return new Range(from,to,direction);
		}
	}
	
	/**
	 * <p>This class represents an entity of VHDL module. It contains
	 * generic and port part.</p>
	 * @author marcupic
	 */
	public static class Entity {
		private String entity_name;
		private List<Generic_entry> generic_part;
		private List<Port_entry> port_part;
		
		/**
		 * Constructor.
		 * @param generic_part list of {@linkplain Generic_entry} elements
		 * @param port_part list of {@linkplain Port_entry} elements
		 */
		public Entity(String entity_name, List<Generic_entry> generic_part, List<Port_entry> port_part) {
			super();
			if(entity_name==null) throw new NullPointerException("Entity_name can not be null.");
			if(generic_part==null) throw new NullPointerException("Generic_part can not be null.");
			if(port_part==null) throw new NullPointerException("Port_part can not be null.");
			this.entity_name = entity_name;
			this.generic_part = Collections.unmodifiableList(new LinkedList<Generic_entry>(generic_part));
			this.port_part = Collections.unmodifiableList(new LinkedList<Port_entry>(port_part));
		}
		
		/**
		 * Getter for entity name of interface.
		 * @return entity name
		 */
		public String getEntity_name() {
			return entity_name;
		}

		/**
		 * Getter for generic part of interface.
		 * @return list of generic parameters
		 */
		public List<Generic_entry> getGeneric_part() {
			return generic_part;
		}

		/**
		 * Getter for port part of interface.
		 * @return list of ports
		 */
		public List<Port_entry> getPort_part() {
			return port_part;
		}
		
		@Override
		public boolean equals(Object arg0) {
			if(arg0==null) return false;
			if(!(arg0 instanceof Entity)) return false;
			Entity other = (Entity)arg0;
			if(generic_part==null && other.generic_part!=null) return false;
			if(generic_part!=null && other.generic_part==null) return false;
			if(!generic_part.equals(other.generic_part)) return false;
			if(port_part==null && other.port_part!=null) return false;
			if(port_part!=null && other.port_part==null) return false;
			if(!port_part.equals(other.port_part)) return false;
			return true;
		}
	}
	
	/**
	 * Helper method. This method reads contents of given file
	 * and return it as a string.
	 * @param fileName name of file to read
	 * @return string which corresponds to content of file; <code>null</code> 
	 *         if file could not be read or other error occur.
	 */
	public static String readFile(String fileName) {
		java.io.File f = new java.io.File(fileName);
		StringBuilder sb = new StringBuilder((int)f.length());
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(f));
			char[] buf = new char[1024*16];
			while(true) {
				int read = br.read(buf);
				if(read<1) break;
				sb.append(buf,0,read);
			}
		} catch(Exception ex) {
			return null;
		} finally {
			if(br!=null) try {br.close();} catch(Exception ex) {}
		}
		return sb.toString();
	}

	/* ----------------------------------  
	 * ADDED ON 2006-08-15 
	 * ---------------------------------- */

	public static interface VHDLSourceProvider {
		public String provide(String identifier) throws Exception;
	}
	
	public static interface EntityIdentifierPair {
		public String getEntityName();
		public String getIdentifier();
	}
	
	public static interface EntityIdentifierBundle {
		public EntityIdentifierPair getEntityIdentifierPairByEntity(String entityName);
		public EntityIdentifierPair getEntityIdentifierPairByIdentifier(String identifier);
		public Iterator<EntityIdentifierPair> iterator();
		public boolean existsEntity(String entityName);
	}
	
	public static class DefaultEntityIdentifierPair implements EntityIdentifierPair {

		private String entity_name;
		private String identifier;

		
		public DefaultEntityIdentifierPair(String entity_name, String identifier) {
			super();
			if(entity_name==null) throw new NullPointerException("Entity name must not be null!");
			if(identifier==null) throw new NullPointerException("Identifier must not be null!");
			this.entity_name = entity_name;
			this.identifier = identifier;
		}

		public String getEntityName() {
			return entity_name;
		}

		public String getIdentifier() {
			return identifier;
		}
		
		@Override
		public boolean equals(Object arg0) {
			if(arg0==null) return false;
			if(!(arg0 instanceof EntityIdentifierPair)) return false;
			EntityIdentifierPair other = (EntityIdentifierPair)arg0;
			return entity_name.equalsIgnoreCase(other.getEntityName()) && identifier.equals(other.getIdentifier());
		}
	}
	
	public static class DefaultEntityIdentifierBundle implements EntityIdentifierBundle, Iterable<EntityIdentifierPair> {

		private List<EntityIdentifierPair> list = new ArrayList<EntityIdentifierPair>();
		private Map<String, EntityIdentifierPair> mapByEntityName = new HashMap<String,EntityIdentifierPair>();
		private Map<String, EntityIdentifierPair> mapByIdentifier = new HashMap<String,EntityIdentifierPair>();
		
		public EntityIdentifierPair getEntityIdentifierPairByEntity(String entityName) {
			return mapByEntityName.get(entityName.toUpperCase());
		}

		public EntityIdentifierPair getEntityIdentifierPairByIdentifier(String identifier) {
			return mapByIdentifier.get(identifier);
		}

		public Iterator<EntityIdentifierPair> iterator() {
			return list.iterator();
		}
		
		public void addEntityIdentifierPair(EntityIdentifierPair pair) {
			if(pair==null) throw new NullPointerException("Pair must not be null!");
			list.add(pair);
			mapByEntityName.put(pair.getEntityName().toUpperCase(),pair);
			mapByIdentifier.put(pair.getIdentifier(),pair);
		}

		public boolean existsEntity(String entityName) {
			return getEntityIdentifierPairByEntity(entityName)!=null;
		}
		
	}
	
	private static class UsageMap {
		private Map<String,Set<String>> usageMap = new HashMap<String,Set<String>>();
		private Map<String,Set<String>> reverseUsageMap = new HashMap<String,Set<String>>();
		
		public void addUsage(String entity, String usedEntity) {
			entity = entity.toUpperCase();
			usedEntity = usedEntity.toUpperCase();
			// This piece of code here stores the fact that "entity" uses "usedEntity".
			// This will enable us later to directly answer the question:
			// which entities are used by given entity - result will be set.
			Set<String> usageSet = usageMap.get(entity);
			if(usageSet==null) {
				usageSet = new HashSet<String>();
				usageMap.put(entity, usageSet);
			}
			usageSet.add(usedEntity);
			// This piece of code here stores the fact that "usedEntity" is used by "entity".
			// This will enable us later to directly answer the question:
			// which entities uses given entity - result will be set.
			usageSet = reverseUsageMap.get(usedEntity);
			if(usageSet==null) {
				usageSet = new HashSet<String>();
				reverseUsageMap.put(usedEntity, usageSet);
			}
			usageSet.add(entity);
		}
		
		public Set<String> getEntitiesUsedByEntity(String entity) {
			return usageMap.get(entity.toUpperCase());
		}

		public Set<String> getEntitiesWhichUseEntity(String entity) {
			return reverseUsageMap.get(entity.toUpperCase());
		}
	}
	
	public static Hierarchy extractHierarchy(Project project, VHDLLabManager labman) throws Exception {
		Hierarchy h = new Hierarchy(project.getProjectName());
		Set<File> files = project.getFiles();
		if(files == null) return h;
		for(File f : files) {
			Pair pair = h.getPair(f.getFileName());
			if(pair == null) {
				pair = new Pair(f.getFileName(), f.getFileType());
				h.addPair(pair);
			}
			createHierarchy(f, h, labman);
		}
		return h;
	}
	
	private static void createHierarchy(File file, Hierarchy h, VHDLLabManager labman) throws ServiceException {
		if(file == null || file.getContent() == null) return;
//		String source = labman.generateVHDL(file);
//		Set<String> usedComponents = Extractor.extractUsedComponents(source);
		List<File> usedComponents = labman.extractDependenciesDisp(file);
		usedComponents.remove(file);
		for(File component : usedComponents) {
			Pair pair = h.getPair(component.getFileName());
			if(pair == null) {
				h.addPair(new Pair(component.getFileName(), component.getFileType()));
				pair = h.getPair(component.getFileName());
			}
			pair.addParent(file.getFileName());
			createHierarchy(component, h, labman);
		}
	}
	
	/*private static void createHierarchy(String name, UsageMap usageMap, Map<String, Pair> pairs) {
		if(pairs.get(name) == null) {
			pairs.put(name, new Pair(name, new TreeSet<String>()));
		}
		Set<String> set = usageMap.getEntitiesUsedByEntity(name);
		if(set==null) return;
		for(String name2 : set) {
			createHierarchy(name2, usageMap, pairs);
			Set<String> parents = new TreeSet<String>();
			parents.addAll(pairs.get(name2).getParents());
			parents.add(name);
			pairs.put(name2, new Pair(name2, parents));
		}
	}*/

	private final static char[] ch_WORK = "WORK".toCharArray();

	public static Set<String> extractUsedComponents(String source) {
		Set<String> result = new HashSet<String>();
		source = Extractor.decomment(source);
		source = Extractor.removeWhiteSpaces(source);
		source = source.toUpperCase();
		char[] chs = source.toCharArray();
		int pos = 0;
		while(true) {
			pos = source.indexOf("ENTITY",pos);
			if(pos==-1) break;
			pos += 6;
			if(chs[pos]!=' ') continue;
			pos++;
			if(!startsWith(chs,pos,ch_WORK)) continue;
			pos += ch_WORK.length;
			if(chs[pos]==' ') pos++;
			if(chs[pos]!='.') continue;
			pos++;
			if(chs[pos]==' ') pos++;
			int start = pos;
			while(chs[pos]!=' ') pos++;
			String component_name = new String(chs,start,pos-start);
			result.add(component_name);
		}
		pos = 0;
		while(true) {
			pos = source.indexOf("COMPONENT",pos);
			if(pos==-1) break;
			pos += 9;
			if(chs[pos]!=' ') continue;
			pos++;
			if(chs[pos]==';') continue;
			int start = pos;
			while(chs[pos]!=' ') pos++;
			String component_name = new String(chs,start,pos-start);
			result.add(component_name);
		}
		return result;
	}

}
