package application;


import java.util.Date;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class Program {
	public static void main(String[] args) {

		SellerDao sellerDao = DaoFactory.createSellerDao();
		
		System.out.println("=== TEST 1: Seller Read =====");
		Seller seller = sellerDao.read(8);
		System.out.println(seller);
		System.out.println();
		
		System.out.println("=== TEST 2: Seller Read2 =====");
		Department department = new Department(2, null);
		List<Seller> list = sellerDao.read2(department);
		for(Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println();
		
		System.out.println("=== TEST 3: Seller ReadAll =====");
		list = sellerDao.readAll();
		for(Seller obj : list) {
			System.out.println(obj);
		}
		System.out.println();
		
		/* Funcionou!
		System.out.println("=== TEST 4: New Seller =====");
		Seller newSeller = new Seller(null, "Greg", "greg@gmail.com", new Date(), 4000.0, department);
		sellerDao.create(newSeller);
		System.out.println("Inserted! New id = " + newSeller.getId());
		System.out.println();*/
		
	}
}
