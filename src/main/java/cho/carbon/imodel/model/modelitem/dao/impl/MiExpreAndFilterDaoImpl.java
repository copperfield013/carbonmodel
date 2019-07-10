package cho.carbon.imodel.model.modelitem.dao.impl;

import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import cho.carbon.imodel.model.modelitem.dao.MiExpreAndFilterDao;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;

@Repository
public class MiExpreAndFilterDaoImpl implements MiExpreAndFilterDao {
	
	@Resource
	SessionFactory sFactory;
	
	@Override
	public List<MiFilterCriterion> getMiFilterCriterionByPid(Integer groupId) {
		
		String hql = "from MiFilterCriterion b WHERE groupId=:groupId";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("groupId", groupId).list();
	}

	@Override
	public List<MiFilterGroup> getMiFilterGroupByPid(Integer groupId) {
		
		String hql = "from MiFilterGroup b WHERE pid=:groupId";
		return sFactory.getCurrentSession().createQuery(hql).setParameter("groupId", groupId).list();
	}
}
