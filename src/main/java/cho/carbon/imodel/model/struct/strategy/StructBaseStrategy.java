package cho.carbon.imodel.model.struct.strategy;

import cho.carbon.imodel.model.comm.service.CommService;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.service.StrucBaseService;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;

public interface StructBaseStrategy {
	
	//保存结构体
	public void saveOrUpdate(String flag, StrucBaseContainer strucBaseContainer, CommService commServicet, StrucBaseService strucBaseService);
	
	//删除结构体
	public void delStruct(Integer sbId, CommService commService, StrucBaseService strucBaseService);
	
	/**
	 * 克隆本身， 并固化
	 * @param sourceSbId   需要克隆的sbId
	 * @param cloneSbId    克隆后的sbID
	 * @param commService
	 * @param strucBaseService
	 */
	public void copyStruct(Integer sourceSbId, Integer cloneSbId, CommService commService, StrucBaseService strucBaseService) throws CloneNotSupportedException ;
}
