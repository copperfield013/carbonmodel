package cho.carbon.imodel.model.modelitem.service;

import java.util.List;

import cho.carbon.imodel.model.modelitem.pojo.BasicChange;

public interface BasicChangeService {

	/**
	 * 根据条件对象查询分页
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List queryList();

	/**
	 * 创建一个DictionaryMappingAlias对象
	 * @param DictionaryMappingAlias
	 */
	void create(BasicChange basicChange);

	/**
	 * 更新一个DictionaryMappingAlias对象
	 * @param demo
	 */
	void update(BasicChange basicChange);

	/**
	 * 从数据库中删除一个DictionaryMappingAlias对象
	 * @param id
	 */
	void delete(String code);
	
	BasicChange getOne(String code);
}
