package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	结构体策略
 * @author so-well
 *
 */
public class StructStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		
		if ("add".contentEquals(flag)) {
			StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
			strucMiCode.setSbId(strucBaseContainer.getStrucBase().getId());
			//只能生产， 不能更新锁选择的ModeItemCode
			commService.insert(strucMiCode);
		} 
		
	}

	@Override
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService) {
		
		StrucMiCode strucMiCode = new StrucMiCode(sbId, null);
		commService.delete(strucMiCode);
	}
	
}
