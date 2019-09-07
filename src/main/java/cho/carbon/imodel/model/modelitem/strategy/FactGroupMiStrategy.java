package cho.carbon.imodel.model.modelitem.strategy;


import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.MiCalExpress;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterGroup;
import cho.carbon.imodel.model.modelitem.pojo.MiStatFact;
import cho.carbon.imodel.model.modelitem.pojo.MiValue;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.constant.ModelItemValueParter;
import cho.carbon.meta.enun.AggregateFunctionType;
import cho.carbon.meta.enun.ItemValueType;
import cho.carbon.meta.enun.ModelItemType;

/**
 * ModelItemType
 * 事实组策略
 * @author so-well
 *
 */
public class FactGroupMiStrategy implements MiStrategy {

	@Override
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		
		// 创建事实组的count属性
		if ("add" == flag) {
			createCorrelationAttr(modelItem, commService);
		}
		
	}
	
	private void createCorrelationAttr(ModelItem modelItem, CommService commService) {
		String tableName = "t_" + modelItem.getBelongModel()+ "_STAT";
		String code_cnt = ModelItemValueParter.getStatCountName(modelItem.getCode());
		
		// 生成默认cnt（count( * )
		ModelItem miCnt = new ModelItem(code_cnt, modelItem.getCode()+"数量", ModelItemType.FACT_ITEM.getIndex(), modelItem.getCode(), modelItem.getBelongModel(), 1, null); 
		commService.insert(miCnt);
		
		MiValue miValue1 = new MiValue(code_cnt, ItemValueType.STRING.getIndex() +"", "11", tableName, 0);
		commService.insert(miValue1);
		
		// 表达式id
		MiCalExpress miCalExpress = new MiCalExpress(null, "count(*)", "count(*)");
		commService.insert(miCalExpress);
		
		// 创建过滤条件
		MiFilterGroup miFilterGroup = new MiFilterGroup();
		miFilterGroup.setType(1);
		miFilterGroup.setName("默认组名");
		miFilterGroup.setLogicalOperator(2);
		commService.insert(miFilterGroup);
		
		//创建事实
		MiStatFact miStatFact = new MiStatFact(code_cnt, miCalExpress.getId() , miFilterGroup.getId(), AggregateFunctionType.SUM.getIndex(), 1);
		commService.insert(miStatFact);
	}

	@Override
	public void delModelItem(ModelItem modelItem, CommService commService, ModelItemService modelItemService) {
		
		String code_cnt = ModelItemValueParter.getStatCountName(modelItem.getCode());
		
		MiStatFact miStatFact = commService.get(MiStatFact.class, code_cnt);
		//删除表达式
		MiCalExpress miCalExpress = new MiCalExpress();
		miCalExpress.setId(miStatFact.getExpressId());
		commService.delete(miCalExpress);
		// 删除事实
		commService.delete(miStatFact);
		
		ModelItem miCnt = new ModelItem();
		miCnt.setCode(code_cnt);
		commService.delete(miCnt);
		
		MiValue miValue1  = new MiValue();
		miValue1.setCode(code_cnt);
		commService.delete(miValue1);
	}

	

	
	
}
