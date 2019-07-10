package cho.carbon.imodel.model.modelitem.service;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;

/**
 * 表达式和过滤条件
 * @author so-well
 *
 */
public interface MiExpreAndFilterService {
	/**
	 * 保存表达式
	 * @param codeTxt
	 * @param modelItemCode
	 */
	void saveExpress(String codeTxt, String modelItemCode);
	
	/**
	 * 保存普通过滤条件组
	 * @param miFilterGroup
	 */
	void saveCommFilterGroup(MiFilterGroup miFilterGroup);

	/**
	 * 保存过滤表达式
	 * @param miFilterCriterion
	 */
	void saveMiFilterCriterion(MiFilterCriterion miFilterCriterion);

	/**
	 * 获取分组的孩子， 表达式
	 * @return
	 */
	List<MiFilterCriterion> getMiFilterCriterionByPid(Integer groupId);

	/**
	 * 保存过滤条件
	 * @param miCode
	 * @param  type 0.统计实体   1.事实属性 2. 计算属性 3. 配置文件 结构体 和 关系结构 5. 二维组结构
	 * @param filterId
	 */
	void saveFilter(String miCode, Integer type, Integer filterId);
	/**
	 *  获取分组的子分组
	 * @param groupId
	 * @return
	 */
	List<MiFilterGroup> getMiFilterGroupByPid(Integer groupId);
}
