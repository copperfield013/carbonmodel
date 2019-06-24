package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiTowlevel;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

/**
 * ModelItemType 
 * 	二级属性
 * @author so-well
 *
 */
public class TwoLevelAttrMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiTowlevel miTowlevel = modelItemContainer.getMiTowlevel();
		
		ModelItem modelItem2 = commService.get(ModelItem.class, modelItem.getParent());
		
		modelItem.setParent(modelItem2.getBelongModel());
		modelItem.setBelongModel(modelItem2.getBelongModel());
		modelItem.setUsingState(0);
		
		miTowlevel.setCode(modelItem.getCode());
		
		if ("add".equals(flag)) {
			commService.insert(miTowlevel);
		} else {
			commService.update(miTowlevel);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		MiTowlevel miTowlevel = new MiTowlevel();
		miTowlevel.setCode(modelItem.getCode());
		
		commService.delete(miTowlevel);
	}
	
}
