package wzg;

import java.util.List;

import domain.Customer;

public interface CustomerDAO {
	
	public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc);
	
	public List<Customer> getAll();
	
	public void save(Customer customer);
	
	public Customer get(Integer id);
	
	public void update(Customer customer);
	
	public void delete(Integer id);
	
	/*
	 * 返回和name 相等的记录数
	 */
	public long getCountWithName(String name);
	
}
