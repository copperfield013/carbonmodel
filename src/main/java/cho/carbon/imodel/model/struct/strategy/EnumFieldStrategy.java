package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldSubenum;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.AttributeValueType;

/**
 * 	枚举字段策略
 * @author so-well
 *
 */
public class EnumFieldStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		strucMiCode.setSbId(sbId);
		//子集对应
		StrucFieldSubenum strucFieldSubenum = strucBaseContainer.getStrucFieldSubenum();
		if(strucFieldSubenum == null) {
			strucFieldSubenum = new StrucFieldSubenum();
		}
		strucFieldSubenum.setSbId(sbId);
		
		StrucFieldValue strucFieldValue = strucBaseContainer.getStrucFieldValue();
		strucFieldValue.setSbId(sbId);
		strucFieldValue.setValueType(AttributeValueType.STRING.getIndex());
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
		
		commService.delete(strucMiCode);
		commService.delete(strucFieldSubenum);
		commService.delete(strucFieldValue);
	}

	@Override
	public void copyStruct(Integer sourceSbId, Integer cloneSbId, CommService commService,
			StrucBaseService strucBaseService) throws CloneNotSupportedException {
		
		StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sourceSbId);
		StrucMiCode cloneStrucMiCode = (StrucMiCode)strucMiCode.clone();
		cloneStrucMiCode.setSbId(cloneSbId);
		commService.insert(cloneStrucMiCode);
		
		StrucFieldSubenum strucFieldSubenum = commService.get(StrucFieldSubenum.class, sourceSbId);
		StrucFieldSubenum cloneStrucFieldSubenum = (StrucFieldSubenum)strucFieldSubenum.clone();
		cloneStrucFieldSubenum.setSbId(cloneSbId);
		commService.insert(cloneStrucFieldSubenum);
		
		StrucFieldValue strucFieldValue = commService.get(StrucFieldValue.class, sourceSbId);
		StrucFieldValue cloneStrucFieldValue = (StrucFieldValue)strucFieldValue.clone();
		cloneStrucFieldValue.setSbId(cloneSbId);
		commService.insert(cloneStrucFieldValue);
	}

	
}
