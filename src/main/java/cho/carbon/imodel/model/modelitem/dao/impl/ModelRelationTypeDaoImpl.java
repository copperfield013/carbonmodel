package cho.carbon.imodel.model.modelitem.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.dao.ModelRelationTypeDao;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.meta.enun.RelationType;
import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;

@Repository
public class ModelRelationTypeDaoImpl implements ModelRelationTypeDao {

	@Resource
	SessionFactory sFactory;
	
	@Override
	public List<ModelRelationType> queryList(ModelRelationType criteria, PageInfo pageInfo) {
		String hql = "from ModelRelationType b WHERE leftModelCode=:leftModelCode";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		dQuery.setParam("leftModelCode", criteria.getLeftModelCode());
		
		if(TextUtils.hasText(criteria.getName())){
			dQuery.appendCondition(" and b.name like :name")
					.setParam("name", "%" + criteria.getName() + "%");
		}
		
		if(TextUtils.hasText(criteria.getTypeCode())){
			dQuery.appendCondition(" and b.typeCode =:typeCode")
					.setParam("typeCode", criteria.getTypeCode());
		}
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), false, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), false, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		
		return new ArrayList<ModelRelationType>();
	}
	
	@Override
	public List<ModelRelationType> getEntityRelaByBitemId(String recordType) {
		/*
		 * String sql = "SELECT t1.type_code, t1.name,t3.c_cn_name as left_model_code,"
		 * +
		 * "  t4.c_cn_name as right_record_type, t2.name as reverse_code, t1.using_state, t1.relation_type, t1.giant "
		 * + " FROM t_cc_model_relation_type t1" +
		 * " LEFT JOIN t_cc_model_relation_type t2" + " ON t1.reverse_code=t2.type_code"
		 * + " LEFT JOIN t_sc_basic_item t3" + " ON t1.left_model_code=t3.c_code" +
		 * " LEFT JOIN t_sc_basic_item t4 " + " ON t1.right_record_type=t4.c_code" +
		 * "  WHERE t1.left_model_code=:leftRecordType"; return
		 * sFactory.getCurrentSession().createSQLQuery(sql).addEntity(ModelRelationType.
		 * class).setParameter("leftRecordType", recordType).list();
		 */
		
	return null;
	}
	
	@Override
	public List<ModelRelationType> getEntityRelaByBitemId(String leftModelCode, String rightModelCode) {
		String hql = "FROM ModelRelationType where rightModelCode =:rightModelCode AND leftModelCode=:leftModelCode AND usingState=1";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("leftModelCode", leftModelCode).setParameter("rightModelCode", rightModelCode).list();
	}

	@Override
	public List<ModelRelationType> getRelaByType(String leftModelCode, RelationType relationType) {
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	t1.* FROM ")
		.append(" t_cc_model_relation_type t1")
		.append(" WHERE")
		.append(" t1.left_model_code =:leftModelCode ");

		if (relationType !=null) {
			sb.append(" AND t1.relation_type=:relationType");
		}
		
		SQLQuery sqlQuery = sFactory.getCurrentSession().createSQLQuery(sb.toString());
		sqlQuery.addEntity(ModelRelationType.class);
		sqlQuery.setParameter("leftModelCode", leftModelCode);
		if (relationType !=null) {
			sqlQuery.setParameter("relationType", relationType.getIndex());
		}
				
		return sqlQuery.list();
	}

	@Override
	public List<ModelItem> getExistRelaRightMi(String leftModelCode) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT 	t.* FROM	t_cc_model_item t ")
		.append(" WHERE	t.using_state = '1' 	AND t.type = '1' ")
		.append(" AND t.code IN ")
		.append(" ( SELECT right_model_code FROM t_cc_model_relation_type WHERE left_model_code =:leftModelCode )");
		
		return  sFactory.getCurrentSession().createSQLQuery(sb.toString())
				.addEntity(ModelItem.class)
				.setParameter("leftModelCode", leftModelCode).list();
	}

}
