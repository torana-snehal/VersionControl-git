package com.icedq.versioncontrol.model;

public class Customer {

	private String firstName;
	private String lastName;
	private String email;

	private static Customer customer;
	
	public static Customer getCustomer() {
		if(customer == null) {
			customer = new Customer();
		}
		return customer;
	}
	
	public Customer() {
	}

	public Customer(String firstName, String lastName, String email) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + "]";
	}	
}
