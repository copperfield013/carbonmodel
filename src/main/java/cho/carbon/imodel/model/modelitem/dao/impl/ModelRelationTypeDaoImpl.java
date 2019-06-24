package cho.carbon.imodel.model.modelitem.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.dao.ModelRelationTypeDao;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
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
		String sql = "SELECT t1.type_code, t1.name,t3.c_cn_name as left_record_type,"
				+ "  t4.c_cn_name as right_record_type, t2.name as reverse_code, t1.using_state, t1.relation_type, t1.giant "
				+ " FROM t_sc_record_relation_type t1"
				+ " LEFT JOIN t_sc_record_relation_type t2"
				+ " ON t1.reverse_code=t2.type_code"
				+ " LEFT JOIN t_sc_basic_item t3"
				+ " ON t1.left_record_type=t3.c_code"
				+ " LEFT JOIN t_sc_basic_item t4 "
				+ " ON t1.right_record_type=t4.c_code"
				+ "  WHERE t1.left_record_type=:leftRecordType";
		return sFactory.getCurrentSession().createSQLQuery(sql).addEntity(ModelRelationType.class).setParameter("leftRecordType", recordType).list();
	}
	
	@Override
	public List<ModelRelationType> getEntityRelaByBitemId(String leftRecordType, String rightRecordType) {
		String hql = "FROM RecordRelationType where rightRecordType =:rightRecordType AND leftRecordType=:leftRecordType AND usingState=1";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("leftRecordType", leftRecordType).setParameter("rightRecordType", rightRecordType).list();
	}

	@Override
	public List<ModelRelationType> getRelaByType(String leftRecordType, String relationType) {
		StringBuffer sb = new StringBuffer();		
		sb.append(" SELECT 	t1.* FROM ")
		.append(" t_sc_record_relation_type t1")
		.append(" WHERE")
		.append(" t1.left_record_type =:leftRecordType ");

		if (!relationType.isEmpty() || !"".equals(relationType)) {
			sb.append(" AND t1.relation_type=:relationType");
		}
		
		SQLQuery sqlQuery = sFactory.getCurrentSession().createSQLQuery(sb.toString());
		sqlQuery.addEntity(ModelRelationType.class);
		sqlQuery.setParameter("leftRecordType", leftRecordType);
		if (!relationType.isEmpty() || !"".equals(relationType)) {
			sqlQuery.setParameter("relationType", relationType);
		}
				
		return sqlQuery.list();
	}

}
