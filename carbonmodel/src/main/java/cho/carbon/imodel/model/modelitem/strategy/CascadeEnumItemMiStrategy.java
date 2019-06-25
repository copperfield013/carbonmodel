package cho.carbon.imodel.model.modelitem.strategy;

import java.util.List;


import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.enun.ItemValueType;
import cho.carbon.meta.enun.ModelItemType;

/**
 * ModelItemType
 * 
 * 	级联枚举属性  策略
 * @author so-well
 *
 */
public class CascadeEnumItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		MiEnum miEnum = modelItemContainer.getMiEnum();
		
		
		String tableName = "t_" + modelItem.getBelongModel()+ "_" + modelItem.getCode();
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_" + modelItem.getCode());
		
		miEnum.setCode(modelItem.getCode());

		// 这里要生成级联枚举的孩子， 存放在表 MiValue和MiCascade中
		createMiValueCas(modelItem, miEnum,tableName, commService, casenumItemService, modelItemService, modelItemCodeGeneratorService);
		
		
		if ("add".equals(flag)) {
			commService.insert(miValue);
			commService.insert(miEnum);
		} else {
			commService.update(miValue);
			commService.update(miEnum);
		}
	}

	
	private void createMiValueCas(ModelItem modelItem,MiEnum miEnum,String tableName, CommService commService, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService miCodeGenService) throws Exception {
		// 枚举孩子 层深
		Integer casCaseDepth = casenumItemService.getCasCaseDepth(Integer.parseInt(miEnum.getPid()));
		//获取所填写数量
		Integer casEnumChildCount = modelItem.getCasEnumChildCount();
		
		//需要有几个孩子
		Integer min = getMin(casCaseDepth, casEnumChildCount);
		//实际有的孩子数量
		List<MiCascade> miCascadeList = modelItemService.getMiCascadeList(modelItem.getCode());
		Integer childCount = miCascadeList == null?0:miCascadeList.size();
		if(childCount!=min) {
			//实际孩子大于需要的， 删除实际的
			if (childCount > min) {
				Integer aCount = childCount-min;
				for (int i = 0; i < aCount; i++) {
					MiCascade miCascade = miCascadeList.get(i);
					MiValue miValue = commService.get(MiValue.class, miCascade.getSubCode());
					commService.delete(miCascade);
					commService.delete(miValue);
				}
			} else {
				Integer aCount = min-childCount;
				for (int i = 0; i < aCount; i++) {
					childCount++;
					
					String miCode = miCodeGenService.getBasicItemCode(ModelItemType.VALUE_ITEM, modelItem.getBelongModel());
					MiValue miValue2 = new MiValue(miCode, ItemValueType.STRING.getIndex() + "", "32", tableName, 0);
					
					MiCascade miCascade = new MiCascade(modelItem.getCode(),childCount , miCode);
					commService.insert(miValue2);
					commService.insert(miCascade);
				}
				
			}
		}
	}
	
	//返回小的数据
	private Integer getMin(Integer a, Integer b) {
		if (a <= b) {
			return a;
		} else {
			return b;
		}
	}


	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		MiValue miValue = new MiValue();
		miValue.setCode(modelItem.getCode());
		MiEnum miEnum = new MiEnum();
		miEnum.setCode(modelItem.getCode());
		
		commService.delete(miValue); 
		commService.delete(miEnum);
		  
		//实际有的孩子
		List<MiCascade> miCascadeList = modelItemService.getMiCascadeList(modelItem.getCode());
		if (!miCascadeList.isEmpty()) {
			for (MiCascade miCascade : miCascadeList) {
				commService.delete(miCascade);
			}
		}
		
		
	}


	
	
}
