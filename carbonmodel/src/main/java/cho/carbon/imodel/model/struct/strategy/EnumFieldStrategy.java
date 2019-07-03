package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldSubenum;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	枚举字段策略
 * @author so-well
 *
 */
public class EnumFieldStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		strucMiCode.setSbId(sbId);
		//子集对应
		StrucFieldSubenum strucFieldSubenum = strucBaseContainer.getStrucFieldSubenum();
		if(strucFieldSubenum == null) {
			strucFieldSubenum = new StrucFieldSubenum();
		}
		strucFieldSubenum.setSbId(sbId);
		
		StrucFieldValue strucFieldValue = strucBaseContainer.getStrucFieldValue();
		strucFieldValue.setSbId(sbId);
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucFieldSubenum);
			commService.insert(strucFieldValue);
		}  else {
			commService.update(strucMiCode);
			commService.update(strucFieldSubenum);
			commService.update(strucFieldValue);
		}
		
	}

	@Override
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService) {
		
		StrucMiCode strucMiCode = new StrucMiCode(sbId, null);
		StrucFieldSubenum strucFieldSubenum = new StrucFieldSubenum(sbId, null);
		StrucFieldValue strucFieldValue = new StrucFieldValue(sbId, null);
		
		commService.insert(strucMiCode);
		commService.insert(strucFieldSubenum);
		commService.insert(strucFieldValue);
	}

	
}
