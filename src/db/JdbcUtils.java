package db;

import java.sql.Connection;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/*
 * JDBC�����Ĺ�����
 */
public class JdbcUtils {
	
	/*
	 * �ͷ�Connection����,����Ҫ�ر�PrepareStatement��ResultSet
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
		//����Դֻ�ܱ�����һ��
		dataSource = new ComboPooledDataSource("mvcapp");
	}
	
	/*
	 * ��������Դ��һ��Connection����
	 */
	public static Connection getConnection() throws Exception{
		return dataSource.getConnection();
	}
	
}
