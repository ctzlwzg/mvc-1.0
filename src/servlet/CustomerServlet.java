package servlet;

import impd.CustomerDAOJdbcIMpl;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import domain.Customer;
import wzg.CriteriaCustomer;
import wzg.CustomerDAO;

/**
 * Servlet implementation class CustomerServlet
 */
@WebServlet("*.do")
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private CustomerDAO customerDAO = new CustomerDAOJdbcIMpl();

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// 1.获取ServletPath:/addCustomer.do ,以"/"开头
		String servletPath = request.getServletPath();
		// 2.去除"/"和，".do"，得到addCustomer字符串
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0, methodName.length() - 3);
		try {
			// 3.利用反射获取methodName对应的方法
			Method method = getClass().getDeclaredMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
			// 4.利用反射调用对应的方法
			method.invoke(this, request, response);
		} catch (Exception e) {
			// e.printStackTrace();
			response.sendRedirect("error.jsp");
		}

	}

	private void addCustomer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.获取表单参数
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		// 2.检查name是否被占用
		// 2.1调用CustomerDAO的getCountWithName(String name)获取name在数据库中是否存在
		long countWithName = customerDAO.getCountWithName(name);
		// 2.2若返回值大于0，则响应newcustomer.jsp页面
		// 2.2.1要求在newcustomer.jsp页面显示一个错误信息
		// 在request中放入一个属性message:用户名name
		// 已经被占用，请重新选择！在页面上通过request.getAttribute("message")的方法显示；

		if (countWithName > 0) {
			request.setAttribute("message", "用户名" + name + "已经被占用");
			// 通过转发的方式来响应newcustomer.jsp
			// 2.2.2newcustomer.jsp的表单值可以回显
			request.getRequestDispatcher("/newCustomer.jsp").forward(request,
					response);
			// 2.2.3结束方法：return
			return;
		}

		// 3.若验证通过，把表单参数封裝为一个Customer对象customer
		Customer customer = new Customer(name, address, phone);
		// 4.调用CustomerDAO的save(Customer customer) 执行保存操作
		customerDAO.save(customer);
		// 5.重定向到success.jsp页面,此时使用重定向即可
		// 使用重定向可以防止表单的重复提交
		// request.getRequestDispatcher("/success.jsp").forward(request,
		// response);
		response.sendRedirect("success.jsp");
	}

	private void query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// 获取传入的属性值
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);

		// 1.调用CustomerDao的getForListWithCriteriaCustomer(cc)方法得到Customer的集合
		List<Customer> customers = customerDAO
				.getForListWithCriteriaCustomer(cc);

		// 2.把Customer的集合放入到request中
		request.setAttribute("customers", customers);

		// 3.转发页面到index.jsp
		request.getRequestDispatcher("/index.jsp").forward(request, response);
	}

	private void delete(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String idStr = request.getParameter("id");
		int id = 0;
		try {
			id = Integer.parseInt(idStr);
			customerDAO.delete(id);
		} catch (Exception e) {
		}

		response.sendRedirect("query.do");
	}

	@SuppressWarnings("unused")
	private void edit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		
		String forwardParth = "/error.jsp";
		// 1.获取请求参数
		String id = request.getParameter("id");
		
		// 2.调用CustomerDAO的customerDAO.get(id)获取和id对应的Customer对象
		try {
			Customer customer = customerDAO.get(Integer.parseInt(id));
			System.out.println(customer);
			if(customer != null){
				forwardParth = "/updatecustomer.jsp";
				// 3.将customer放入request中
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		// 4.响应updateCustomer.jsp页面
		System.out.println(forwardParth);
		request.getRequestDispatcher(forwardParth).forward(request, response);

	}

	private void update(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		System.out.println("update");
		// 1.获取表单参数
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String oldName = request.getParameter("oldName");
		System.out.println("name="+name);
		System.out.println("oldName="+oldName);
		System.out.println("phone="+phone);
		System.out.println("address="+address);
		// 2.检查name是否被占用

		// 2.1比较name和oldName是否相同，若相同说明name可用,不区分大小写
		if(!oldName.equalsIgnoreCase(name)){
			System.out.println(name);
			long count = customerDAO.getCountWithName(name);
			if(count > 0){
				
				request.setAttribute("message", "用户名" + name + "已经被占用");
				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				return;
			}
		}
		// 2.2不相同，调用CustomerDAO的getCountWithName(String name)获取name在数据库中是否存在
		// 2.2若返回值大于0，则响应updatecustomer.jsp页面 ,
		// 2.2.1要求在updatecustomer.jsp页面显示一个错误信息
		// 在request中放入一个属性message:用户名name
		// 已经被占用，请重新选择！在页面上通过request.getAttribute("message")的方法显示；
		// 通过转发的方式来响应newcustomer.jsp
		// 2.2.2newcustomer.jsp的表单值可以回显
		// address,phone 显示提交表单的新的值，而name显示oldName，而不是新提交的值
		// 2.2.3结束方法：return
		// 3.若验证通过，把表单参数封裝为一个Customer对象customer
		Customer customer = new Customer(name, address, phone);
		customer.setId(Integer.parseInt(id));
		
		System.out.println(customer);
		// 4.调用CustomerDAO的update(Customer customer) 执行更新操作
		customerDAO.update(customer);
		
		response.sendRedirect("query.do");
		
		// 5.重定向到// 3.若验证通过，把表单参数封裝为一个Customer对象customer
		// 4.调用CustomerDAO的save(Customer customer) 执行保存操作
		// 5.重定向到success.jsp页面,此时使用重定向即可
		// 使用重定向可以防止表单的重复提交
		// request.getRequestDispatcher("/success.jsp").forward(request,
		// response);页面,此时使用重定向即可
		// 使用重定向到query.do
		

	}

}
