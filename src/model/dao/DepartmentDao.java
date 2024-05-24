package model.dao;

import java.util.List;

import model.entities.Department;

public interface DepartmentDao {
	public void create(Department dep);
	
	public void update(Department dep);
	
	public void delete(Integer id);
	
	public Department read(Integer id);
	
	public List<Department> findAll();
}
