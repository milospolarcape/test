package com.websystique.springmvc.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name="employee")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Size(min=3, max=50)
	@Column(name = "Email", nullable = false, unique=true)
	private String Email;
	
	@Size(min=3, max=50)
	@Column(name = "position", nullable = false)
	private String position;
	
	@Size(min=3, max=50)
	@Column(name = "name", nullable = false)
	private String name;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Employee))
			return false;
		Employee other = (Employee) obj;
		if (id != other.id)
			return false;
		if (Email == null) {
			if (other.Email != null)
				return false;
		} else if (!Email.equals(other.Email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", name=" + name + ", Email="
				+ Email + ", Position=" + position + "]";
	}
}
