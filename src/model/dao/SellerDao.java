package model.dao;

import java.util.List;

import model.entities.Department;
import model.entities.Seller;

public interface SellerDao {
	public void create(Seller dep);
	
	public void update(Seller dep);
	
	public void delete(Integer id);
	
	public Seller read(Integer id);
	
	public List<Seller> readAll();
	
	public List<Seller> read2(Department department);
}
