package cho.carbon.imodel.model.modelitem.strategy;

import com.abc.model.enun.ModelItemType;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

/**
 * 	策略的书包
 * @author so-well
 *
 */
public class MiStrategyContext {
	CommService commService = null;
	CascadedictBasicItemService casenumItemService = null;
	ModelItemService modelItemService = null;
	ModelItemCodeGeneratorService modelItemCodeGeneratorService = null;
	
	public MiStrategyContext(CommService commService, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) {
		this.commService = commService;
		this.casenumItemService = casenumItemService;
		this.modelItemService = modelItemService;
		this.modelItemCodeGeneratorService = modelItemCodeGeneratorService;
	}
	
	/**
	 * 保存ModelItem
	 * @param flag
	 * @param modelItemContainer
	 * @throws Exception
	 */
	public void saveOrUpdateMi(String flag, ModelItemContainer modelItemContainer) throws Exception {
		ModelItem modelItem = modelItemContainer.getModelItem();
		ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
		
		MiStrategy miStrategy = MiStrategyFactory.getMiStrategy(itemType);
		if (miStrategy!=null) {
			miStrategy.saveOrUpdate(modelItemContainer, commService, flag, casenumItemService, modelItemService, modelItemCodeGeneratorService);
		}
		
		// 这里没什么问题， 先保存为敬
		if ("add".equals(flag)) {
			commService.insert(modelItem);
		} else {
			commService.update(modelItem);
		}
		
	}
	
	/**
	 * 删除ModelItem
	 * @param miCode
	 */
	public void delModelItem(String miCode) {
		ModelItem modelItem = commService.get(ModelItem.class, miCode);
		ModelItemType itemType = ModelItemType.getItemType(modelItem.getType());
		
		//处理衍生数据
		MiStrategy miStrategy = MiStrategyFactory.getMiStrategy(itemType);
		if (miStrategy!=null) {
			miStrategy.delModelItem(modelItem, commService, modelItemService);
		}
		
		//删除自己
		commService.delete(modelItem);
	}
	
	
}
