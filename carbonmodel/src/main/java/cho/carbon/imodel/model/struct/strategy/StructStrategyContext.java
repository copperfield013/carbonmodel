package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.StrucElementType;

public class StructStrategyContext {
	private CommService commServicet;
	
	public StructStrategyContext(CommService commService) {
		this.commServicet = commService;
	}

	public void saveOrUpdate(StrucBaseContainer strucBaseContainer) {
		StrucBase strucBase = strucBaseContainer.getStrucBase();
		strucBase.setOpt(1);//这个先写默认， 以后再做
		StrucElementType strucElementType = StrucElementType.getType(strucBase.getType());
		
		String flag = "";
		//先保存为敬
		if (strucBase.getId() == null) {
			flag = "add";
			commServicet.insert(strucBase);
		}else {
			commServicet.update(strucBase);
		}
		
		//执行策略
		StructBaseStrategy structStrategy = StructStrategyFactory.getStructStrategy(strucElementType);
		
		if (structStrategy != null) {
			structStrategy.saveOrUpdate(flag, strucBaseContainer, commServicet);
		}
		
	}
	
}
