package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.pojo.StrucPointer;
import cho.carbon.imodel.model.struct.pojo.StrucRRef;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	引用关联策略
 * @author so-well
 *
 */
public class RrefFieldStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		strucMiCode.setSbId(sbId);
		
		StrucFieldValue strucFieldValue = strucBaseContainer.getStrucFieldValue();
		strucFieldValue.setSbId(sbId);
		
		StrucPointer strucPointer = strucBaseContainer.getStrucPointer();
		strucPointer.setSbId(sbId);
		
		StrucRRef strucRRef = strucBaseContainer.getStrucRRef();
		strucRRef.setSbId(sbId);
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucFieldValue);
			commService.insert(strucPointer);
			commService.insert(strucRRef);
		}  else {
			commService.update(strucMiCode);
			commService.update(strucFieldValue);
			commService.update(strucPointer);
			commService.update(strucRRef);
		}
		
	}

	@Override
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService) {
		StrucMiCode strucMiCode = new StrucMiCode(sbId, null);
		StrucFieldValue strucFieldValue = new StrucFieldValue(sbId, null);
		StrucPointer strucPointer = new StrucPointer(sbId, null);
		StrucRRef strucRRef = new StrucRRef(sbId, null);
		
		commService.delete(strucMiCode);
		commService.delete(strucFieldValue);
		commService.delete(strucPointer);
		commService.delete(strucRRef);
	}

}
