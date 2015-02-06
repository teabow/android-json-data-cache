package com.teabow.app.jsoncache.db.model;

public class DBField implements Comparable<DBField> {

	public enum Constraint {
		PRIMARY, NONE
	};
	
	private String name;
	
	private String type;
	
	private int columnIndex;
	
	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	private Constraint constraint;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + columnIndex;
		result = prime * result
				+ ((constraint == null) ? 0 : constraint.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		DBField other = (DBField) obj;
		if (columnIndex != other.columnIndex)
			return false;
		if (constraint != other.constraint)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DBField [name=" + name + ", type=" + type + ", columnIndex="
				+ columnIndex + ", constraint=" + constraint + "]";
	}

	public void setType(String type) {
		this.type = type;
	}

	public Constraint getConstraint() {
		return constraint;
	}

	public void setConstraint(Constraint constraint) {
		this.constraint = constraint;
	}

	@Override
	public int compareTo(DBField another) {
		return this.columnIndex - another.columnIndex;
	}
	

}
