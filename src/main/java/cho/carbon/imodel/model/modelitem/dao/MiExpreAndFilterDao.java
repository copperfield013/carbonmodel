package cho.carbon.imodel.model.modelitem.dao;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;

public interface MiExpreAndFilterDao {
	/**
	 * 获取分组的孩子， 表达式
	 * @return
	 */
	public List<MiFilterCriterion> getMiFilterCriterionByPid(Integer groupId);

	/**
	 * 
	 * @param groupId
	 * @return
	 */
	public List<MiFilterGroup> getMiFilterGroupByPid(Integer groupId);
}
