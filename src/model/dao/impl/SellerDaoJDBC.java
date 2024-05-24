package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
	public void create(Seller dep) {
		
	}

	@Override
	public void update(Seller dep) {
		
	}

	@Override
	public void delete(Integer id) {
		
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
				Department temp = new Department();
				temp.setId(id);
				rs.getInt("DepartmentId");
				temp.setName(rs.getString("depname"));
				Seller sel = new Seller();
				sel.setId(rs.getInt("Id"));
				sel.setName(rs.getString("Name"));
				sel.setMail(rs.getString("Email"));
				sel.setBaseSalary(rs.getDouble("BaseSalary"));
				sel.setBirthDate(rs.getDate("BirthDate"));
				sel.setDepartment(temp);
				return sel;
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
	public List<Seller> findAll() {
		
		return null;
	}
	
}
