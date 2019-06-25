package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

/**
 * ModelItemType.
 * 	事实和维度属性
 * @author so-well
 *
 */
public class FactDimeMiStrategy extends ValueItemMiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_STAT");
		
		if ("add".equals(flag)) {
			commService.insert(miValue);
		} else {
			commService.update(miValue);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		super.delModelItem(modelItem, commService, modelItemService);
		
		// 这里还要删除对应的表达式， 或者是过滤条件
		
	}

	
	
}
