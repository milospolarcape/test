package com.websystique.springmvc.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.websystique.springmvc.model.Employee;

@Repository("employeeDao")
public class EmployeeDaoImpl extends AbstractDao<Integer, Employee> implements EmployeeDao {

	public Employee findById(int id) {
		return getByKey(id);
	}

	public void saveEmployee(Employee employee) {
		persist(employee);
	}
	public void pdftoData(String name,String email,String pos){
	/*Query query = getSession().createSQLQuery("INSERT INTO employee (name, Email, position) VALUES (:name, :email, :position)");
		query.setParameter("name", name);
		query.setParameter("email", email);
		query.setParameter("position", pos);
		query.executeUpdate();*/
		Employee e=new Employee();
		e.setName(name);
		e.setEmail(email);
		e.setPosition(pos);
		saveEmployee(e);
		
	}
	public void deleteEmployeeByEmail(String email) {
		Query query = getSession().createSQLQuery("delete from employee where Email = :email");
		query.setString("email", email);
		query.executeUpdate();
	}

	@SuppressWarnings("unchecked")
	public List<Employee> findAllEmployees() {
		Criteria criteria = createEntityCriteria();
		return (List<Employee>) criteria.list();
	}

	public Employee findEmployeeByEmail(String email) {
		Criteria criteria = createEntityCriteria();
		criteria.add(Restrictions.eq("Email", email));
		return (Employee) criteria.uniqueResult();
	}
}
