package cho.carbon.imodel.model.struct.service;

import java.util.List;

import cho.carbon.hc.copframe.dto.page.PageInfo;
import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.pojo.StrucRelation;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.StrucElementType;

public interface StrucBaseService {

	/**
	 * 分页查询数据
	 * @param strucBase
	 * @param pageInfo
	 * @return
	 */
	List<StrucBase> queryList(StrucBase strucBase, PageInfo pageInfo);
	
	/**
	 * 获取所有结构体
	 * @param modelCode 获取item， 对应的结构体， 若为null , 则获取所有结构体
	 * @return StrucBase
	 */
	List<StrucBase> getAllStruc(String modelCode);

	/**
	 * 	保存结构体
	 * @param strucBaseController
	 * @return
	 */
	StrucBaseContainer saveOrUpdate(StrucBaseContainer strucBaseContainer);

	/**
	 * 获取模型必填属性
	 * @param sbId
	 * @param type
	 * @param sbPid
	 * @return
	 */
	List<ViewLabel> getDefaultAttrByMType(Integer sbId, StrucElementType type, Integer sbPid)  throws Exception;

	/**
	 * 根据父id，获取下一阶梯的孩子
	 * @param sbPid
	 * @return
	 */
	List<StrucBase> getStructStairChild(Integer sbPid);

	/**
	 * 快速生成结构体
	 * @param belongModel
	 */
	void createStrucBaseQuick(String belongModel) throws Exception ;

	/**
	 * 获取结构体下， 所有Group1D的孩子
	 * @param id
	 * @return
	 */
	List<StrucBase> getGroup1DChild(Integer sbId);
	
	/**
	 * 根据sbId, 获取StrucRelation
	 * @param sbId
	 * @return
	 */
	List<StrucRelation> getStrucRelationBySbId(Integer sbId);

	/**
	 * 	删除结构体
	 * @param sbId
	 */
	void deleteStruct(Integer sbId);

	/**
	 * 复制结构体
	 * @param sbId
	 */
	void createCopyStruct(Integer sbId)  throws Exception ;

}
