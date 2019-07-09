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
 * ModelItemType
 * .MULTI_ENUM_ITEM
 * 
 * 	多选枚举属性  策略
 * @author so-well
 *
 */
public class FileItemMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		
		MiValue miValue = new MiValue();
		modelItemContainer.setMiValue(miValue);
		String tableName = "t_" + modelItem.getBelongModel()+ "_" + modelItem.getParent();
		
		miValue.setCode(modelItem.getCode());
		miValue.setDataType(ItemValueType.STRING.getIndex()+"");
		miValue.setDataLength("32");
		miValue.setBelongTable(tableName);
		miValue.setUsingState(0);
		
		if ("add".equals(flag)) {
			//这里要生成 4个伴生属性
			commService.insert(miValue);
			createCorrelationAttr(modelItem, commService, tableName);
		} else {
			commService.update(miValue);
		}
	}
	
	private void createCorrelationAttr(ModelItem modelItem, CommService commService, String tableName) {
		String code_FN = ModelItemValueParter.getFileNameName(modelItem.getCode());
		String code_FP = ModelItemValueParter.getFileKeyName(modelItem.getCode());
		String code_FSF = ModelItemValueParter.getFileSuffixName(modelItem.getCode());
		String code_FSK = ModelItemValueParter.getFileKBSizeName(modelItem.getCode());
		
		MiValue miValue1 = new MiValue(code_FN, ItemValueType.STRING.getIndex() +"", "256", tableName, 0);
		MiValue miValue2 = new MiValue(code_FP, ItemValueType.STRING.getIndex() + "", "32", tableName, 0);
		MiValue miValue3 = new MiValue(code_FSF, ItemValueType.STRING.getIndex() + "", "32", tableName, 0);	
		MiValue miValue4 = new MiValue(code_FSK, ItemValueType.DECIMAL.getIndex() + "", "10,4", tableName, 0);	
		
		commService.insert(miValue1);
		commService.insert(miValue2);
		commService.insert(miValue3);
		commService.insert(miValue4);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		String code = modelItem.getCode();
		
		MiValue miValue = new MiValue();
		miValue.setCode(code);
		commService.delete(miValue);
		
		//这里要删除四个衍生属性
		
		String code_FN = ModelItemValueParter.getFileNameName(modelItem.getCode());
		String code_FP = ModelItemValueParter.getFileKeyName(modelItem.getCode());
		String code_FSF = ModelItemValueParter.getFileSuffixName(modelItem.getCode());
		String code_FSK = ModelItemValueParter.getFileKBSizeName(modelItem.getCode());
		
		MiValue miValue1 = new MiValue();
		miValue1.setCode(code_FN);
		MiValue miValue2 = new MiValue();
		miValue2.setCode(code_FP);
		MiValue miValue3 = new MiValue();
		miValue3.setCode(code_FSF);
		MiValue miValue4 = new MiValue();
		miValue4.setCode(code_FSK);
		
		commService.delete(miValue1);
		commService.delete(miValue2);
		commService.delete(miValue3);
		commService.delete(miValue4);
	}

	
	
}
