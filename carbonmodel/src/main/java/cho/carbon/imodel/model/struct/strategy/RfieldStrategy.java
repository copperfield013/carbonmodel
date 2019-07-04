package cho.carbon.imodel.model.struct.strategy;

import java.util.List;

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
		
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucFieldValue);
		}  else {
			commService.update(strucMiCode);
			commService.update(strucFieldValue);
		}
		
		
		//删除具体关系
		List<StrucRelation> strucRelationBySbId = strucBaseService.getStrucRelationBySbId(sbId);
		for (StrucRelation strucRelation : strucRelationBySbId) {
			commService.delete(strucRelation);
		}
		
		//重新添加关系
		String modelRelationType = strucBaseContainer.getStrucRelation().getModelRelationType();
		String[] modelRelas = modelRelationType.split(",");
		for (int i = 0; i < modelRelas.length; i++) {
			String modelRelaCode = modelRelas[i];
			StrucRelation strucRela = new StrucRelation(null, sbId, modelRelaCode);
			commService.insert(strucRela);
		}
		
	}

	@Override
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService) {
		StrucMiCode strucMiCode = new StrucMiCode(sbId, null);
		StrucFieldValue strucFieldValue = new StrucFieldValue(sbId, null);
		
		commService.delete(strucMiCode);
		commService.delete(strucFieldValue);
		//删除具体关系
		List<StrucRelation> strucRelationBySbId = strucBaseService.getStrucRelationBySbId(sbId);
		for (StrucRelation strucRelation : strucRelationBySbId) {
			commService.delete(strucRelation);
		}
	}

	@Override
	public void copyStruct(Integer sourceSbId, Integer cloneSbId, CommService commService,
			StrucBaseService strucBaseService) throws CloneNotSupportedException {
		
		
		StrucMiCode strucMiCode = commService.get(StrucMiCode.class, sourceSbId);
		StrucMiCode cloneStrucMiCode = (StrucMiCode)strucMiCode.clone();
		cloneStrucMiCode.setSbId(cloneSbId);
		commService.insert(cloneStrucMiCode);
		
		StrucFieldValue strucFieldValue = commService.get(StrucFieldValue.class, sourceSbId);
		StrucFieldValue cloneStrucFieldValue = (StrucFieldValue)strucFieldValue.clone();
		cloneStrucFieldValue.setSbId(cloneSbId);
		commService.insert(cloneStrucFieldValue);
		
		//克隆具体关系
		List<StrucRelation> strucRelationBySbId = strucBaseService.getStrucRelationBySbId(sourceSbId);
		for (StrucRelation strucRelation : strucRelationBySbId) {
			StrucRelation cloneStrucRelation = (StrucRelation)strucRelation.clone();
			cloneStrucRelation.setSbId(cloneSbId);
			cloneStrucRelation.setId(null);
			commService.insert(cloneStrucRelation);
		}
	}

	
}
