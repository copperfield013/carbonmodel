package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.constant.ModelItemValueParter;

/**
 * ModelItemType.
 * 	事实属性
 * @author so-well
 *
 */
public class FactMiStrategy extends ValueItemMiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		MiValue miValue = modelItemContainer.getMiValue();
		miValue.setCode(modelItem.getCode());
		miValue.setBelongTable("t_" + modelItem.getBelongModel()+ "_STAT");
		
		
		if ("add".equals(flag)) {
			//事实属性的父亲
			String parent = modelItem.getParent();
			//事实属性父亲的半生属性code
			String code_cnt = ModelItemValueParter.getStatCountName(parent);
			// 半生属性的事实属性
			MiStatFact pfact = commService.get(MiStatFact.class, code_cnt);
			Integer filterId = pfact.getFilterId();
			
			MiStatFact miStatFact = modelItemContainer.getMiStatFact();
			miStatFact.setCode(modelItem.getCode());
			miStatFact.setFilterId(filterId);
			
			commService.insert(miValue);
			commService.insert(miStatFact);
		} else {
			commService.update(miValue);
			
			MiStatFact miStatFact = commService.get(MiStatFact.class, modelItem.getCode());
			miStatFact.setUpdrillFuncType(modelItemContainer.getMiStatFact().getUpdrillFuncType());
			commService.update(miStatFact);
		}
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		super.delModelItem(modelItem, commService, modelItemService);
		
		// 这里还要删除对应的表达式， 或者是过滤条件
		MiStatFact miStatFact = commService.get(MiStatFact.class, modelItem.getCode());
		
		if (miStatFact != null) {
			//删除事实的表达式
			MiCalExpress miCalExpress = new MiCalExpress();
			miCalExpress.setId(miStatFact.getExpressId());
			commService.delete(miCalExpress);
			
			//删除事实的过滤条件
			//TODO...删除过滤条件还没开始做
			
			//删除事实
			commService.delete(miStatFact);
		}
		
		
	}

	
	
}
