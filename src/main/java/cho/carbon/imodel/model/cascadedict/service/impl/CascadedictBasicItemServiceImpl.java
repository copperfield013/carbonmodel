package cho.carbon.imodel.model.cascadedict.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.cascadedict.criteria.CascadedictBasicItemCriteria;
import cho.carbon.imodel.model.cascadedict.dao.CascadedictBasicItemDao;
import cho.carbon.imodel.model.cascadedict.pojo.CascadedictBasicItem;
import cho.carbon.imodel.model.cascadedict.pojo.CascadedictCodeGenerator;
import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.modelitem.pojo.BasicChange;
import cho.carbon.imodel.model.modelitem.service.BasicChangeService;
import cn.sowell.copframe.dto.page.PageInfo;

@Service
public class CascadedictBasicItemServiceImpl implements CascadedictBasicItemService {

	@Resource
	CascadedictBasicItemDao dictionaryBasicItemDao;
	
	@Resource
	BasicChangeService basicChangeService;
	
	@Override
	public List<CascadedictBasicItem> queryList(CascadedictBasicItemCriteria criteria, PageInfo pageInfo) throws Exception{
		return dictionaryBasicItemDao.queryList(criteria, pageInfo);
	}

	@Override
	public void create(CascadedictBasicItem dictParentItem) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		dictParentItem.setUpdateTime(df.format(new Date()));
		
		CascadedictBasicItem one = dictionaryBasicItemDao.get(CascadedictBasicItem.class, dictParentItem.getParentId());
		dictParentItem.setCasPid(one.getId() ==0? one.getId() +"":one.getCasPid()+"."+one.getId());
		
		CascadedictCodeGenerator cc = new CascadedictCodeGenerator();
		dictionaryBasicItemDao.insert(cc);
		dictParentItem.setId(cc.getId());
		dictionaryBasicItemDao.insert(dictParentItem);
		
		  BasicChange basicChange = new BasicChange(BasicChange.CASCADEDICT);
		  basicChangeService.create(basicChange);
		 
	}

	@Override
	public CascadedictBasicItem getOne(Integer id) throws Exception{
		return dictionaryBasicItemDao.get(CascadedictBasicItem.class, id);
	}

	@Override
	public void update(CascadedictBasicItem dictParentItem) throws Exception{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		dictParentItem.setUpdateTime(df.format(new Date()));
		dictionaryBasicItemDao.update(dictParentItem);
		
		BasicChange basicChange = new BasicChange(BasicChange.CASCADEDICT);
		basicChangeService.create(basicChange);
	}

	@Override
	public void delete(Integer id) throws Exception{
		dictionaryBasicItemDao.delete(id);
	}
	
	@Override
	public void delete(CascadedictBasicItem creteria) throws Exception{
		dictionaryBasicItemDao.delete(creteria);
	}

	@Override
	public List<CascadedictBasicItem> getChildByParentId(Integer parentId) throws Exception{
		return dictionaryBasicItemDao.getChildByParentId(parentId);
	}

	@Override
	public void saveOrUpdate(CascadedictBasicItem basicItem) throws Exception {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		basicItem.setUpdateTime(df.format(new Date()));
		CascadedictBasicItem one = dictionaryBasicItemDao.get(CascadedictBasicItem.class, basicItem.getParentId());
		basicItem.setCasPid(one.getCasPid()+"."+one.getId());
		if (basicItem.getId() == null || "".equals(String.valueOf(basicItem.getId()))) {
			CascadedictCodeGenerator cc = new CascadedictCodeGenerator();
			dictionaryBasicItemDao.insert(cc);
			basicItem.setId(cc.getId());
			dictionaryBasicItemDao.insert(basicItem);
		} else {
			dictionaryBasicItemDao.update(basicItem);
		}
		
		BasicChange basicChange = new BasicChange(BasicChange.CASCADEDICT);
		basicChangeService.create(basicChange);
	}

	@Override
	public List<CascadedictBasicItem> getParentAll() throws Exception{
		return dictionaryBasicItemDao.getParentAll();
	}

	@Override
	public List<CascadedictBasicItem> getCascaseDictPitem() throws Exception{
		return dictionaryBasicItemDao.getCascaseDictPitem();
	}

	@Override
	public Integer getCasCaseDepth(Integer parentId) {
		return dictionaryBasicItemDao.getCasCaseDepth(parentId);
	}

}
