package hr.fer.zemris.vhdllab.test;

import hr.fer.zemris.vhdllab.entities.Caseless;
import hr.fer.zemris.vhdllab.entities.FileType;

/**
 * A simple structure that {@link FileContentProvider#getContent(FileType)}
 * returns.
 * 
 * @author Miro Bezjak
 * @version 1.0
 * @since vhdllab2
 */
public class NameAndContent {

	private String content;
	private Caseless name;

	public NameAndContent(Caseless name, String content) {
		super();
		this.name = name;
		this.content = content;
	}

	public String getContent() {
		return content;
	}

	public Caseless getName() {
		return name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((content == null) ? 0 : content.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final NameAndContent other = (NameAndContent) obj;
		if (content == null) {
			if (other.content != null)
				return false;
		} else if (!content.equals(other.content))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "name=" + name + ", content=" + content;
	}
}
