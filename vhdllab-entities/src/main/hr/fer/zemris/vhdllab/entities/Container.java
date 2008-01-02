package hr.fer.zemris.vhdllab.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class Container<T extends Resource<?>> implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	private String name;
	private Date created;
	private Set<T> children;
	
	public Container() {
	}

	public Container(Container<T> c) {
		this.id = c.id;
		this.name = c.name;
		this.created = c.created;
		this.children = c.children;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", updatable = false, insertable = false)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Basic
	@Column(name = "name", length = 255, nullable = false)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Set<T> getChildren() {
		return children;
	}

	public void setChildren(Set<T> children) {
		this.children = children;
	}
	
}
