package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiEnum;
import cho.carbon.imodel.model.modelitem.pojo.MiStatDimension;
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
 * 	维度枚举属性  策略
 * @author so-well
 *
 */
public class DimenEnumItemMiStrategy extends EnumItemMiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		MiEnum miEnum = modelItemContainer.getMiEnum();
		
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_STAT");
		
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
