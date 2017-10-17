package test;

import java.util.List;

import impd.CustomerDAOJdbcIMpl;

import org.junit.Test;

import domain.Customer;
import wzg.CriteriaCustomer;
import wzg.CustomerDAO;

public class CustomerDAOJdbcIMplTest {

	private CustomerDAO customerDAO = new CustomerDAOJdbcIMpl();
	
	@Test
	public void getForListWithCriteriaCustomer(){
		CriteriaCustomer cc = new CriteriaCustomer("d",null, null);
		List<Customer> customers = customerDAO.getForListWithCriteriaCustomer(cc);
		System.out.println(customers);
		
		
	}
	
	@Test
	public void testGetAll() {
		List<Customer> customers = customerDAO.getAll();
		System.out.println(customers);
	}

	@Test
	public void testSave() {
		Customer customer = new Customer();
		customer.setName("dd");
		customer.setAddress("hangzhou");;
		customer.setPhone("789456");
		customerDAO.save(customer);
	}

	@Test
	public void testGetInteger() {
		Customer customer = customerDAO.get(3);
		System.out.println(customer);
	}

	@Test
	public void testDelete() {
		customerDAO.delete(1);
	}

	@Test
	public void testGetCountWithName() {
		long count = customerDAO.getCountWithName("dd");
		System.out.println(count);
	}

}
