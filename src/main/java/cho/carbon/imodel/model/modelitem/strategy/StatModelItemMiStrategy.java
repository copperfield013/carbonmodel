package cho.carbon.imodel.model.modelitem.strategy;


import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiModelStat;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.constant.ModelItemValueParter;
import cho.carbon.meta.enun.ItemValueType;

/**
 * ModelItemType.STAT_MODEL
 * 	片段统计实体策略
 * @author so-well
 *
 */
public class StatModelItemMiStrategy extends ModelItemMiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		super.saveOrUpdate(modelItemContainer, commService, flag, casenumItemService, modelItemService, modelItemCodeGeneratorService);
	
		
		if ("add".equals(flag)) {
			
			//获取统计实体
			MiModelStat miModelStat = modelItemContainer.getMiModelStat();
			miModelStat.setCode(modelItemContainer.getModelItem().getCode());
			commService.insert(miModelStat);
		}
		
	}
	

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService,ModelItemService modelItemService) {
		super.delModelItem(modelItem, commService, modelItemService);
		
		//删除统计实体
		MiModelStat miModelStat = new MiModelStat();
		miModelStat.setCode(modelItem.getCode());
		
		commService.delete(miModelStat);
	}

	
	
}
