package cho.carbon.imodel.model.cascadedict.dao.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.type.Type;
import org.springframework.stereotype.Repository;

import cho.carbon.hc.copframe.dao.deferedQuery.DeferedParamQuery;
import cho.carbon.hc.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cho.carbon.hc.copframe.dao.utils.QueryUtils;
import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.TextUtils;
import cho.carbon.imodel.model.cascadedict.criteria.CascadedictBasicItemCriteria;
import cho.carbon.imodel.model.cascadedict.dao.CascadedictBasicItemDao;
import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;

@Repository
public class CascadedictBasicItemDaoImpl implements CascadedictBasicItemDao {

	private static final Type Integer = null;
	@Resource
	SessionFactory sFactory;

	@Override
	public List<CascadedictBasicItem> queryList(CascadedictBasicItemCriteria criteria, PageInfo pageInfo) throws Exception{
		String hql = "from CascadedictBasicItem b WHERE 1=1 and b.id != 1";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		
		if(criteria.getId() != null){
			dQuery.appendCondition(" and b.id =:id")
					.setParam("id", criteria.getId());
		}
		
		if(TextUtils.hasText(criteria.getName())){
			dQuery.appendCondition(" and b.name like :name")
					.setParam("name", "%" + criteria.getName() + "%");
		}
		
		dQuery.appendCondition(" ORDER BY updateTime DESC");
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), false, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), false, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<CascadedictBasicItem>();
	}
	
	@Override
	public void insert(Object pojo) {
		sFactory.getCurrentSession().save(pojo);
	}
	
	@Override
	public <T> T get(Class<T> clazz, Integer id) throws Exception{
		return sFactory.getCurrentSession().get(clazz, id);
	}
	
	@Override
	public void update(Object pojo) throws Exception{
		sFactory.getCurrentSession().update(pojo);
	}
	
	@Override
	public void delete(Object pojo) throws Exception{
		sFactory.getCurrentSession().delete(pojo);
	}

	@Override
	public void delete(java.lang.Integer id) throws Exception{
		String sql = "DELETE FROM t_cc_casenum_item where id=:id";
		sFactory.getCurrentSession().createSQLQuery(sql).setParameter("id", id).executeUpdate();
	}

	@Override
	public List<CascadedictBasicItem> getChildByParentId(java.lang.Integer parentId) throws Exception{
		String hql = "from CascadedictBasicItem where parentId=:parentId ORDER BY order";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("parentId", parentId).list();
	}

	@Override
	public List<CascadedictBasicItem> getParentAll() {
		String sql = "SELECT DISTINCT * FROM `t_cc_casenum_item` b "
				+ "WHERE b.id !=1 AND b.id in(SELECT DISTINCT parent_id FROM `t_cc_casenum_item` WHERE parent_id is not null) ";
		return sFactory.getCurrentSession().createSQLQuery(sql).addEntity(CascadedictBasicItem.class).list();
	}

	@Override
	public List<CascadedictBasicItem> getCascaseDictPitem() throws Exception {
		String sql = "SELECT DISTINCT  a.* FROM `t_cc_casenum_item` a "
				+ " inner join t_cc_casenum_item b on a.id=b.parent_id "
				+ " inner join t_cc_casenum_item c on b.id=c.parent_id "
				+ " WHERE a.parent_id is not null AND a.id !=1 ";
		return sFactory.getCurrentSession().createSQLQuery(sql).addEntity(CascadedictBasicItem.class).list();
	}

	@Override
	public Integer getCasCaseDepth(Integer parentId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT	(")
		.append(" SELECT	LENGTH( cas_pid ) - LENGTH( REPLACE ( cas_pid, '.', '' ) ) AS count ")
		.append(" FROM	`t_cc_casenum_item` ")
		.append(" WHERE 	cas_pid LIKE (")
		.append(" SELECT 	concat( cas_pid, '.', id, '%' ) ")
		.append(" FROM	t_cc_casenum_item ")
		.append(" WHERE	id =:parentId 	) ")
		.append(" ORDER BY	LENGTH( cas_pid ) DESC	LIMIT 1 	)");
		BigInteger bi = (BigInteger)sFactory.getCurrentSession().createSQLQuery(sb.toString())
		.setParameter("parentId", parentId).uniqueResult();
		
		return bi.intValue();
	}
	
}
