package cho.carbon.imodel.model.cascadedict.service;

import java.util.List;

import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.model.cascadedict.criteria.DictionaryMappingAliasCriteria;
import cho.carbon.imodel.model.cascadedict.pojo.DictionaryMappingAlias;

public interface DictionaryMappingAliasService {

	/**
	 * 根据条件对象查询分页
	 * @param criteria
	 * @param pageInfo
	 * @return
	 */
	List queryList(DictionaryMappingAliasCriteria criteria, PageInfo pageInfo);

	/**
	 * 创建一个DictionaryMappingAlias对象
	 * @param DictionaryMappingAlias
	 */
	void create(DictionaryMappingAlias basicItem);

	DictionaryMappingAlias getOne(Integer id);

	/**
	 * 更新一个DictionaryMappingAlias对象
	 * @param demo
	 */
	void update(DictionaryMappingAlias basicItem);

	/**
	 * 从数据库中删除一个DictionaryMappingAlias对象
	 * @param id
	 */
	void delete(Integer id);
}
