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
 * ModelItemType 
 * 	多行组  策略
 * @author so-well
 *
 */
public class MultiLineGroupMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		
		if("add".contentEquals(flag)) {
			//生成伴生属性
			createCorrelationMiValue(modelItem, commService);
		}
	}

	private void createCorrelationMiValue(ModelItem modelItem, CommService commService) {
		String code_P = ModelItemValueParter.getRepeatKeyName(modelItem.getCode());
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		
		String tableName = "t_" + modelItem.getBelongModel()+ "_" + modelItem.getCode();
		
		MiValue miValue1 = new MiValue(code_P, ItemValueType.STRING.getIndex() +"", "32", tableName, 0);
		MiValue miValue2 = new MiValue(code_ED, ItemValueType.TIMESTAMP.getIndex() + "", "3", tableName, 0);
		
		commService.insert(miValue1);
		commService.insert(miValue2);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		//删除衍生属性
		String code_P = ModelItemValueParter.getRepeatKeyName(modelItem.getCode());
		String code_ED = ModelItemValueParter.getRepeatEditTimeName(modelItem.getCode());
		
		MiValue miValue1 = new MiValue();
		miValue1.setCode(code_P);
		MiValue miValue2 = new MiValue();
		miValue2.setCode(code_ED);
		
		commService.delete(miValue1);
		commService.delete(miValue2);
	}
	
}
