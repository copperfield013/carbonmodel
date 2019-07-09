package cho.carbon.imodel.model.modelitem.dao;

import java.util.List;


import cho.carbon.imodel.model.modelitem.pojo.MiCascade;
import cho.carbon.imodel.model.modelitem.pojo.MiFilterCriterion;
import cho.carbon.imodel.model.modelitem.pojo.MiTwolevelMapping;
import cho.carbon.imodel.model.modelitem.pojo.ModelItem;
import cho.carbon.imodel.model.modelitem.vo.ModelItemContainer;
import cho.carbon.meta.enun.ModelItemType;
import cn.sowell.copframe.dto.page.PageInfo;

public interface ModelItemDao {
	/**
	 * 分页显示模型信息
	 * @param modelItem
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public List<ModelItem> queryList(ModelItem modelItem, PageInfo pageInfo) throws Exception;
	
	
	/**
	 * 保存模型信息
	 * @param modelItemContainer
	 * @return
	 * @throws Exception
	 */
	ModelItemContainer saveOrUpdate(ModelItemContainer modelItemContainer)  throws Exception ;
	
	/**
	 * 根据父亲code 获取下一阶梯孩子
	 * @param pmiCode   父亲的code
	 */
	List<ModelItem> getModelItemStairChild(String pmiCode);
	
	/**
	 * @param parentCode  父code
	 * @param existMiType  ModelItemType 中的类型  存在这个类型
	 * @param noNiType  ModelItemType 中的类型   不存在这个类型
	 * @param miUsingState ModelItem 状态值     Constants.USING_STATE_NORMAL
	 * @return 返回父亲下面指定类型的孩子
	 * @throws Exception
	 */
	List<ModelItem> getModelItemByType(String parentCode, ModelItemType[] 
			existMiTypes, ModelItemType[] noNiTypes, Integer miUsingState) throws Exception;
	
	/**
	 * 
	 * @param belongMode  属于模型Code
	 * @param pTypes  父亲的类型， 是单行组还是多行组， 或者两者都有，   ModelItem Type 里面为
	 * @param needCorrelation   是否需要伴生属性  true : 需要
	 * @param chilTypes 获取指定类型的孩子
	 * @return   根据所传类型， 返回普通分组下的孩子 ， 多值分组下的孩子， 或者前两者
	 */
	List<ModelItem> getModelItemByBelongMode(String belongMode, ModelItemType[] pTypes, ModelItemType[] chilTypes, boolean needCorrelation);
	
	/**
	 * @param miCasEnumCode   级联枚举or级联引用的code
	 * @return 返回 级联枚举or级联引用的孩子
	 */
	List<MiCascade> getMiCascadeList(String miCasEnumCode);
	
	/**
	 * 
	 * @param moreLineCode   ModelItem中多行组的code
	 * @return 多行组对应的二级属性mapping
	 */
	List<MiTwolevelMapping> getMiTwoMapping(String moreLineCode);
	
	/**
	 * 根据mappingID 获取MiTwolevelMapping下面的所有二级属性
	 * @param mappingId
	 * @return
	 */
	List getTwoAttrByMappingId(String mappingId);

	/**
	 * 获取ModelItemType.MODEL
	 * @param 
	 * @return
	 */
	public List<ModelItem> getModelList();


	/**
	 * 获取分组的孩子， 表达式
	 * @return
	 */
	public List<MiFilterCriterion> getMiFiltergroupChild(Integer groupId);
	
}
