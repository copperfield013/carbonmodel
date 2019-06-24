package cho.carbon.imodel.model.modelitem.strategy;

import cho.carbon.imodel.model.cascadedict.service.CascadedictBasicItemService;
import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.service.ModelItemCodeGeneratorService;
import cho.carbon.imodel.model.modelitem.service.ModelItemService;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;

public interface MiStrategy {
	/**
	 * 
	 * @throws Exception
	 */
	public void saveOrUpdate(ModelItemContainer modelItemContainer, CommService commService, String flag, CascadedictBasicItemService casenumItemService, ModelItemService modelItemService, ModelItemCodeGeneratorService modelItemCodeGeneratorService) throws Exception;
	
	
	/**
	 * 删除ModelItem 以及衍生属性
	 * @param modelItem
	 * @param commService
	 */
	public void delModelItem(ModelItem modelItem , CommService commService, ModelItemService modelItemService);
}
