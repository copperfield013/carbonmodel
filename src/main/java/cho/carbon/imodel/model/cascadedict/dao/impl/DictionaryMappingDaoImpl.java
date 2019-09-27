package cho.carbon.imodel.model.cascadedict.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.hc.copframe.dao.deferedQuery.DeferedParamQuery;
import cho.carbon.hc.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cho.carbon.hc.copframe.dao.utils.QueryUtils;
import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.TextUtils;
import cho.carbon.imodel.model.cascadedict.criteria.DictionaryMappingCriteria;
import cho.carbon.imodel.model.cascadedict.dao.DictionaryMappingDao;
import cho.carbon.imodel.model.cascadedict.pojo.DictionaryMapping;

@Repository
public class DictionaryMappingDaoImpl implements DictionaryMappingDao {

	@Resource
	SessionFactory sFactory;

	@Override
	public List<DictionaryMapping> queryList(DictionaryMappingCriteria criteria, PageInfo pageInfo) {
		String hql = "from DictionaryMapping b";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(criteria.getName())){
			dQuery.appendCondition(" and b.name like :name")
					.setParam("name", "%" + criteria.getName() + "%");
		}
		
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), true, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), true, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		
		return new ArrayList<DictionaryMapping>();
	}
	
	
	
	@Override
	public void insert(Object pojo) {
		sFactory.getCurrentSession().save(pojo);
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
	public List<DictionaryMapping> queryListAll() {
		String hql = "from DictionaryMapping";
		return sFactory.getCurrentSession().createQuery(hql).list();
	}

}
