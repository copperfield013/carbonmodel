package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

/**
 * ModelItemType
 * .ENUM_ITEM 
 * .PREENUM_STRING_ITEM
 * 
 * 	枚举属性  策略
 * @author so-well
 *
 */
public class EnumItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		MiEnum miEnum = modelItemContainer.getMiEnum();
		
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_" + modelItem.getParent());
		
		miEnum.setCode(modelItem.getCode());
		
		if ("add".equals(flag)) {
			commService.insert(miValue);
			commService.insert(miEnum);
		} else {
			commService.update(miValue);
			commService.update(miEnum);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		MiValue miValue = new MiValue();
		MiEnum miEnum = new MiEnum();
		
		String code = modelItem.getCode();
		miValue.setCode(code);
		miEnum.setCode(code);
		
		commService.delete(miValue);
		commService.delete(miEnum);
	}

	
	
}
