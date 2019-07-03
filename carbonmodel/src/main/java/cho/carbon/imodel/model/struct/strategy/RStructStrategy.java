package cho.carbon.imodel.model.struct.strategy;

import java.util.List;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucFieldValue;
import cho.carbon.imodel.model.struct.pojo.StrucMiCode;
import cho.carbon.imodel.model.struct.pojo.StrucPointer;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

/**
 * 	关系结构策略
 * @author so-well
 *
 */
public class RStructStrategy  implements StructBaseStrategy {

	@Override
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commService, StrucBaseService strucBaseService) {
		
		Integer sbId = strucBaseContainer.getStrucBase().getId();
		
		StrucMiCode strucMiCode = strucBaseContainer.getStrucMiCode();
		strucMiCode.setSbId(sbId);
		
		StrucPointer strucPointer = strucBaseContainer.getStrucPointer();
		strucPointer.setSbId(sbId);
		
		if ("add".contentEquals(flag)) {
			commService.insert(strucMiCode);
			commService.insert(strucPointer);
			
		}  else {
			commService.update(strucMiCode);
			commService.update(strucPointer);
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
		StrucPointer strucPointer = new StrucPointer(sbId, null);
		
		commService.delete(strucMiCode);
		commService.delete(strucPointer);
		
		//删除具体关系
		List<StrucRelation> strucRelationBySbId = strucBaseService.getStrucRelationBySbId(sbId);
		for (StrucRelation strucRelation : strucRelationBySbId) {
			commService.delete(strucRelation);
		}
		
	}
	
}
