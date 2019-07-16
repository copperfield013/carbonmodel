package cho.carbon.imodel.model.modelitem.strategy;


import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.constant.ModelItemValueParter;
import cho.carbon.meta.enun.ItemValueType;

/**
 * ModelItemType.MODEL
 * 	实体策略
 * @author so-well
 *
 */
public class ModelItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		modelItem.setUsingState(0);
		
		if ("add".equals(flag)) {
			createCorrelationMiValue(modelItem, commService);
		} 
	}
	
	//实体伴生属性
	private void createCorrelationMiValue(ModelItem modelItem, CommService commService) {
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		String code_SP = ModelItemValueParter.getStatKeyName(modelItem.getCode());
		
		String tableName = "t_" + modelItem.getBelongModel()+ "_m";
		MiValue miValue_ED = new MiValue(code_ED, ItemValueType.TIMESTAMP.getIndex() + "", "3", tableName, 0);
		MiValue miValue_SP = new MiValue(code_SP, ItemValueType.STRING.getIndex() + "", "32", tableName, 0);
		
		commService.insert(miValue_ED);
		commService.insert(miValue_SP);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService,ModelItemService modelItemService) {
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		String code_SP = ModelItemValueParter.getStatKeyName(modelItem.getCode());
		
		//构建实体衍生数据
		MiValue miValue_ED = new MiValue();
		miValue_ED.setCode(code_ED);
		commService.delete(miValue_ED);
		
		MiValue miValue_SP = new MiValue();
		miValue_SP.setCode(code_SP);
		commService.delete(miValue_SP);
	}
	
}
