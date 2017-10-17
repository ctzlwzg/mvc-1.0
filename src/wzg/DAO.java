package wzg;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import db.JdbcUtils;

/*
 * 封装了基本的CRUD的方法，以供子类继承使用
 * 当前DAO直接在方法中获取数据库连接
 * 整个DAO采取DBUtils解决方案
 * <T>当前DAO处理的实体类的类型是什么
 */
/*
 * 不需要理会T是什么实际意义，使用该类时就可为T类型参传入实际类型
 */
public class DAO<T> {
	
	private QueryRunner queryRunner = new QueryRunner();
	
	//该对象的类型
	private Class<T> clazz;
	
	public DAO(){
		Type superclass = getClass().getGenericSuperclass();
		if(superclass instanceof ParameterizedType){
			ParameterizedType parameterizedType = (ParameterizedType) superclass;
			
			Type [] typeArgs = parameterizedType.getActualTypeArguments();
			if(typeArgs != null && typeArgs.length > 0){
				if(typeArgs[0] instanceof Class){
					clazz = (Class<T>) typeArgs[0];
				}
			}
		}
	}
	
	/*
	 * 返回某一个字段的值，例如返回某一条记录的customerName,
	 * 或返回数据表中有多少条记录等
	 */
	public <E> E getForValue(String sql,Object ...args ){
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			return (E) queryRunner.query(connection, sql, new ScalarHandler(), args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.releaseConnection(connection);
		}
		return null;
	}
	
	/*
	 * 查询多条记录
	 * 返回T所对应的List集合
	 * 
	 */
	public List<T> getForList(String sql,Object ...args ){
		
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			return queryRunner.query(connection, sql, new BeanListHandler<>(clazz), args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.releaseConnection(connection);
		}
		
		return null;
	}
	
	
	/*
	 * 查询一条记录
	 * 返回对应的T的一个实例类的对象
	 */
	public T get(String sql,Object ...args ){
		
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			return queryRunner.query(connection, sql, new BeanHandler<>(clazz), args);
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.releaseConnection(connection);
		}
		
		return null;
	}
	
	/*
	 * 该方法封装了INSERT,DELETE,UPDATE操作
	 * sql:SQL语句
	 * args:填充SQL语句的占位符参数
	 */
	public void update(String sql,Object...args){
		Connection connection = null;
		try {
			connection = JdbcUtils.getConnection();
			queryRunner.update(connection, sql, args);
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			JdbcUtils.releaseConnection(connection);
		}
	}
	
}
