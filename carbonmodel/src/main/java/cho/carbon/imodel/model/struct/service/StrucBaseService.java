package cho.carbon.imodel.model.struct.service;

import java.util.List;

import cho.carbon.imodel.model.modelitem.vo.ViewLabel;
import cho.carbon.imodel.model.struct.pojo.StrucBase;
import cho.carbon.imodel.model.struct.vo.StrucBaseContainer;
import cho.carbon.meta.enun.StrucElementType;
import cn.sowell.copframe.dto.page.PageInfo;

public interface StrucBaseService {

	/**
	 * 分页查询数据
	 * @param strucBase
	 * @param pageInfo
	 * @return
	 */
	List<StrucBase> queryList(StrucBase strucBase, PageInfo pageInfo);

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
	void quickCreateStrucBase(String belongModel) throws Exception ;

}
