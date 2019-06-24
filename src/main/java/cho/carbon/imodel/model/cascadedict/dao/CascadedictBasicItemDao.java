package cho.carbon.imodel.model.cascadedict.dao;

import java.util.List;

import cho.carbon.imodel.model.cascadedict.criteria.CascadedictBasicItemCriteria;
import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;
import cn.sowell.copframe.dto.page.PageInfo;

public interface CascadedictBasicItemDao {
	/**
	 * 从数据库中根据条件分页查询列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<CascadedictBasicItem> queryList(CascadedictBasicItemCriteria criteria, PageInfo pageInfo) throws Exception;

	/**
	 * 对象插入到数据表中
	 * @param demo
	 */
	void insert(Object obj) throws Exception;

	/**
	 * 从数据库中查找对应的pojo对象
	 * @param clazz
	 * @param id
	 * @return
	 */
	<T> T get(Class<T> clazz, Integer id) throws Exception;

	/**
	 * 更新一个pojo对象
	 * @param demo
	 */
	void update(Object obj) throws Exception;

	/**
	 * 从数据库中删除一个对象
	 * @param pojo
	 */
	void delete(Object pojo) throws Exception;
	
	/**
	 * 从数据库中删除一个对象
	 * @param pojo
	 */
	void delete(Integer id) throws Exception;

	/**
	 * 根据ParentId查询子数据
	 * @param parentId
	 * @return
	 */
	List<CascadedictBasicItem> getChildByParentId(Integer parentId) throws Exception;
	
	/**
	 * 获取所有父亲， 不管是一级父亲还是二级父亲,只要你这个父亲下面有孩子就获取
	 * @return
	 */
	List <CascadedictBasicItem> getParentAll() throws Exception;

	/**
	 * 获取级联属性的枚举值
	 * @return
	 */
	List<CascadedictBasicItem> getCascaseDictPitem() throws Exception;
	/**
	 * 	获取枚举节点的深度
	 * @param parentId 父节点
	 * @return
	 */
	Integer getCasCaseDepth(Integer parentId);
}
