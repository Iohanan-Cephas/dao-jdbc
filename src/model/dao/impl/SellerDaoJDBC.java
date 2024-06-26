package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{

	private Connection cnct;
	
	public SellerDaoJDBC(Connection cnct) {
		this.cnct = cnct;
	}
	
	@Override
	public void create(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = cnct.prepareStatement("insert into seller (Name, Email, BirthDate, BaseSalary, DepartmentId) values (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getMail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = ps.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			} else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void update(Seller obj) {
		PreparedStatement ps = null;
		
		try {
			ps = cnct.prepareStatement("update seller set Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? where Id = ?");
			
			ps.setString(1, obj.getName());
			ps.setString(2, obj.getMail());
			ps.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			ps.setDouble(4, obj.getBaseSalary());
			ps.setInt(5, obj.getDepartment().getId());
			ps.setInt(6,  obj.getId());
			
			ps.executeUpdate();
			
		} catch(SQLException e) {
			throw new DbException(e.getMessage());
		} finally {
			DB.closeStatement(ps);
		}
	}

	@Override
	public void delete(Integer id) {
		PreparedStatement ps = null;
		
		try {
			
			ps = cnct.prepareStatement("delete from seller where id = ?");
			ps.setInt(1, id);
			int rowsAffected = ps.executeUpdate();
			
			if(rowsAffected == 0) {
				throw new DbException("No seller found with the provided ID.");
			}
			
		} catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		} finally {
			
			DB.closeStatement(ps);
			
		}
	}

	@Override
	public Seller read(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = cnct.prepareStatement("select seller.*, department.Name as depname from seller inner join department on seller.DepartmentId = department.Id where seller.Id = ?");
			
			ps.setInt(1, id);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				
				Department temp = instantiateDepartment(rs);
				Seller temp2 = instantiateSeller(rs, temp);
				
				return temp2;
				
			}
			return null;
		}catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		}finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public List<Seller> readAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = cnct.prepareStatement("select seller.*, department.Name as depname from seller inner join department on seller.DepartmentId = department.Id order by name");
			
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				Department temp = map.get(rs.getInt("DepartmentId"));
				
				if(temp == null) {
					temp = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), temp);
				}
				
				Seller temp2 = instantiateSeller(rs, temp);
				list.add(temp2);
				
			}
			return list;
		}catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		}finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	@Override
	public List<Seller> read2(Department dep){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = cnct.prepareStatement("select seller.*, department.Name as depname from seller inner join department on seller.DepartmentId = department.Id where department.Id = ? order by name");
			
			ps.setInt(1, dep.getId());
			rs = ps.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while(rs.next()) {
				
				Department temp = map.get(rs.getInt("DepartmentId"));
				
				if(temp == null) {
					temp = instantiateDepartment(rs);
					map.put(rs.getInt("DepartmentId"), temp);
				}
				
				Seller temp2 = instantiateSeller(rs, temp);
				list.add(temp2);
				
			}
			return list;
		}catch(SQLException e) {
			
			throw new DbException(e.getMessage());
			
		}finally {
			
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}
	
	private Department instantiateDepartment(ResultSet rs) throws SQLException{
		Department temp = new Department();
		temp.setId(rs.getInt("DepartmentId"));
		temp.setName(rs.getString("depname"));
		return temp;
	}
	
	private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setMail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartment(dep);
		return sel;
	}
	
}
