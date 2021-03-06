package cho.carbon.imodel.model.modelitem.dao.impl;

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
import cho.carbon.imodel.model.modelitem.dao.ModelItemDao;
import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.imodel.model.neo4j.domain.Item;
import cho.carbon.meta.enun.ModelItemType;

@Repository
public class ModelItemDaoImpl implements ModelItemDao {
	@Resource
	SessionFactory sFactory;

	@Override
	public List<ModelItem> queryList(ModelItem modelItem, PageInfo pageInfo) throws Exception {
		
		String hql = "from ModelItem b WHERE type in(1, 101, 102, 103)";
		DeferedParamQuery dQuery = new DeferedParamQuery(hql);
		if(TextUtils.hasText(modelItem.getCode())){
			dQuery.appendCondition(" AND b.code =:code")
					.setParam("code", modelItem.getCode());
		}
		
		if(TextUtils.hasText(modelItem.getName())){
			dQuery.appendCondition(" AND b.name like :name")
					.setParam("name", "%" + modelItem.getName() + "%");
		}
		
		Query countQuery = dQuery.createQuery(sFactory.getCurrentSession(), false, new WrapForCountFunction());
		Integer count = FormatUtils.toInteger(countQuery.uniqueResult());
		pageInfo.setCount(count);
		if(count > 0){
			Query query = dQuery.createQuery(sFactory.getCurrentSession(), false, null);
			QueryUtils.setPagingParamWithCriteria(query , pageInfo);
			return query.list();
		}
		return new ArrayList<ModelItem>();
	}

	@Override
	public ModelItemContainer saveOrUpdate(ModelItemContainer modelItemContainer) throws Exception {
		
		return null;
	}

	@Override
	public List<ModelItem> getModelItemStairChild(String pmiCode) {
		String hql = "from ModelItem b WHERE parent=:pmiCode AND type<>'4'";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("pmiCode", pmiCode).list();
	}

	@Override
	public List<ModelItem> getModelItemByType(String parentCode, ModelItemType[] existMiTypes, ModelItemType[] noNiTypes,Integer miUsingState) throws Exception {
		StringBuffer sb = new StringBuffer();
		sb.append(" select a.* ")
		.append(" from  (")
		.append(" SELECT b.* from t_cc_model_item b WHERE 1=1 ");
		
		if (noNiTypes != null) {
			String str = "";
			for (ModelItemType modelItemType : noNiTypes) {
				int index = modelItemType.getIndex();
				str+=index + ",";
			}
			str = str.substring(0,str.length()-1);
			
			sb.append(" AND b.type NOT in ("+str+") ) a WHERE 1=1 ");
		} else {
			sb.append(" ) a   WHERE 1=1 ");
		}
		if (existMiTypes != null) {
			String str = "";
			for (ModelItemType modelItemType : existMiTypes) {
				int index = modelItemType.getIndex();
				str+=index + ",";
			}
			str = str.substring(0,str.length()-1);
			sb.append("  AND a.type in ("+str+") ");
		} 
		
		if (parentCode != null) {
			sb.append(" AND a.parent=:parentCode");
		}
		if (miUsingState != null) {
			sb.append(" AND a.using_state=:miUsingState");
		}
		Query createQuery = sFactory.getCurrentSession().createSQLQuery(sb.toString()).addEntity(ModelItem.class);
		
		if (parentCode != null) {
			createQuery.setParameter("parentCode", parentCode);
		}
		
		if (miUsingState != null) {
			createQuery.setParameter("miUsingState", miUsingState);
		}
		return createQuery.list();
	}

	@Override
	public List<ModelItem> getModelItemByBelongMode(String belongMode, ModelItemType[] pTypes, ModelItemType[] chilTypes, boolean needCorrelation) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.* from t_cc_model_item a ")
		.append(" left join t_cc_model_item b on a.parent = b.code")
		.append(" where a.belong_model =:belongMode ");
		
		if (pTypes == null || pTypes.length == 0) {
			sb.append(" AND b.type =" + ModelItemType.ONE_LINE_GROUP.getIndex());
		} else {
			String str = "";
			for (ModelItemType modelItemType : pTypes) {
				int index = modelItemType.getIndex();
				str+=index + ",";
			}
			//str = str.substring(0,str.length()-1);
			str = str+ModelItemType.MODEL.getIndex();
			sb.append(" AND b.type in ("+str+") ");
		}
		
		if (chilTypes != null && chilTypes.length != 0) {
			String str = "";
			for (ModelItemType modelItemType : chilTypes) {
				int index = modelItemType.getIndex();
				str+=index + ",";
			}
			str = str.substring(0,str.length()-1);
			sb.append(" AND a.type in ("+str+") ");
		} else {
			sb.append(" AND a.type not in (5, 6) ");
		}
		
		
		
		if (!needCorrelation) {
			sb.append(" AND (a.code not like '%\\_%' or a.code like '%_cnt')");
		}
		
		return sFactory.getCurrentSession().createSQLQuery(sb.toString())
				.addEntity(ModelItem.class)
				.setParameter("belongMode", belongMode)
				.list();
	}

	@Override
	public List<MiCascade> getMiCascadeList(String miCasEnumCode) {
		String hql = "from MiCascade b WHERE  code=:miCasEnumCode ORDER BY level desc ";
		return sFactory.getCurrentSession().createQuery(hql)
				.setParameter("miCasEnumCode", miCasEnumCode)
				.list();
	}

	@Override
	public List<MiTwolevelMapping> getMiTwoMapping(String moreLineCode) {
		String hql = "from MiTwolevelMapping b WHERE  mlgroupCode=:moreLineCode";
		return sFactory.getCurrentSession().createQuery(hql)
				.setParameter("moreLineCode", moreLineCode)
				.list();
	}

	@Override
	public List getTwoAttrByMappingId(Integer mappingId) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT a.*, b.name, b.type, b.parent FROM `t_cc_mi_twolevel` a")
		.append(" join t_cc_model_item b")
		.append(" on a.code=b.code")
		.append(" WHERE mapping_id =:mappingId");
		return sFactory.getCurrentSession().createSQLQuery(sb.toString())
				.setInteger("mappingId", mappingId).list();
	}

	@Override
	public List<ModelItem> getModelList() {
		String hql = "from ModelItem b WHERE type in (1, 101, 102, 103)";
		return sFactory.getCurrentSession().createQuery(hql).list();
	}

	@Override
	public List<Item> getAllItem() {
		StringBuffer sb = new StringBuffer();
		sb.append("SELECT a.name, a.type,b.code, b.need_history needHistory, b.need_cache needCache, b.need_running_logger needRunningLogger ")
		.append(" FROM t_cc_model_item a")
		.append(" left join t_cc_mi_model b on a.code=b.code")
		.append(" WHERE type IN(1, 101, 102, 103)");
		 List list = sFactory.getCurrentSession().createSQLQuery(sb.toString()).list();
		 List<Item> itemList = new ArrayList<Item>();
		 
		 for (Object obj: list) {
			 Object[] objStr = (Object[]) obj;
			 Item item = new Item(objStr[2]+"", objStr[0]+"", objStr[1]+"", objStr[3]+"", objStr[4]+"", objStr[5]+"");
			itemList.add(item);
		}
		 
		return itemList;
	}

}
