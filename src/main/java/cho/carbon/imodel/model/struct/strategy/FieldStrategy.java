package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	字段策略
 * @author so-well
 *
 */
public class FieldStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		strucMiCode.setSbId(sbId);
		
		StrucFieldValue strucFieldValue = strucBaseContainer.getStrucFieldValue();
		strucFieldValue.setSbId(sbId);
		
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucFieldValue);
		}  else {
			commService.update(strucMiCode);
			commService.update(strucFieldValue);
		}
		
	}

	@Override
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService) {
		
		StrucMiCode strucMiCode = new StrucMiCode(sbId, null);
		StrucFieldValue strucFieldValue = new StrucFieldValue(sbId, null);
		commService.delete(strucMiCode);
		commService.delete(strucFieldValue);
	}

	@Override
	public void copyStruct(Integer sourceSbId, Integer cloneSbId, CommService commService,
			StrucBaseService strucBaseService) throws CloneNotSupportedException {
		//克隆StrucMiCode
		StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sourceSbId);
		StrucMiCode cloneStrucMiCode = (StrucMiCode)strucMiCode.clone();
		cloneStrucMiCode.setSbId(cloneSbId);
		commService.insert(cloneStrucMiCode);
		
		//克隆StrucFieldValue
		StrucFieldValue strucFieldValue = commService.get(StrucFieldValue.class, sourceSbId);
		StrucFieldValue cloneStrucFieldValue = (StrucFieldValue)strucFieldValue.clone();
		cloneStrucFieldValue.setSbId(cloneSbId);
		commService.insert(cloneStrucFieldValue);
	}

	
}
