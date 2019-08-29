package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiReference;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.enun.ItemValueType;

/**
 * ModelItemType. 
 * 	引用属性  策略
 * @author so-well
 *
 */
public class ReferenceItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_" + modelItem.getParent());
		miValue.setDataLength("32");
		miValue.setDataType(ItemValueType.STRING.getIndex() + "");
		
		MiReference miReference = modelItemContainer.getMiReference();
		miReference.setCode(modelItem.getCode());
		
		if ("add".equals(flag)) {
			commService.insert(miValue);
			commService.insert(miReference);
		} else {
			commService.update(miValue);
			commService.update(miReference);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		MiValue miValue = new MiValue();
		miValue.setCode(modelItem.getCode());
		MiReference miReference = new MiReference();
		miReference.setCode(modelItem.getCode());
		
		commService.delete(miValue);
		commService.delete(miReference);
	}

	
	
}
