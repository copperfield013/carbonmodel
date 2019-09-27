package cho.carbon.imodel.model.cascadedict.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.model.cascadedict.criteria.DictionaryMappingCriteria;
import cho.carbon.imodel.model.cascadedict.dao.DictionaryMappingDao;
import cho.carbon.imodel.model.cascadedict.pojo.DictionaryMapping;
import cho.carbon.imodel.model.cascadedict.service.DictionaryMappingService;

@Service
public class DictionaryMappingServiceImpl implements DictionaryMappingService {

	@Resource
	DictionaryMappingDao dictMappingDao;
	
	@Override
	public List<DictionaryMapping> queryList(DictionaryMappingCriteria criteria, PageInfo pageInfo) {
		return dictMappingDao.queryList(criteria, pageInfo);
	}

	@Override
	public void create(DictionaryMapping dictParentItem) {
		dictMappingDao.insert(dictParentItem);
	}

	@Override
	public DictionaryMapping getOne(Integer id) {
		return dictMappingDao.get(DictionaryMapping.class, id);
	}

	@Override
	public void update(DictionaryMapping dictParentItem) {
		dictMappingDao.update(dictParentItem);
	}

	@Override
	public void delete(Integer id) {
		DictionaryMapping dictionaryMapping = dictMappingDao.get(DictionaryMapping.class, id);
		dictMappingDao.delete(dictionaryMapping);
	}

	@Override
	public List<DictionaryMapping> queryListAll() {
		return dictMappingDao.queryListAll();
	}

}
