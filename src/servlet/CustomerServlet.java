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

		// 1.��ȡServletPath:/addCustomer.do ,��"/"��ͷ
		String servletPath = request.getServletPath();
		// 2.ȥ��"/"�ͣ�".do"���õ�addCustomer�ַ���
		String methodName = servletPath.substring(1);
		methodName = methodName.substring(0, methodName.length() - 3);
		try {
			// 3.���÷����ȡmethodName��Ӧ�ķ���
			Method method = getClass().getDeclaredMethod(methodName,
					HttpServletRequest.class, HttpServletResponse.class);
			// 4.���÷�����ö�Ӧ�ķ���
			method.invoke(this, request, response);
		} catch (Exception e) {
			// e.printStackTrace();
			response.sendRedirect("error.jsp");
		}

	}

	private void addCustomer(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 1.��ȡ������
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		// 2.���name�Ƿ�ռ��
		// 2.1����CustomerDAO��getCountWithName(String name)��ȡname�����ݿ����Ƿ����
		long countWithName = customerDAO.getCountWithName(name);
		// 2.2������ֵ����0������Ӧnewcustomer.jspҳ��
		// 2.2.1Ҫ����newcustomer.jspҳ����ʾһ��������Ϣ
		// ��request�з���һ������message:�û���name
		// �Ѿ���ռ�ã�������ѡ����ҳ����ͨ��request.getAttribute("message")�ķ�����ʾ��

		if (countWithName > 0) {
			request.setAttribute("message", "�û���" + name + "�Ѿ���ռ��");
			// ͨ��ת���ķ�ʽ����Ӧnewcustomer.jsp
			// 2.2.2newcustomer.jsp�ı�ֵ���Ի���
			request.getRequestDispatcher("/newCustomer.jsp").forward(request,
					response);
			// 2.2.3����������return
			return;
		}

		// 3.����֤ͨ�����ѱ��������bΪһ��Customer����customer
		Customer customer = new Customer(name, address, phone);
		// 4.����CustomerDAO��save(Customer customer) ִ�б������
		customerDAO.save(customer);
		// 5.�ض���success.jspҳ��,��ʱʹ���ض��򼴿�
		// ʹ���ض�����Է�ֹ�����ظ��ύ
		// request.getRequestDispatcher("/success.jsp").forward(request,
		// response);
		response.sendRedirect("success.jsp");
	}

	private void query(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ��ȡ���������ֵ
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");

		CriteriaCustomer cc = new CriteriaCustomer(name, address, phone);

		// 1.����CustomerDao��getForListWithCriteriaCustomer(cc)�����õ�Customer�ļ���
		List<Customer> customers = customerDAO
				.getForListWithCriteriaCustomer(cc);

		// 2.��Customer�ļ��Ϸ��뵽request��
		request.setAttribute("customers", customers);

		// 3.ת��ҳ�浽index.jsp
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
		// 1.��ȡ�������
		String id = request.getParameter("id");
		
		// 2.����CustomerDAO��customerDAO.get(id)��ȡ��id��Ӧ��Customer����
		try {
			Customer customer = customerDAO.get(Integer.parseInt(id));
			System.out.println(customer);
			if(customer != null){
				forwardParth = "/updatecustomer.jsp";
				// 3.��customer����request��
				request.setAttribute("customer", customer);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		// 4.��ӦupdateCustomer.jspҳ��
		System.out.println(forwardParth);
		request.getRequestDispatcher(forwardParth).forward(request, response);

	}

	private void update(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		System.out.println("update");
		// 1.��ȡ������
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String address = request.getParameter("address");
		String oldName = request.getParameter("oldName");
		System.out.println("name="+name);
		System.out.println("oldName="+oldName);
		System.out.println("phone="+phone);
		System.out.println("address="+address);
		// 2.���name�Ƿ�ռ��

		// 2.1�Ƚ�name��oldName�Ƿ���ͬ������ͬ˵��name����,�����ִ�Сд
		if(!oldName.equalsIgnoreCase(name)){
			System.out.println(name);
			long count = customerDAO.getCountWithName(name);
			if(count > 0){
				
				request.setAttribute("message", "�û���" + name + "�Ѿ���ռ��");
				request.getRequestDispatcher("/updatecustomer.jsp").forward(request, response);
				return;
			}
		}
		// 2.2����ͬ������CustomerDAO��getCountWithName(String name)��ȡname�����ݿ����Ƿ����
		// 2.2������ֵ����0������Ӧupdatecustomer.jspҳ�� ,
		// 2.2.1Ҫ����updatecustomer.jspҳ����ʾһ��������Ϣ
		// ��request�з���һ������message:�û���name
		// �Ѿ���ռ�ã�������ѡ����ҳ����ͨ��request.getAttribute("message")�ķ�����ʾ��
		// ͨ��ת���ķ�ʽ����Ӧnewcustomer.jsp
		// 2.2.2newcustomer.jsp�ı�ֵ���Ի���
		// address,phone ��ʾ�ύ�����µ�ֵ����name��ʾoldName�����������ύ��ֵ
		// 2.2.3����������return
		// 3.����֤ͨ�����ѱ��������bΪһ��Customer����customer
		Customer customer = new Customer(name, address, phone);
		customer.setId(Integer.parseInt(id));
		
		System.out.println(customer);
		// 4.����CustomerDAO��update(Customer customer) ִ�и��²���
		customerDAO.update(customer);
		
		response.sendRedirect("query.do");
		
		// 5.�ض���// 3.����֤ͨ�����ѱ��������bΪһ��Customer����customer
		// 4.����CustomerDAO��save(Customer customer) ִ�б������
		// 5.�ض���success.jspҳ��,��ʱʹ���ض��򼴿�
		// ʹ���ض�����Է�ֹ�����ظ��ύ
		// request.getRequestDispatcher("/success.jsp").forward(request,
		// response);ҳ��,��ʱʹ���ض��򼴿�
		// ʹ���ض���query.do
		

	}

}
