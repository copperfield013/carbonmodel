package cho.carbon.imodel.model.modelitem.strategy;

import com.abc.model.constant.ModelItemValueParter;
import com.abc.model.enun.ItemValueType;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

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
		
		if ("add".equals(flag)) {
			createCorrelationMiValue(modelItem, commService);
		} 
	}
	
	//实体伴生属性
	private void createCorrelationMiValue(ModelItem modelItem, CommService commService) {
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		String tableName = "t_" + modelItem.getBelongModel()+ "_m";
		MiValue miValue = new MiValue(code_ED, ItemValueType.TIMESTAMP.getIndex() + "", "3", tableName, 0);
		
		commService.insert(miValue);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService,ModelItemService modelItemService) {
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		
		//构建实体衍生数据
		MiValue miValue = new MiValue();
		miValue.setCode(code_ED);
		commService.delete(miValue);
	}

	
	
}
