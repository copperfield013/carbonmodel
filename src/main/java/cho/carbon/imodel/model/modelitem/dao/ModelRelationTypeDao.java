package cho.carbon.imodel.model.modelitem.dao;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
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
	List<ModelRelationType> getEntityRelaByBitemId(String leftRecordType, String rightRecordType);
	
	
	/**
	 * 根据左实体id， 和关系类型， 求出本实体中特定关系类型的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getRelaByType(String leftRecordType, String relationType);
	
	
}
