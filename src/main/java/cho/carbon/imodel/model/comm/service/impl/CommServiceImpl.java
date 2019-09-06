package cho.carbon.imodel.model.comm.service.impl;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;

import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.comm.dao.CommDao;
import cho.carbon.imodel.model.comm.service.CommService;

@Service
public class CommServiceImpl implements CommService{

	@Resource
	CommDao commDao;
	
	@Override
	public void insert(Object pojo) {
		commDao.insert(pojo);
	}
	
	@Override
	public <T> T get(Class<T> clazz, String id) {
		return commDao.get(clazz, id);
	}
	
	@Override
	public <T> T get(Class<T> clazz, Integer id) {
		return commDao.get(clazz, id);
	}
	
	@Override
	public void update(Object pojo) {
		commDao.update(pojo);
	}
	
	@Override
	public void delete(Object pojo) {
		commDao.delete(pojo);
	}

	@Override
	public void excuteBySql(String sql) {
		commDao.excuteBySql(sql);
	}
	
	@Override
	public Object excuteBySqlSelect(String sql) {
		return commDao.excuteBySqlSelect(sql);
	}
	
	/**
	 * 获取当前链接的数据库名字
	 */
	public String getDataBaseName() {
		return commDao.getDataBaseName();
	}

}
