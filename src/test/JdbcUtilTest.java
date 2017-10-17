package test;

import java.sql.Connection;

import org.junit.Test;

import db.JdbcUtils;

public class JdbcUtilTest {

	@Test
	public void testGetConnection() throws Exception {
		Connection connection = JdbcUtils.getConnection();
		System.out.println(connection);
	}

}
