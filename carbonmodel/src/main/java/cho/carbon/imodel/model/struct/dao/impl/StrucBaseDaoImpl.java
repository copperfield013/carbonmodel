package cho.carbon.imodel.model.struct.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.struct.dao.StrucBaseDao;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cn.sowell.copframe.dao.deferedQuery.DeferedParamQuery;
import cn.sowell.copframe.dao.deferedQuery.sqlFunc.WrapForCountFunction;
import cn.sowell.copframe.dao.utils.QueryUtils;
import cn.sowell.copframe.dto.page.PageInfo;
import cn.sowell.copframe.utils.FormatUtils;
import cn.sowell.copframe.utils.TextUtils;

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
	
	
	
}
