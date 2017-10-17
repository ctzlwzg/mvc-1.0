package db;

import java.sql.Connection;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/*
 * JDBC操作的工具类
 */
public class JdbcUtils {
	
	/*
	 * 释放Connection连接,不需要关闭PrepareStatement和ResultSet
	 */
	public static void releaseConnection(Connection connection){
		try {
			if(connection != null){
				connection.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static DataSource dataSource = null;
	
	static{
		//数据源只能被创建一次
		dataSource = new ComboPooledDataSource("mvcapp");
	}
	
	/*
	 * 返回数据源的一个Connection对象
	 */
	public static Connection getConnection() throws Exception{
		return dataSource.getConnection();
	}
	
}
