package cho.carbon.imodel.model.comm.dao.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.comm.dao.CommDao;

@Repository
public class CommDaoImpl implements CommDao{

	//数据库名称
	private static String dataBaseName = null;
	
	@Resource
	SessionFactory sFactory;
	
	@Override
	public void insert(Object pojo) {
		sFactory.getCurrentSession().save(pojo);
	}
	
	@Override
	public <T> T get(Class<T> clazz, String id) {
		return sFactory.getCurrentSession().get(clazz, id);
	}
	
	@Override
	public <T> T get(Class<T> clazz, Integer id) {
		return sFactory.getCurrentSession().get(clazz, id);
	}
	
	@Override
	public void update(Object pojo) {
		sFactory.getCurrentSession().update(pojo);
	}
	
	@Override
	public void delete(Object pojo) {
		sFactory.getCurrentSession().delete(pojo);
	}

	@Override
	public void excuteBySql(String sql) {
		sFactory.getCurrentSession().createSQLQuery(sql).executeUpdate();
	}
	
	/**
	 * 获取当前链接的数据库名字
	 */
	public String getDataBaseName() {
		Session ss = sFactory.getCurrentSession();
		if(dataBaseName == null) {
			ss.doWork(new Work(){
				@Override
				public void execute(Connection connection) throws SQLException {
					String catalog = connection.getCatalog();
					dataBaseName =catalog;
				}
			});
		}
		
		return dataBaseName;
	}
}
