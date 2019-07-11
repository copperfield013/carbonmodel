package cho.carbon.imodel.model.modelitem.service;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.pojo.ModelRelationType;
import cho.carbon.meta.enun.RelationType;
import cn.sowell.copframe.dto.page.PageInfo;

public interface ModelRelationTypeService {

	/**
	 * 根据条件对象查询分页
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List<ModelRelationType> queryList(ModelRelationType criteria, PageInfo pageInfo);

	/**
	 * 根据实体id， 求出实体所有的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getEntityRelaByBitemId(String recordType);
	
	/**
	 * 根据左实体id， 和关系类型， 求出本实体中特定关系类型的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getRelaByType(String leftRecordType, RelationType relationType);
	
	/**
	 * 创建一个RecordRelationType对象
	 * @param ModelRelationType
	 */
	void create(ModelRelationType basicItem);

	ModelRelationType getRecordRelationType(String id);

	/**
	 * 更新一个RecordRelationType对象
	 * @param demo
	 */
	void update(ModelRelationType basicItem);

	/**
	 * 从数据库中删除一个RecordRelationType对象
	 * @param id
	 */
	void delete(String typeCode);

	/**
	 *保存关系
	 * @param lefRrecordType
	 * @param rightRrecordType
	 */
	void saveRelation(ModelRelationType lefRrecordType, ModelRelationType rightRrecordType, String symmetry) throws Exception;
	
	/**
	 * 根据左实体id， 和右实体id， 求出左右实体共同的关系
	 * @param recordType
	 * @return
	 */
	List<ModelRelationType> getEntityRelaByBitemId(String leftRecordType, String rightRecordType);

	void saveStatus(String typeCode, Integer usingState) throws Exception;
	
	/**
	 * 获取与左MODEL 存在关系的右MODEL
	 * @param leftModelCode
	 * @return
	 */
	public List<ModelItem> getExistRelaRightMi(String leftModelCode);
	
}
