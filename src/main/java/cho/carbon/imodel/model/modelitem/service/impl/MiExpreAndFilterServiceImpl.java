package cho.carbon.imodel.model.modelitem.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.dao.MiExpreAndFilterDao;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiCalculated;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterRgroup;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFactGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiStatDimension;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.MiExpreAndFilterService;
import cho.carbon.imodel.model.modelitem.vo.MiFilterContainer;
import cho.carbon.imodel.model.struct.pojo.StrucFilter;
import cho.carbon.meta.enun.ModelItemType;

@Service
public class MiExpreAndFilterServiceImpl implements MiExpreAndFilterService {

	@Resource
	CommService commService;
	
	@Resource
	MiExpreAndFilterDao miExpreAndFilterDao;
	
	@Override
	public void saveExpress(String codeTxt, String modelItemCode) {
		ModelItem modelItem = commService.get(ModelItem.class, modelItemCode);
		ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
		
		switch (itemType) {
		case DIMENSION_ITEM:
		case DIMENSION_ENUM_ITEM:
			saveDimensionExpress(codeTxt, modelItemCode);
			break;
		case FACT_ITEM:
			//事实属性
			saveFactExpress(codeTxt, modelItemCode);
			break;
		case CALCULATED_ITEM:
			//计算属性
			saveCalculatedExpress(codeTxt, modelItemCode);
			break;
		}
		
	}
	
	/**
	 * 保存事实表达式
	 * @param codeTxt
	 * @param modelItemCode
	 */
	private void saveCalculatedExpress(String codeTxt, String modelItemCode) {
			MiCalculated miCalculated = commService.get(MiCalculated.class, modelItemCode);
		
		if (miCalculated == null) {
			//构建表达式
			MiCalExpress miCalExpress = new MiCalExpress();
			miCalExpress.setCodeTxt(codeTxt);
			commService.insert(miCalExpress);
			
			//构建事实
			miCalculated = new MiCalculated();
			miCalculated.setCode(modelItemCode);
			miCalculated.setExpressId(miCalExpress.getId());
			
			commService.insert(miCalculated);
		} else {
			Integer expressId = miCalculated.getExpressId();
			
			//构建表达式， 
			MiCalExpress miCalExpress = commService.get(MiCalExpress.class, expressId);
			miCalExpress.setCodeTxt(codeTxt);
			commService.update(miCalExpress);
		}
		
	}

	/**
	 * 保存事实表达式
	 * @param codeTxt
	 * @param modelItemCode
	 */
	private void saveFactExpress(String codeTxt, String modelItemCode) {
		MiStatFact miStatFact = commService.get(MiStatFact.class, modelItemCode);
		
		if (miStatFact == null) {
			//构建表达式
			MiCalExpress miCalExpress = new MiCalExpress();
			miCalExpress.setCodeTxt(codeTxt);
			commService.insert(miCalExpress);
			
			//构建事实
			miStatFact = new MiStatFact();
			miStatFact.setCode(modelItemCode);
			miStatFact.setExpressId(miCalExpress.getId());
			
			commService.insert(miStatFact);
		} else {
			Integer expressId = miStatFact.getExpressId();
			if (expressId == null) {
				//构建表达式
				MiCalExpress miCalExpress = new MiCalExpress();
				miCalExpress.setCodeTxt(codeTxt);
				commService.insert(miCalExpress);
				
				miStatFact.setExpressId(miCalExpress.getId());
				commService.update(miStatFact);
			} else {
				//构建表达式， 
				MiCalExpress miCalExpress = commService.get(MiCalExpress.class, expressId);
				miCalExpress.setCodeTxt(codeTxt);
				commService.update(miCalExpress);
			}
			
		}
		
	}

	/**
	 * 保存维度表达式
	 * @param codeTxt
	 * @param modelItemCode
	 */
	public void saveDimensionExpress(String codeTxt, String modelItemCode) {
		MiStatDimension miStatDimension = commService.get(MiStatDimension.class, modelItemCode);
		
		if (miStatDimension == null) {
			//构建表达式
			MiCalExpress miCalExpress = new MiCalExpress();
			miCalExpress.setCodeTxt(codeTxt);
			
			commService.insert(miCalExpress);
			//构建维度
			miStatDimension = new MiStatDimension(modelItemCode, miCalExpress.getId(), 1);
			commService.insert(miStatDimension);
		} else {
			
			Integer expressId = miStatDimension.getExpressId();
			
			//构建表达式， 
			MiCalExpress miCalExpress = commService.get(MiCalExpress.class, expressId);
			miCalExpress.setCodeTxt(codeTxt);
			commService.update(miCalExpress);
		}
		
	}
	
	
	@Override
	public void saveCommFilterGroup(MiFilterGroup miFilterGroup) {

		if (miFilterGroup.getId() == null) {
			commService.insert(miFilterGroup);
		} else {
			commService.update(miFilterGroup);
		}
		
	}

