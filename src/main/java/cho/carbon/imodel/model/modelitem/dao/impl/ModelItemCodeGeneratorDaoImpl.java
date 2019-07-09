package cho.carbon.imodel.model.modelitem.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.dao.ModelItemCodeGeneratorDao;

@Repository
public class ModelItemCodeGeneratorDaoImpl implements ModelItemCodeGeneratorDao {

	@Resource
	SessionFactory sFactory;
	
	
	@Override
	public void insert(Object pojo) {
		sFactory.getCurrentSession().save(pojo);
	}
	
	/**
	 * 	从数据库加载获取实体和属性前缀
	 * @return
	 * @throws Exception
	 */
	@Override
	public String getBasicItemFixByDB() throws Exception {
		String sql = "SELECT prefix FROM `t_cc_model_item_fix` WHERE using_state='1' ORDER BY id desc";
		List list = sFactory.getCurrentSession().createSQLQuery(sql).list();
		if (list.isEmpty()) {
			throw new Exception("前缀表【t_cc_model_item_fix】 没有using_state=1的可用数据");
		}
		return (String) list.get(0);
	}
}
