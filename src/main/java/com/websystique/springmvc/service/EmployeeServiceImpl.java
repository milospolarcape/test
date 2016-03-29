package com.websystique.springmvc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.websystique.springmvc.dao.EmployeeDao;
import com.websystique.springmvc.model.Employee;

@Service("employeeService")
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDao dao;
	
	public Employee findById(int id) {
		return dao.findById(id);
	}

	public void saveEmployee(Employee employee) {
		dao.saveEmployee(employee);
	}
	
	/*
	 * Since the method is running with Transaction, No need to call hibernate update explicitly.
	 * Just fetch the entity from db and update it with proper values within transaction.
	 * It will be updated in db once transaction ends. 
	 */
	public void updateEmployee(Employee employee) {
		Employee entity = dao.findById(employee.getId());
		if(entity!=null){
			entity.setName(employee.getName());
			entity.setEmail(employee.getEmail());
			entity.setPosition(employee.getPosition());
		}
	}
	public void pdftoData(String name,String email,String pos){
		dao.pdftoData(name, email, pos);
	}
	public void deleteEmployeeByEmail(String email) {
		dao.deleteEmployeeByEmail(email);
	}
	
	public List<Employee> findAllEmployees() {
		return dao.findAllEmployees();
	}

	public Employee findEmployeeByEmail(String email) {
		return dao.findEmployeeByEmail(email);
	}

	public boolean isEmployeeEmailUnique(Integer id, String email) {
		Employee employee = findEmployeeByEmail(email);
		return ( employee == null || ((id != null) && (employee.getId() == id)));
	}
	public boolean doesEmployeeExists(String email) {
		Employee employee = findEmployeeByEmail(email);
		return ( employee == null);
	}
	
}
