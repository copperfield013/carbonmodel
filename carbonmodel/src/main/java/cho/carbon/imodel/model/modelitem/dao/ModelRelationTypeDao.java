package cho.carbon.imodel.model.modelitem.dao;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.meta.enun.RelationType;
import cn.sowell.copframe.dto.page.PageInfo;

public interface ModelRelationTypeDao {
	/**
	 * 从数据库中根据条件分页查询列表
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<ModelRelationType> queryList(ModelRelationType criteria, PageInfo pageInfo);

	/**
	 * 根据实体id， 求出实体的关系, 名称变成中文
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getEntityRelaByBitemId(String recordType);
	
	/**
	 * 根据左实体id， 和右实体id， 求出左右实体共同的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getEntityRelaByBitemId(String leftModelCode, String rightModelCode);
	
	
	/**
	 * 根据左实体id， 和关系类型， 求出本实体中特定关系类型的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getRelaByType(String leftModelCode, RelationType relationType);
	
	/**
	 * 获取 与 左Model存在关系的右Model
	 * @param leftModelCode  左模型code
	 * @return
	 */
	public List<ModelItem> getExRelaRightMi(String leftModelCode);
}
