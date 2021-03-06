package com.websystique.springmvc.service;

import java.util.List;

import com.websystique.springmvc.model.Employee;

public interface EmployeeService {

	Employee findById(int id);
	
	void saveEmployee(Employee employee);
	
	void updateEmployee(Employee employee);
	
	void deleteEmployeeByEmail(String email);

	List<Employee> findAllEmployees(); 
	
	Employee findEmployeeByEmail(String email);
	
	void pdftoData(String name,String email,String pos);

	boolean isEmployeeEmailUnique(Integer id, String email);
	
	boolean doesEmployeeExists(String email);
	
}
