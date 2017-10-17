package impd;

import java.util.List;

import domain.Customer;
import wzg.CriteriaCustomer;
import wzg.CustomerDAO;
import wzg.DAO;

public class CustomerDAOJdbcIMpl extends DAO<Customer> implements CustomerDAO{

	@Override
	public List<Customer> getAll() {
		String sql = "select id , name , address , phone from customers";
		return getForList(sql);
	}

	@Override
	public void save(Customer customer) {
		String sql ="insert into customers(name , address , phone) values(?,?,?)";
		update(sql, customer.getName(),customer.getAddress(),customer.getPhone());
		
	}

	@Override
	public Customer get(Integer id) {
		String sql = "select id , name, address , phone from customers where id = ?";
		return get(sql, id);
	}

	@Override
	public void delete(Integer id) {
		String sql = "delete from customers where id = ?";
		update(sql, id);
	}

	@Override
	public long getCountWithName(String name) {
		String sql = "select count(id) from customers where name = ?";
		return getForValue(sql, name);
	}

	@Override
	public List<Customer> getForListWithCriteriaCustomer(CriteriaCustomer cc) {
		//模糊查询,%%代表查询全部，%name或address或phone%，查询包含该字符串的信息,而并非全部 
		String sql = "select id , name , address , phone from customers "
				+ " where name like ? and address like ? and phone like ?";
		return getForList(sql,cc.getName(),cc.getAddress(),cc.getPhone());
	}

	@Override
	public void update(Customer customer) {
		String sql = "update customers set name = ? , address = ? , phone = ? "
				+ " where id = ?";
		update(sql, customer.getName(), customer.getAddress(), customer.getPhone(), customer.getId());
		
	}

}
