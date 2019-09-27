package cho.carbon.imodel.model.cascadedict.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.model.cascadedict.criteria.DictionaryMappingAliasCriteria;
import cho.carbon.imodel.model.cascadedict.dao.DictionaryMappingAliasDao;
import cho.carbon.imodel.model.cascadedict.pojo.DictionaryMappingAlias;
import cho.carbon.imodel.model.cascadedict.service.DictionaryMappingAliasService;

@Service
public class DictionaryMappingAliasServiceImpl implements DictionaryMappingAliasService {

	@Resource
	DictionaryMappingAliasDao dictMappingAlias;
	
	@Override
	public List queryList(DictionaryMappingAliasCriteria criteria, PageInfo pageInfo) {
		return dictMappingAlias.queryList(criteria, pageInfo);
	}

	@Override
	public void create(DictionaryMappingAlias dictParentItem) {
		dictMappingAlias.insert(dictParentItem);
	}

	@Override
	public DictionaryMappingAlias getOne(Integer id) {
		return dictMappingAlias.get(DictionaryMappingAlias.class, id);
	}

	@Override
	public void update(DictionaryMappingAlias dictParentItem) {
		dictMappingAlias.update(dictParentItem);
	}

	@Override
	public void delete(Integer id) {
		DictionaryMappingAlias dictParentItem = dictMappingAlias.get(DictionaryMappingAlias.class, id);
		dictMappingAlias.delete(dictParentItem);
	}

}
