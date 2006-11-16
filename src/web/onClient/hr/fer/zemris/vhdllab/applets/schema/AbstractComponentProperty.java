package hr.fer.zemris.vhdllab.applets.schema;

/**
 * Ovo je apstraktna klasa za svako svojstvo.
 * Ona vraca ime svojstva i tzv. PropFields,
 * iliti polje svojstva - Java komponentu (ne sklop!!)
 * koja ce se pojaviti u listi 
 * @author Axel
 *
 */
public abstract class AbstractComponentProperty {
	private String name;
	
	public AbstractComponentProperty(String nm) {
		name = nm;
	}
	
	public String getPropertyName() {
		return name;
	}
}
