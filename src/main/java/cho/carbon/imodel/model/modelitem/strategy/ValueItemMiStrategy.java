package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.enun.ModelItemType;

/**
 * ModelItemType.VALUE_ITEM 
 * 	普通属性  策略
 * @author so-well
 *
 */
public class ValueItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		
		if (ModelItemType.CASCADE_REFERENCE_ITEM.getIndex() == modelItem.getType()) {
			//级联引用属性需要单独处理
			miValue.setDataLength("32");
		}
		
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_" + modelItem.getParent());
		
		if ("add".equals(flag)) {
			commService.insert(miValue);
		} else {
			commService.update(miValue);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		MiValue miValue = new MiValue();
		miValue.setCode(modelItem.getCode());
		commService.delete(miValue);
	}

	
	
}