	@Override
	public void saveMiFilterCriterion(MiFilterCriterion miFilterCriterion) {
		
		if (miFilterCriterion.getId() == null) {
			commService.insert(miFilterCriterion);
		} else {
			commService.update(miFilterCriterion);
		}
		
	}

	@Override
	public List<MiFilterCriterion> getMiFilterCriterionByPid(Integer groupId) {
		
		return miExpreAndFilterDao.getMiFilterCriterionByPid(groupId);
	}

	@Override
	public void saveFilter(String miCode, Integer type, Integer filterId) {

		switch (type) {
		case 0:
			// 统计实体， 过滤条件保存
			saveStatModelFilter(miCode, filterId);
			break;
		case 1:
			// 事实属性， 过滤条件保存
			saveFactFilter(miCode, filterId);
			break;
		case 2:
			saveMiCalculatedFilter(miCode, filterId);
			break;
		case 3:
		case 5:
			// 结构体， 过滤条件保存
			saveStrucFilter(Integer.parseInt(miCode), filterId);
		case 6:
			// 事实组的保存
			saveFactGroupFilter(miCode, filterId);
			break;
		}
		
	}
	
	private void saveFactGroupFilter(String miCode, Integer filterId) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 保存计算属性的过滤条件
	 * @param miCode
	 * @param filterId
	 */
	private void saveMiCalculatedFilter(String miCode, Integer filterId) {
		MiCalculated miCalculated = commService.get(MiCalculated.class, miCode);
		
		if (miCalculated == null) {
			miCalculated = new MiCalculated(miCode, null, filterId, null);
			commService.insert(miCalculated);
		} else {
			miCalculated.setFilterId(filterId);
			commService.update(miCalculated);
		}
		
	}

	/**
	 * 保存结构体， 的过滤条件
	 * @param miCode
	 * @param filterId
	 */
		private void saveStrucFilter(Integer sbId, Integer filterId) {
			StrucFilter strucFilter = commService.get(StrucFilter.class, sbId);
			
			if (strucFilter == null) {
				strucFilter = new StrucFilter(sbId, filterId);
				commService.insert(strucFilter);
			} else {
				strucFilter.setFilterGroupId(filterId);
				commService.update(strucFilter);
			}
		
		}
	
	/**
	 * 事实属性， 过滤条件保存
	 * @param miCode
	 * @param filterId
	 */
	private void saveFactFilter(String miCode, Integer filterId) {
		MiStatFact miStatFact = commService.get(MiStatFact.class, miCode);
		miStatFact.setFilterId(filterId);
		commService.update(miStatFact);
	}
	
	/**
	 * 统计实体过滤条件保存
	 * @param miCode
	 * @param filterId
	 */
	private void saveStatModelFilter(String miCode, Integer filterId) {
		MiModelStat miModelStat = commService.get(MiModelStat.class, miCode);
		miModelStat.setFilterId(filterId);
		
		commService.update(miModelStat);
	}
	
	@Override
	public List<MiFilterGroup> getMiFilterGroupByPid(Integer groupId) {
		return miExpreAndFilterDao.getMiFilterGroupByPid(groupId);
	}

	@Override
	public void saveFilterRGroup(MiFilterContainer miFilterContainer) {
		MiFilterGroup miFilterGroup = miFilterContainer.getMiFilterGroup();
		MiFilterRgroup miFilterRgroup = miFilterContainer.getMiFilterRgroup();
		
		if (miFilterGroup.getId() == null) {
			// 首次添加
			commService.insert(miFilterGroup);
			miFilterRgroup.setGroupId(miFilterGroup.getId());
			commService.insert(miFilterRgroup);
		} else {
			//更新
			
			commService.update(miFilterGroup);
			miFilterRgroup.setGroupId(miFilterGroup.getId());
			commService.update(miFilterRgroup);
		}
	}

	@Override
	public void saveSqlModelSql(String sqlTxt, String modelItemCode) {
		MiStatFactGroup miModelSql = commService.get(MiStatFactGroup.class, modelItemCode);
		
		if (miModelSql == null) {
			miModelSql = new MiStatFactGroup(modelItemCode, sqlTxt);
			commService.insert(miModelSql);
		} else {
			miModelSql.setSqlTxt(sqlTxt);
			commService.update(miModelSql);
		}
		
	}
	
}
