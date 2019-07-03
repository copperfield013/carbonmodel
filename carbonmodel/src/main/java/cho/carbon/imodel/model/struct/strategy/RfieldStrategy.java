package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	关系字段策略
 * @author so-well
 *
 */
public class RfieldStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		strucMiCode.setSbId(sbId);
		
		StrucFieldValue strucFieldValue = strucBaseContainer.getStrucFieldValue();
		strucFieldValue.setSbId(sbId);
		
		
		StrucRelation strucRelation = strucBaseContainer.getStrucRelation();
		strucRelation.setSbId(sbId);
		
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucFieldValue);
			commService.insert(strucRelation);
		}  else {
			commService.update(strucMiCode);
			commService.update(strucFieldValue);
			commService.update(strucRelation);
		}
		
	}

	@Override
	public void delStruct(StrucBaseContainer strucBaseContainer, CommService commServicet, StrucBaseService strucBaseService) {
		// TODO Auto-generated method stub
		
	}
	
}
