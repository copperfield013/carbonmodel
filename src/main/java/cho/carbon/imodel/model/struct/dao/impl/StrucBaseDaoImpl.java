package cho.carbon.imodel.model.struct.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.hc.copframe.dao.deferedQuery.DeferedParamQuery;
import cho.carbon.hc.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cho.carbon.hc.copframe.dao.utils.QueryUtils;
import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.hc.copframe.utils.FormatUtils;
import cho.carbon.hc.copframe.utils.TextUtils;
import cho.carbon.imodel.model.struct.dao.StrucBaseDao;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;

@Repository
public class StrucBaseDaoImpl implements StrucBaseDao {

	@Resource
	SessionFactory sFactory;
	
	@Override
	public List<StrucBase> queryList(StrucBase strucBase, PageInfo pageInfo) {
		
		String hql = "from StrucBase b WHERE type=1 ";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(strucBase.getTitle())){
			dQuery.appendCondition(" AND b.title =:title")
					.setParam("title", strucBase.getTitle());
		}
		
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), false, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), false, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<StrucBase>();
	}

	@Override
	public List<StrucBase> getStructStairChild(Integer sbPid) {
		String hql = "from StrucBase b WHERE parentId=:sbPid";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("sbPid", sbPid).list();
	}

	@Override
	public List<StrucBase> getAllStruc(String modelCode) {
		String hql = "from StrucBase b WHERE type=1 ";
		
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.* FROM `t_cc_struc_base` a")
		.append(" left join t_cc_struc_micode b on a.id=b.sb_id")
		.append(" WHERE type='1' ");
		
		if (modelCode != null && modelCode !="") {
			sb.append(" AND B.item_code=:modelCode ");
		}
		
		SQLQuery sqlQuery = sFactory.getCurrentSession().createSQLQuery(sb.toString());
		if (modelCode != null && modelCode !="") {
			sqlQuery.setString("modelCode", modelCode);
		}
				
		return sqlQuery.addEntity(StrucBase.class).list();
	}

	@Override
	public List<StrucBase> getGroup1DChild(Integer sbId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT b.* FROM `t_cc_struc_base` b")
		.append(" inner JOIN (")
		.append(" SELECT * FROM `t_cc_struc_base` a")
		.append(" WHERE a.parent_id=:sbId AND a.type=101")
		.append(" ) c on b.parent_id=c.id");
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(StrucBase.class).setInteger("sbId", sbId).list();
	}

	@Override
	public List<StrucRelation> getStrucRelationBySbId(Integer sbId) {
		
		if (sbId == null) {
			return new ArrayList<StrucRelation>();
		}
		
		String hql = "from StrucRelation b WHERE sbId=:sbId ";
		return sFactory.getCurrentSession().createQuery(hql).setInteger("sbId", sbId).list();
	}
	
	
}
