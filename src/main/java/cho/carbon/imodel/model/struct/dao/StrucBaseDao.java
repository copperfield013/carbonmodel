package cho.carbon.imodel.model.struct.dao;

import java.util.List;

import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;

public interface StrucBaseDao {

	/**
	 * 分页查询结构体数据
	 * @param strucBase
	 * @param pageInfo
	 * @return
	 */
	List<StrucBase> queryList(StrucBase strucBase, PageInfo pageInfo);

	/**
	 * 根据父id， 获取下一阶梯的孩子
	 * @param sbPid
	 * @return
	 */
	List<StrucBase> getStructStairChild(Integer sbPid);

	/**
	 * 获取所有结构体
	 * @param modelCode 获取item， 对应的结构体， 若为null , 则获取所有结构体
	 * @return StrucBase
	 */
	List<StrucBase> getAllStruc(String modelCode);

	/**
	 * 获取结构体下， 所有Group1D的孩子
	 * @param sbId
	 * @return
	 */
	List<StrucBase> getGroup1DChild(Integer sbId);
	
	/**
	 * 根据sbId, 获取StrucRelation
	 * @param sbId
	 * @return
	 */
	List<StrucRelation> getStrucRelationBySbId(Integer sbId);
	
}
