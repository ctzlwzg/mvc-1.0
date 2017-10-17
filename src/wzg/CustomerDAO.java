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
	 * ���غ�name ��ȵļ�¼��
	 */
	public long getCountWithName(String name);
	
}
