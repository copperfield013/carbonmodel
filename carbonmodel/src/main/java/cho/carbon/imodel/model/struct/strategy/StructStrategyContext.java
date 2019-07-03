package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.StrucElementType;

public class StructStrategyContext {
	private CommService commService;
	private StrucBaseService strucBaseService;
	
	public StructStrategyContext(CommService commService, StrucBaseService strucBaseService) {
		this.commService = commService;
		this.strucBaseService = strucBaseService;
	}

	public void saveOrUpdate(StrucBaseContainer strucBaseContainer) {
		StrucBase strucBase = strucBaseContainer.getStrucBase();
		StrucElementType strucElementType = StrucElementType.getType(strucBase.getType());
		
		String flag = "";
		//先保存为敬
		if (strucBase.getId() == null) {
			flag = "add";
			commService.insert(strucBase);
		}else {
			commService.update(strucBase);
		}
		
		//执行策略
		StructBaseStrategy structStrategy = StructStrategyFactory.getStructStrategy(strucElementType);
		
		if (structStrategy != null) {
			structStrategy.saveOrUpdate(flag, strucBaseContainer, commService, strucBaseService);
		}
		
	}
	
	public void delStruct(Integer sbId) {
		//执行策略
		
		StrucBase strucBase = commService.get(StrucBase.class, sbId);
		StrucElementType strucElementType = StrucElementType.getType(strucBase.getType());
		StructBaseStrategy structStrategy = StructStrategyFactory.getStructStrategy(strucElementType);
		
		if (structStrategy != null) {
			structStrategy.delStruct(sbId, commService, strucBaseService);
		}
		
		commService.delete(strucBase);
	}
	
}
