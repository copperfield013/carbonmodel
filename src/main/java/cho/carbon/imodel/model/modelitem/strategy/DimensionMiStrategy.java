package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiStatDimension;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

/**
 * ModelItemType.
 * 	维度属性
 * @author so-well
 *
 */
public class DimensionMiStrategy extends ValueItemMiStrategy {

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
		
		//获取维度
		MiStatDimension miStatDimension = commService.get(MiStatDimension.class, modelItem.getCode());
		
		if (miStatDimension != null) {
			//删除维度的表达式
			MiCalExpress miCalExpress = new MiCalExpress();
			miCalExpress.setId(miStatDimension.getExpressId());
			commService.delete(miCalExpress);
			
			//删除维度
			commService.delete(miStatDimension);
		}
		
	}

	
	
}
