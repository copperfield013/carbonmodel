package cho.carbon.imodel.model.struct.strategy;

import java.util.List;

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
	
	public void copyStruct(Integer sbId) throws CloneNotSupportedException {
		
		StrucBase strucBase = commService.get(StrucBase.class, sbId);
		//复制结构体
		shuntStruc(strucBase, null);
	}
	
	private void shuntStruc(StrucBase strucBase, Integer pSbId) throws CloneNotSupportedException {
		//克隆对象
		StrucBase cloneStrucBase = (StrucBase)strucBase.clone();
		cloneStrucBase.setId(null);
		cloneStrucBase.setParentId(pSbId);
		commService.insert(cloneStrucBase);
		
		//克隆此对象， 并固化
		StrucElementType strucElementType = StrucElementType.getType(strucBase.getType());
		StructBaseStrategy structStrategy = StructStrategyFactory.getStructStrategy(strucElementType);
		if (structStrategy != null) {
			structStrategy.copyStruct(strucBase.getId(), cloneStrucBase.getId(), commService, strucBaseService);
		}
		
		switch (strucElementType) {
			case STRUC:
			case GROUP1D:
			case GROUP2D:
			case RSTRUC:
				List<StrucBase> structStairChild = strucBaseService.getStructStairChild(strucBase.getId());
				//继续克隆孩子啊
				for (StrucBase strucBase2 : structStairChild) {
					shuntStruc(strucBase2, cloneStrucBase.getId());
				}
				break;
		}
	}
	
}
