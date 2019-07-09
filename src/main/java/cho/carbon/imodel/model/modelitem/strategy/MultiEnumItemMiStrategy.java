package cho.carbon.imodel.model.modelitem.strategy;

import java.util.List;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.constant.ModelItemValueParter;
import cho.carbon.meta.enun.ItemValueType;

/**
 * ModelItemType
 * .MULTI_ENUM_ITEM
 * 
 * 	多选枚举属性  策略
 * @author so-well
 *
 */
public class MultiEnumItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiEnum miEnum = modelItemContainer.getMiEnum();
		
		miEnum.setCode(modelItem.getCode());
		if ("add".equals(flag)) {
			//这里要生成 3个伴生属性
			createCorrelationMiValue(modelItem, commService);
			commService.insert(miEnum);
		} else {
			commService.update(miEnum);
		}
	}
	
	private void createCorrelationMiValue(ModelItem modelItem, CommService commService) {
		String code_P = ModelItemValueParter.getRepeatKeyName(modelItem.getCode());
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		String code_V = ModelItemValueParter.getMEValueName(modelItem.getCode());
		
		String tableName = "t_" + modelItem.getBelongModel()+ "_" + modelItem.getCode();
		
		MiValue miValue1 = new MiValue(code_P, ItemValueType.STRING.getIndex() +"", "32", tableName, 0);
		MiValue miValue2 = new MiValue(code_ED, ItemValueType.TIMESTAMP.getIndex() + "", "3", tableName, 0);
		MiValue miValue3 = new MiValue(code_V, ItemValueType.STRING.getIndex() + "", "32", tableName, 0);	
		
		commService.insert(miValue1);
		commService.insert(miValue2);
		commService.insert(miValue3);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		String code = modelItem.getCode();
		
		MiEnum miEnum = new MiEnum();
		miEnum.setCode(code);
		
		commService.delete(miEnum);
		
		// 删除三个衍生属性
		String code_P = ModelItemValueParter.getRepeatKeyName(code);
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(code);
		String code_V = ModelItemValueParter.getMEValueName(code);
		
		
		MiValue miValue1 = new MiValue();
		miValue1.setCode(code_P);
		MiValue miValue2 = new MiValue();
		miValue2.setCode(code_ED);
		MiValue miValue3 = new MiValue();
		miValue3.setCode(code_V);
		
		commService.delete(miValue1);
		commService.delete(miValue2);
		commService.delete(miValue3);
	}

	
	
}
